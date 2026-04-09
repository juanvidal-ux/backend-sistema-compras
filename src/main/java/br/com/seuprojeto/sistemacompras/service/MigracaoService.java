package br.com.seuprojeto.sistemacompras.service;

import br.com.seuprojeto.sistemacompras.model.*;
import br.com.seuprojeto.sistemacompras.repository.EntidadeFaturamentoRepository;
import br.com.seuprojeto.sistemacompras.repository.FornecedorRepository;
import br.com.seuprojeto.sistemacompras.repository.PedidoCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MigracaoService {

    @Autowired private PedidoCompraRepository pedidoRepository;
    @Autowired private FornecedorRepository fornecedorRepository;
    @Autowired private EntidadeFaturamentoRepository entidadeRepository;

    private static final String FLAG_MIGRACAO = "Migração"; 

    private static final Map<String, List<String>> MAPA_BI = Map.of(
        "Consumiveis", List.of("Projetos", "Partículas", "Microscopia", "Química Air Liquide", "Química - Consumo geral", "DRX"),
        "Equipamentos e Reformas", List.of("DRX", "Geral - AC", "Microscopia", "Partículas", "Projetos", "Química", "Química ICP Horiba"),
        "Escritorio e Uniformes", List.of("Saco plasticos", "Tinta / afins", "Luva latex", "Gimba", "Uniformes", "Copo /papel", "Café e afins", "Maskface", "Diversos"),
        "Reembolsos", List.of("DRX", "Particulas", "Quimica", "Projetos", "Manutenção geral", "Microscopia", "Outros")
    );

    private static final DateTimeFormatter FORMATTER_COMPLETO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATTER_CURTO_BR = new DateTimeFormatterBuilder()
            .appendPattern("d/M/yy")
            .parseDefaulting(ChronoField.YEAR_OF_ERA, 2000)
            .toFormatter();
    private static final DateTimeFormatter FORMATTER_CURTO_US = new DateTimeFormatterBuilder()
            .appendPattern("M/d/yy")
            .parseDefaulting(ChronoField.YEAR_OF_ERA, 2000)
            .toFormatter();

    /**
     * PASSO 1: VALIDAR (Dry Run)
     */
    @Transactional(readOnly = true) 
    public MigracaoResponseDTO validarMigracao(List<MigracaoRequestDTO> linhas) {
        
        MigracaoResponseDTO resposta = new MigracaoResponseDTO("Validação iniciada.");
        
        Map<String, Fornecedor> cacheFornecedores = new HashMap<>();
        Map<String, EntidadeFaturamento> cacheEntidades = new HashMap<>();
        Map<String, PedidoCompra> cachePedidos = new HashMap<>();
        int linhaNum = 0;

        for (MigracaoRequestDTO linha : linhas) {
            linhaNum++;
            resposta.incrementLinhas();

            try {
                validarCamposObrigatorios(linha, linhaNum); 
                validarLogicaBI(linha.getTipoCompra(), linha.getItemArea(), linhaNum);
                parseData(linha.getDataEmissao(), linhaNum);
                
                parseValor(linha.getItemValorTotal(), linhaNum); 
                parseValor(linha.getValorUnit(), linhaNum);
                
                // --- CORREÇÃO (Erro 1) ---
                // Converte o Double da Qtd para String ANTES de enviar ao parser
                String qtdComoString = (linha.getQtd() != null) ? linha.getQtd().toString() : null;
                parseValor(qtdComoString, linhaNum); // V6.9 valida a Qtd
                // -------------------------
                
                findOrCreateFornecedor(linha.getFornecedor(), cacheFornecedores, resposta, true);
                findOrCreateEntidade(linha.getEntidade(), cacheEntidades, resposta, true);
                
                String pcn = linha.getPcn().trim(); 
                PedidoCompra pedido = cachePedidos.get(pcn);

                if (pedido == null) {
                    pedido = new PedidoCompra();
                    pedido.setCodigoPcn(pcn);
                    cachePedidos.put(pcn, pedido);
                    resposta.incrementPedidos();
                }
                
                resposta.incrementItens();

            } catch (MigracaoValidationException e) {
                throw e; 
            } catch (Exception e) {
                throw new MigracaoValidationException("Linha " + linhaNum + ": Erro inesperado. " + e.getMessage());
            }
        }
        
        resposta.setMensagem("Validação concluída. Nenhum erro encontrado.");
        return resposta;
    }

    /**
     * PASSO 2: EXECUTAR (Gravar)
     */
    @Transactional
    public MigracaoResponseDTO processarMigracao(List<MigracaoRequestDTO> linhas) {
        
        MigracaoResponseDTO resposta = new MigracaoResponseDTO("Migração iniciada.");
        
        Map<String, Fornecedor> cacheFornecedores = new HashMap<>();
        Map<String, EntidadeFaturamento> cacheEntidades = new HashMap<>();
        Map<String, PedidoCompra> cachePedidos = new HashMap<>();
        int linhaNum = 0;

        for (MigracaoRequestDTO linha : linhas) {
            linhaNum++;
            resposta.incrementLinhas();

            validarCamposObrigatorios(linha, linhaNum);
            LocalDate data = parseData(linha.getDataEmissao(), linhaNum);
            
            BigDecimal vlrTotal = parseValor(linha.getItemValorTotal(), linhaNum); 
            BigDecimal vlrUnit = parseValor(linha.getValorUnit(), linhaNum);
            
            // --- CORREÇÃO (Erro 2) ---
            String qtdComoString = (linha.getQtd() != null) ? linha.getQtd().toString() : null;
            BigDecimal qtd = parseValor(qtdComoString, linhaNum); 
            // ---------------------

            String tipoCompraNormalizado = normalizarTipoCompra(linha.getTipoCompra().trim());
            Fornecedor f = findOrCreateFornecedor(linha.getFornecedor(), cacheFornecedores, resposta, false);
            EntidadeFaturamento e = findOrCreateEntidade(linha.getEntidade(), cacheEntidades, resposta, false);
            String pcn = linha.getPcn().trim();
            PedidoCompra pedido = cachePedidos.get(pcn);

            if (pedido == null) {
                pedido = new PedidoCompra();
                pedido.setCodigoPcn(pcn);
                pedido.setDataEmissao(data);
                pedido.setResponsavel(linha.getResponsavel() != null ? linha.getResponsavel().trim() : null);
                pedido.setTipoCompra(tipoCompraNormalizado); 
                pedido.setFornecedor(f);
                pedido.setEntidadeFaturamento(e);
                pedido.setLocalEmissao(FLAG_MIGRACAO); 
                pedido.setTotalPedido(BigDecimal.ZERO); 
                
                cachePedidos.put(pcn, pedido);
                resposta.incrementPedidos();
            }

            ItemPedido item = new ItemPedido();
            item.setItemNum(pedido.getItens().size() + 1);
            item.setDescricao(linha.getItemDescricao() != null ? linha.getItemDescricao().trim() : "N/A (Migrado)");
            item.setArea(linha.getItemArea().trim());
            item.setQuantidade(qtd);
            item.setValorUnitario(vlrUnit);
            item.setValorTotal(vlrTotal);
            
            item.setPedidoCompra(pedido);
            pedido.getItens().add(item);
            pedido.setTotalPedido(pedido.getTotalPedido().add(item.getValorTotal()));
            
            resposta.incrementItens();
        }

        pedidoRepository.saveAll(cachePedidos.values());

        resposta.setMensagem("Migração concluída com sucesso!");
        return resposta;
    }

    /**
     * PASSO 3: EXCLUIR (Rollback) - (V6.7)
     */
    @Transactional
    public long excluirDadosMigrados() {
        // --- CORREÇÃO (Erro 3) ---
        // Agora o repositório tem o método
        List<PedidoCompra> pedidosMigrados = pedidoRepository.findByLocalEmissao(FLAG_MIGRACAO);
        long count = pedidosMigrados.size();
        
        if (count > 0) {
            // --- CORREÇÃO (Erro 4) ---
            pedidoRepository.deleteByLocalEmissao(FLAG_MIGRACAO);
        }
        return count;
    }


    // --- MÉTODOS AUXILIARES ---

    private Fornecedor findOrCreateFornecedor(String nome, Map<String, Fornecedor> cache, MigracaoResponseDTO resposta, boolean isDryRun) {
        if (nome == null || nome.trim().isEmpty()) return null;
        String nomeTrim = nome.trim(); 
        
        if (cache.containsKey(nomeTrim)) return cache.get(nomeTrim);
        Fornecedor f = fornecedorRepository.findByNome(nomeTrim);
        
        if (f == null) {
            if (isDryRun) { 
                f = new Fornecedor(); f.setNome(nomeTrim); 
            } else { 
                f = new Fornecedor(); f.setNome(nomeTrim);
                f = fornecedorRepository.save(f);
                resposta.incrementFornecedores();
            }
        }
        cache.put(nomeTrim, f);
        return f;
    }

    private EntidadeFaturamento findOrCreateEntidade(String nome, Map<String, EntidadeFaturamento> cache, MigracaoResponseDTO resposta, boolean isDryRun) {
        if (nome == null || nome.trim().isEmpty()) return null;
        String nomeTrim = nome.trim(); 
        
        if (cache.containsKey(nomeTrim)) return cache.get(nomeTrim);
        EntidadeFaturamento e = entidadeRepository.findByNomeFantasia(nomeTrim);
        
        if (e == null) {
            if (isDryRun) { 
                e = new EntidadeFaturamento(); e.setNomeFantasia(nomeTrim); 
            } else { 
                e = new EntidadeFaturamento();
                e.setNomeFantasia(nomeTrim);
                e.setRazaoSocial("Migrado - " + nomeTrim); 
                e.setCnpj("00.000.000/0000-00"); 
                e = entidadeRepository.save(e);
                resposta.incrementEntidades();
            }
        }
        cache.put(nomeTrim, e);
        return e;
    }
    
    // --- Validadores (V6.9) ---
    private void validarCamposObrigatorios(MigracaoRequestDTO linha, int n) {
        if (linha.getPcn() == null || linha.getPcn().trim().isEmpty())
            throw new MigracaoValidationException("Linha " + n + ": Coluna 'PCN' está vazia.");
        if (linha.getDataEmissao() == null || linha.getDataEmissao().trim().isEmpty())
            throw new MigracaoValidationException("Linha " + n + ": Coluna 'Data Emissão' está vazia.");
        if (linha.getItemDescricao() == null || linha.getItemDescricao().trim().isEmpty())
            throw new MigracaoValidationException("Linha " + n + ": Coluna 'Item Descrição' está vazia.");
        if (linha.getTipoCompra() == null || linha.getTipoCompra().trim().isEmpty())
            throw new MigracaoValidationException("Linha " + n + ": Coluna 'Tipo Compra (BI)' está vazia.");
        if (linha.getItemArea() == null || linha.getItemArea().trim().isEmpty())
            throw new MigracaoValidationException("Linha " + n + ": Coluna 'Item Área (BI)' está vazia.");
        if (linha.getItemValorTotal() == null || linha.getItemValorTotal().trim().isEmpty())
            throw new MigracaoValidationException("Linha " + n + ": Coluna 'Item Valor Total' está vazia.");
        
        // --- CORREÇÃO (Erro 5) ---
        // O validador agora checa se o Double é nulo, não se o .trim() está vazio
        if (linha.getQtd() == null)
            throw new MigracaoValidationException("Linha " + n + ": Coluna 'Item Qtd' está vazia.");
        // -------------------------
    }

    private String normalizarTipoCompra(String tipoCompra) {
        if (tipoCompra.equalsIgnoreCase("Reembolso")) {
            return "Reembolsos"; 
        }
        return tipoCompra;
    }

    private void validarLogicaBI(String tipoCompra, String itemArea, int n) {
        String tipoCompraTrim = tipoCompra.trim();
        String itemAreaTrim = itemArea.trim(); 
        String tipoCompraNormalizado = normalizarTipoCompra(tipoCompraTrim);

        if (!MAPA_BI.containsKey(tipoCompraNormalizado)) {
            throw new MigracaoValidationException("Linha " + n + ": 'Tipo Compra (BI)' inválido: '" + tipoCompra + "'. Valores esperados: " + MAPA_BI.keySet());
        }
        
        // --- CORREÇÃO (Erro 6) ---
        // Faltava uma aspa em replace("á", "a")
        String areaNormalizada = itemAreaTrim.replace("í", "i").replace("á", "a");
        // -------------------------
        
        List<String> areasValidas = MAPA_BI.get(tipoCompraNormalizado).stream()
                                    .map(s -> s.replace("í", "i").replace("á", "a"))
                                    .toList();
        
        if (!areasValidas.contains(areaNormalizada)) {
            throw new MigracaoValidationException("Linha " + n + ": Combinação inválida. A Área '" + itemArea + "' não pertence ao Tipo '" + tipoCompra + "'.");
        }
    }

    private LocalDate parseData(String dataExcel, int n) {
        String dataTrim = dataExcel.trim(); 
        try {
            return LocalDate.parse(dataTrim, FORMATTER_COMPLETO_BR);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(dataTrim, FORMATTER_CURTO_US);
            } catch (DateTimeParseException e2) {
                try {
                     return LocalDate.parse(dataTrim, FORMATTER_CURTO_BR);
                } catch (DateTimeParseException e3) {
                    throw new MigracaoValidationException("Linha " + n + ": Data em formato inválido: '" + dataExcel + "'. Use DD/MM/YYYY.");
                }
            }
        }
    }

    // (V6.8 - Robusto)
    private BigDecimal parseValor(String valorExcel, int n) {
        if (valorExcel == null || valorExcel.trim().isEmpty()) return BigDecimal.ZERO;
        
        String valorLimpo = valorExcel
                .replace("R$", "")
                .replace(",", "") 
                .trim();
        
        try {
            return new BigDecimal(valorLimpo);
        } catch (NumberFormatException e) {
             try {
                String valorLimpoBR = valorExcel.replace("R$", "").replace(".", "").replace(",", ".").trim();
                return new BigDecimal(valorLimpoBR);
             } catch (NumberFormatException e2) {
                throw new MigracaoValidationException("Linha " + n + ": Valor/Qtd em formato inválido: '" + valorExcel + "'.");
             }
        }
    }
}
package br.com.seuprojeto.sistemacompras.service;

import br.com.seuprojeto.sistemacompras.model.ItemPedido;
import br.com.seuprojeto.sistemacompras.model.PedidoCompra;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class DocumentoService {

    // Formato de data (ex: 28 de março de 2024)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
    private static final DateTimeFormatter SHORT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerarDocumentoWord(PedidoCompra pedido) throws Exception {

        // Carrega o arquivo da pasta src/main/resources/
        InputStream inputStream = new ClassPathResource("template.docx").getInputStream();
        
        try (XWPFDocument doc = new XWPFDocument(inputStream)) {

            // 1. Substituir textos (Com proteção contra nulos)
            substituirPlaceholders(doc, pedido);

            // 2. Preencher a tabela de itens
            // Pega a primeira tabela do documento
            if (!doc.getTables().isEmpty()) {
                XWPFTable tabelaItens = doc.getTables().get(0);
                
                // Verifica se a tabela tem linhas suficientes (Cabeçalho + Modelo)
                if (tabelaItens.getRows().size() > 1) {
                    XWPFTableRow linhaTemplateItem = tabelaItens.getRow(1); // Linha 2 (índice 1) é o modelo
                    
                    // Copia o XML da linha para poder duplicar
                    CTRow templateRowXml = (CTRow) linhaTemplateItem.getCtRow().copy();

                    List<ItemPedido> itens = pedido.getItens();
                    if (itens != null && !itens.isEmpty()) {
                        
                        // Preenche a primeira linha de dados
                        preencherLinhaItem(linhaTemplateItem, itens.get(0));

                        // Cria linhas para os demais itens
                        for (int i = 1; i < itens.size(); i++) {
                            XWPFTableRow novaLinha = new XWPFTableRow((CTRow) templateRowXml.copy(), tabelaItens);
                            preencherLinhaItem(novaLinha, itens.get(i));
                            tabelaItens.addRow(novaLinha, i + 1);
                        }
                    } else {
                        // Se não tiver itens, limpa os placeholders da linha modelo
                        limparLinhaItem(linhaTemplateItem);
                    }
                }
            }

            // 3. Gerar o arquivo final em memória
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            doc.write(b);
            return b.toByteArray();
        }
    }

    // --- SUBSTITUIÇÃO BLINDADA (NÃO QUEBRA SE TIVER NULL) ---
    private void substituirPlaceholders(XWPFDocument doc, PedidoCompra pedido) {
        
        // Cabeçalho
        replaceText(doc, "${pcn_code}", safe(pedido.getCodigoPcn()));
        
        String dataEmissao = "N/A";
        if (pedido.getDataEmissao() != null) dataEmissao = pedido.getDataEmissao().format(DATE_FORMATTER);
        replaceText(doc, "${data_emissao}", dataEmissao);

        replaceText(doc, "${prezado}", safe(pedido.getPrezado()));
        replaceText(doc, "${num_orcamento}", safe(pedido.getNumOrcamento()));
        
        String dataOrc = "N/A";
        if (pedido.getDataOrcamento() != null) dataOrc = pedido.getDataOrcamento().format(SHORT_DATE);
        replaceText(doc, "${data_orcamento}", dataOrc);
        
        replaceText(doc, "${numero_projeto}", safe(pedido.getNumeroProjeto()));

        // Fornecedor
        String fNome = "N/A", fCnpj = "", fTel = "";
        if (pedido.getFornecedor() != null) {
            fNome = safe(pedido.getFornecedor().getNome());
            fCnpj = safe(pedido.getFornecedor().getCnpj());
            fTel = safe(pedido.getFornecedor().getTelefone());
        }
        replaceText(doc, "${fornecedor_nome}", fNome);
        replaceText(doc, "${fornecedor_cnpj}", fCnpj);
        replaceText(doc, "${fornecedor_telefone}", fTel);
        replaceText(doc, "${contato_nome}", safe(pedido.getContatoFornecedorNome()));
        replaceText(doc, "${contato_email}", safe(pedido.getContatoFornecedorEmail()));

        // Financeiro
        String total = "0,00";
        if (pedido.getTotalPedido() != null) total = pedido.getTotalPedido().toString();
        replaceText(doc, "${total_pedido}", total);
        
        replaceText(doc, "${condicoes_pagamento}", safe(pedido.getCondicoesPagamento()));
        replaceText(doc, "${prazo_entrega}", safe(pedido.getPrazoEntrega()));

        // Entidade de Faturamento
        String eRazao = "N/A", eCnpj = "", eIe = "", eCr = "", eEnd = "";
        if (pedido.getEntidadeFaturamento() != null) {
            eRazao = safe(pedido.getEntidadeFaturamento().getRazaoSocial());
            eCnpj = safe(pedido.getEntidadeFaturamento().getCnpj());
            eIe = safe(pedido.getEntidadeFaturamento().getInscricaoEstadual());
            eCr = safe(pedido.getEntidadeFaturamento().getNumeroCr());
            eEnd = safe(pedido.getEntidadeFaturamento().getEnderecoCompleto());
        }
        replaceText(doc, "${entidade_razao_social}", eRazao);
        replaceText(doc, "${entidade_cnpj}", eCnpj);
        replaceText(doc, "${entidade_ie}", eIe);
        replaceText(doc, "${entidade_cr}", eCr);
        replaceText(doc, "${entidade_endereco}", eEnd);

        // Observações
        replaceText(doc, "${observacoes}", safe(pedido.getObservacoes()));

        // Local de Entrega
        String lHeader = "N/A", lDepto = "", lLab = "", lEnd = "", lContato = "", lResp = "", lCargo = "";
        if (pedido.getLocalEntrega() != null) {
            lHeader = safe(pedido.getLocalEntrega().getHeaderInstituicao());
            lDepto = safe(pedido.getLocalEntrega().getDepartamento());
            lLab = safe(pedido.getLocalEntrega().getLaboratorio());
            lEnd = safe(pedido.getLocalEntrega().getEndereco());
            lContato = safe(pedido.getLocalEntrega().getHeaderContato());
            lResp = safe(pedido.getLocalEntrega().getContatoResponsavel());
            lCargo = safe(pedido.getLocalEntrega().getContatoCargo());
        }
        replaceText(doc, "${local_header}", lHeader);
        replaceText(doc, "${local_depto}", lDepto);
        replaceText(doc, "${local_lab}", lLab);
        replaceText(doc, "${local_endereco}", lEnd);
        replaceText(doc, "${local_contato}", lContato);
        replaceText(doc, "${local_responsavel_nome}", lResp);
        replaceText(doc, "${local_responsavel_cargo}", lCargo);
    }

    // Helper para evitar NullPointerException em Strings
    private String safe(String s) {
        return s != null ? s : "";
    }

    private void preencherLinhaItem(XWPFTableRow linha, ItemPedido item) {
        // Garante que a célula existe antes de tentar escrever (try-catch cell index)
        if(linha.getTableCells().size() >= 5) {
            replaceTextInCell(linha.getCell(0), "${item_num}", String.valueOf(item.getItemNum()));
            replaceTextInCell(linha.getCell(1), "${item_desc}", safe(item.getDescricao()));
            replaceTextInCell(linha.getCell(2), "${item_qtd}", item.getQuantidade() != null ? String.valueOf(item.getQuantidade()) : "0");
            replaceTextInCell(linha.getCell(3), "${item_vlr_unit}", item.getValorUnitario() != null ? String.valueOf(item.getValorUnitario()) : "0.00");
            replaceTextInCell(linha.getCell(4), "${item_vlr_total}", item.getValorTotal() != null ? String.valueOf(item.getValorTotal()) : "0.00");
        }
    }
    
    private void limparLinhaItem(XWPFTableRow linha) {
        if(linha.getTableCells().size() >= 5) {
            replaceTextInCell(linha.getCell(0), "${item_num}", "-");
            replaceTextInCell(linha.getCell(1), "${item_desc}", "Sem Itens");
            replaceTextInCell(linha.getCell(2), "${item_qtd}", "");
            replaceTextInCell(linha.getCell(3), "${item_vlr_unit}", "");
            replaceTextInCell(linha.getCell(4), "${item_vlr_total}", "");
        }
    }

    // --- LÓGICA DE SUBSTITUIÇÃO ---

    private void replaceText(XWPFDocument doc, String find, String replace) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            replaceTextInParagraph(p, find, replace);
        }
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    replaceTextInCell(cell, find, replace);
                }
            }
        }
    }

    private void replaceTextInCell(XWPFTableCell cell, String find, String replace) {
        for (XWPFParagraph p : cell.getParagraphs()) {
            replaceTextInParagraph(p, find, replace);
        }
    }

    private void replaceTextInParagraph(XWPFParagraph p, String find, String replace) {
        if (replace == null) replace = "";
        
        // Verifica no texto completo do parágrafo se o placeholder existe
        // Isso ajuda se o Word quebrou o placeholder em vários "runs"
        String fullText = p.getText();
        if (fullText != null && fullText.contains(find)) {
            
            String newText = fullText.replace(find, replace);
            
            // Limpa todas as "runs" antigas
            while (p.getRuns().size() > 0) {
                p.removeRun(0);
            }
            
            // Cria uma nova run com o texto corrigido
            XWPFRun newRun = p.createRun();
            
            // Trata quebra de linha
            if (newText.contains("\n")) {
                String[] lines = newText.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    newRun.setText(lines[i]);
                    if (i < lines.length - 1) newRun.addBreak();
                }
            } else {
                newRun.setText(newText);
            }
        }
    }
}
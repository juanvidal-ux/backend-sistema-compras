package br.com.seuprojeto.sistemacompras.service;

import br.com.seuprojeto.sistemacompras.model.AbcResultDTO;
import br.com.seuprojeto.sistemacompras.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BiService {

    @Autowired
    private ItemPedidoRepository itemRepository;

    public List<AbcResultDTO> getCurvaAbcInteligente(String filtroUsuario, String anoStr) {

        Integer ano = converterAno(anoStr);

        if (ano == null) {
            return List.of();
        }

        try {

            List<AbcResultDTO> resultado;

            switch (filtroUsuario) {

                case "Admin geral":
                    resultado = itemRepository.findAbcByTipoCompraAndAno("Escritorio e Uniformes", ano);
                    break;

                case "Quimica":
                    resultado = itemRepository.findAbcQuimicaGeral(ano);
                    break;

                case "Manutenção Geral":
                    resultado = itemRepository.findAbcManutencaoGeral(ano);
                    break;

                default:
                    resultado = itemRepository.findAbcByAreaAndAno(filtroUsuario, ano);
                    break;
            }

            // 🔒 PROTEÇÃO CONTRA NULL
            if (resultado == null) {
                return List.of();
            }

            // 🔒 REMOVE ITENS NULOS
            return resultado.stream()
                    .filter(Objects::nonNull)
                    .toList();

        } catch (Exception e) {

            // 🔥 LOG PARA DEBUG NO RENDER
            System.out.println("ERRO NA CURVA ABC:");
            e.printStackTrace();

            // NÃO QUEBRA O FRONT
            return List.of();
        }
    }

    public Map<Integer, BigDecimal> getResumoFinanceiro(Integer ano, String setor) {

        try {
            List<Object[]> resultados = itemRepository.somarPorMesGeral(ano);

            Map<Integer, BigDecimal> mapa = new HashMap<>();

            for (int i = 1; i <= 12; i++) {
                mapa.put(i, BigDecimal.ZERO);
            }

            if (resultados != null) {
                for (Object[] row : resultados) {
                    if (row != null && row.length >= 2) {
                        mapa.put(
                                (Integer) row[0],
                                (BigDecimal) row[1]
                        );
                    }
                }
            }

            return mapa;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private Integer converterAno(String anoStr) {
        try {
            return Integer.parseInt(anoStr);
        } catch (Exception e) {
            return null; // ⚠️ antes estava forçando 2024 → pode dar erro no banco
        }
    }
}

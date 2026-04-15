package br.com.seuprojeto.sistemacompras.service;

import br.com.seuprojeto.sistemacompras.model.AbcResultDTO;
import br.com.seuprojeto.sistemacompras.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BiService {

    @Autowired
    private ItemPedidoRepository itemRepository;

    public List<AbcResultDTO> getCurvaAbcInteligente(String filtroUsuario, String anoStr) {
        Integer ano = converterAno(anoStr);
        if (ano == null) {
            return List.of();
        }

        String filtroNormalizado = normalizarTexto(filtroUsuario);

        try {
            List<AbcResultDTO> resultado;

            switch (filtroNormalizado) {
                case "admin geral":
                    resultado = itemRepository.findAbcByTipoCompraAndAno("Escritorio e Uniformes", ano);
                    break;

                case "quimica":
                    resultado = itemRepository.findAbcQuimicaGeral(ano);
                    break;

                case "manutencao geral":
                    resultado = itemRepository.findAbcManutencaoGeral(ano);
                    break;

                default:
                    resultado = itemRepository.findAbcByAreaAndAno(filtroUsuario, ano);
                    break;
            }

            if (resultado == null) {
                return List.of();
            }

            return resultado.stream()
                    .filter(Objects::nonNull)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
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
                    if (row != null && row.length >= 2 && row[0] != null && row[1] != null) {
                        mapa.put((Integer) row[0], (BigDecimal) row[1]);
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
            return null;
        }
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return semAcento.trim().toLowerCase();
    }
}

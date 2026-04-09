package br.com.seuprojeto.sistemacompras.service;

import br.com.seuprojeto.sistemacompras.model.AbcResultDTO;
import br.com.seuprojeto.sistemacompras.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BiService {

    @Autowired
    private ItemPedidoRepository itemRepository;

    public List<AbcResultDTO> getCurvaAbcInteligente(String filtroUsuario, String anoStr) {
        Integer ano = converterAno(anoStr);
        if (ano == null) return List.of();

        switch (filtroUsuario) {
            
            case "Admin geral":
                return itemRepository.findAbcByTipoCompraAndAno("Escritorio e Uniformes", ano);

            case "Quimica":
                // USA O NOVO MÉTODO BLINDADO (Com ou sem acento)
                return itemRepository.findAbcQuimicaGeral(ano);

            case "Manutenção Geral":
                  return itemRepository.findAbcManutencaoGeral(ano);

            default:
                // Para Partículas, DRX, etc.
                return itemRepository.findAbcByAreaAndAno(filtroUsuario, ano);
        }
    }
    
    public Map<Integer, BigDecimal> getResumoFinanceiro(Integer ano, String setor) {
        List<Object[]> resultados = itemRepository.somarPorMesGeral(ano);
        Map<Integer, BigDecimal> mapa = new HashMap<>();
        for (int i = 1; i <= 12; i++) mapa.put(i, BigDecimal.ZERO);
        for (Object[] row : resultados) mapa.put((Integer) row[0], (BigDecimal) row[1]);
        return mapa;
    }

    private Integer converterAno(String anoStr) {
        try { return Integer.parseInt(anoStr); } catch (Exception e) { return 2024; }
    }
}
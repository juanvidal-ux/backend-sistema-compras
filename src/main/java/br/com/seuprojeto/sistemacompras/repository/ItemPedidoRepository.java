package br.com.seuprojeto.sistemacompras.repository;

import br.com.seuprojeto.sistemacompras.model.AbcResultDTO;
import br.com.seuprojeto.sistemacompras.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {

    @Query("""
        SELECT new br.com.seuprojeto.sistemacompras.model.AbcResultDTO(
            COALESCE(i.descricao, 'Sem descrição'),
            COALESCE(i.area, 'Sem área'),
            COALESCE(SUM(i.valorTotal), 0)
        )
        FROM ItemPedido i
        JOIN i.pedidoCompra p
        WHERE p.tipoCompra = :tipoCompra
          AND YEAR(p.dataEmissao) = :ano
        GROUP BY i.descricao, i.area
        ORDER BY COALESCE(SUM(i.valorTotal), 0) DESC
    """)
    List<AbcResultDTO> findAbcByTipoCompraAndAno(@Param("tipoCompra") String tipoCompra,
                                                 @Param("ano") Integer ano);

    @Query("""
        SELECT new br.com.seuprojeto.sistemacompras.model.AbcResultDTO(
            COALESCE(i.descricao, 'Sem descrição'),
            COALESCE(i.area, 'Sem área'),
            COALESCE(SUM(i.valorTotal), 0)
        )
        FROM ItemPedido i
        JOIN i.pedidoCompra p
        WHERE (
            LOWER(COALESCE(i.descricao, '')) LIKE 'quimica%' OR
            LOWER(COALESCE(i.descricao, '')) LIKE 'química%' OR
            LOWER(COALESCE(i.area, '')) LIKE 'quimica%' OR
            LOWER(COALESCE(i.area, '')) LIKE 'química%'
        )
          AND YEAR(p.dataEmissao) = :ano
        GROUP BY i.descricao, i.area
        ORDER BY COALESCE(SUM(i.valorTotal), 0) DESC
    """)
    List<AbcResultDTO> findAbcQuimicaGeral(@Param("ano") Integer ano);

    @Query("""
        SELECT new br.com.seuprojeto.sistemacompras.model.AbcResultDTO(
            COALESCE(i.descricao, 'Sem descrição'),
            COALESCE(i.area, 'Sem área'),
            COALESCE(SUM(i.valorTotal), 0)
        )
        FROM ItemPedido i
        JOIN i.pedidoCompra p
        WHERE (
            LOWER(COALESCE(i.area, '')) LIKE 'geral - ac%' OR
            LOWER(COALESCE(i.area, '')) LIKE 'manutenção geral%' OR
            LOWER(COALESCE(i.area, '')) LIKE 'manutencao geral%'
        )
          AND YEAR(p.dataEmissao) = :ano
        GROUP BY i.descricao, i.area
        ORDER BY COALESCE(SUM(i.valorTotal), 0) DESC
    """)
    List<AbcResultDTO> findAbcManutencaoGeral(@Param("ano") Integer ano);

    @Query("""
        SELECT new br.com.seuprojeto.sistemacompras.model.AbcResultDTO(
            COALESCE(i.descricao, 'Sem descrição'),
            COALESCE(i.area, 'Sem área'),
            COALESCE(SUM(i.valorTotal), 0)
        )
        FROM ItemPedido i
        JOIN i.pedidoCompra p
        WHERE i.area = :area
          AND YEAR(p.dataEmissao) = :ano
        GROUP BY i.descricao, i.area
        ORDER BY COALESCE(SUM(i.valorTotal), 0) DESC
    """)
    List<AbcResultDTO> findAbcByAreaAndAno(@Param("area") String area,
                                           @Param("ano") Integer ano);

    @Query("""
        SELECT MONTH(p.dataEmissao), COALESCE(SUM(i.valorTotal), 0)
        FROM ItemPedido i
        JOIN i.pedidoCompra p
        WHERE YEAR(p.dataEmissao) = :ano
        GROUP BY MONTH(p.dataEmissao)
        ORDER BY MONTH(p.dataEmissao)
    """)
    List<Object[]> somarPorMesGeral(@Param("ano") Integer ano);

    @Query("""
        SELECT MONTH(p.dataEmissao), COALESCE(SUM(i.valorTotal), 0)
        FROM ItemPedido i
        JOIN i.pedidoCompra p
        WHERE YEAR(p.dataEmissao) = :ano
          AND i.area = :area
        GROUP BY MONTH(p.dataEmissao)
        ORDER BY MONTH(p.dataEmissao)
    """)
    List<Object[]> somarPorMesESetor(@Param("ano") Integer ano,
                                     @Param("area") String area);
}

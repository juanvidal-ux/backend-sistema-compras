package br.com.seuprojeto.sistemacompras.repository;

import br.com.seuprojeto.sistemacompras.model.AbcResultDTO;
import br.com.seuprojeto.sistemacompras.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {

    // 1. Busca pelo TIPO DE COMPRA (Para Admin Geral / Escritório)
    @Query("SELECT new br.com.seuprojeto.sistemacompras.model.AbcResultDTO(i.descricao, i.area, SUM(i.valorTotal)) " +
           "FROM ItemPedido i JOIN i.pedidoCompra p " +
           "WHERE p.tipoCompra = :tipoCompra AND YEAR(p.dataEmissao) = :ano " +
           "GROUP BY i.descricao, i.area ORDER BY SUM(i.valorTotal) DESC")
    List<AbcResultDTO> findAbcByTipoCompraAndAno(@Param("tipoCompra") String tipoCompra, @Param("ano") Integer ano);

    // 2. BUSCA BLINDADA PARA QUÍMICA (Resolve o problema do acento e do prefixo)
    // Procura por 'Quimica' OU 'Química', tanto na Descrição quanto na Área
    @Query("SELECT new br.com.seuprojeto.sistemacompras.model.AbcResultDTO(i.descricao, i.area, SUM(i.valorTotal)) " +
           "FROM ItemPedido i JOIN i.pedidoCompra p " +
           "WHERE (i.descricao LIKE 'Quimica%' OR i.descricao LIKE 'Química%' OR i.area LIKE 'Quimica%' OR i.area LIKE 'Química%') " +
           "AND YEAR(p.dataEmissao) = :ano " +
           "GROUP BY i.descricao, i.area ORDER BY SUM(i.valorTotal) DESC")
    List<AbcResultDTO> findAbcQuimicaGeral(@Param("ano") Integer ano);
    // 3. BUSCA BLINDADA PARA Manutenção Geral (Resolve o problema do acento e do prefixo)
    // Procura por 'Geral - AC e Manutenção geral' tanto na Descrição quanto na Área
      @Query("SELECT new br.com.seuprojeto.sistemacompras.model.AbcResultDTO(i.descricao, i.area, SUM(i.valorTotal)) " +
           "FROM ItemPedido i JOIN i.pedidoCompra p " +
           "WHERE ( i.area LIKE 'Geral - AC%' OR i.area LIKE 'Manutenção geral%') " +
           "AND YEAR(p.dataEmissao) = :ano " +
           "GROUP BY i.descricao, i.area ORDER BY SUM(i.valorTotal) DESC")
       List<AbcResultDTO> findAbcManutencaoGeral(Integer ano);

    // 4. Busca por ÁREA EXATA (Para Partículas, DRX, etc.)
    @Query("SELECT new br.com.seuprojeto.sistemacompras.model.AbcResultDTO(i.descricao, i.area, SUM(i.valorTotal)) " +
           "FROM ItemPedido i JOIN i.pedidoCompra p " +
           "WHERE i.area = :area AND YEAR(p.dataEmissao) = :ano " +
           "GROUP BY i.descricao, i.area ORDER BY SUM(i.valorTotal) DESC")
    List<AbcResultDTO> findAbcByAreaAndAno(@Param("area") String area, @Param("ano") Integer ano);
    
    // --- Métodos Financeiros ---
    @Query("SELECT MONTH(p.dataEmissao), SUM(i.valorTotal) FROM ItemPedido i JOIN i.pedidoCompra p WHERE YEAR(p.dataEmissao) = :ano GROUP BY MONTH(p.dataEmissao)")
    List<Object[]> somarPorMesGeral(@Param("ano") Integer ano);

    @Query("SELECT MONTH(p.dataEmissao), SUM(i.valorTotal) FROM ItemPedido i JOIN i.pedidoCompra p WHERE YEAR(p.dataEmissao) = :ano AND i.area = :area GROUP BY MONTH(p.dataEmissao)")
    List<Object[]> somarPorMesESetor(@Param("ano") Integer ano, @Param("area") String area);
}
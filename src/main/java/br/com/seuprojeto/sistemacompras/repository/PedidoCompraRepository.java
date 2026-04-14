package br.com.seuprojeto.sistemacompras.repository;

import br.com.seuprojeto.sistemacompras.dto.PedidoResumoDTO;
import br.com.seuprojeto.sistemacompras.model.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Integer> {

    void deleteByLocalEmissao(String localEmissao);
    List<PedidoCompra> findByLocalEmissao(String localEmissao);

    @Query("SELECT DISTINCT YEAR(p.dataEmissao) FROM PedidoCompra p WHERE p.dataEmissao IS NOT NULL ORDER BY YEAR(p.dataEmissao) DESC")
    List<Integer> findDistinctAnos();

    @Query("SELECT DISTINCT p FROM PedidoCompra p LEFT JOIN FETCH p.itens ORDER BY p.id DESC")
    List<PedidoCompra> findAllComItens();

    @Query("""
    SELECT new br.com.seuprojeto.sistemacompras.dto.PedidoResumoDTO(
        p.id,
        p.codigoPcn,
        p.dataEmissao,
        p.responsavel,
        p.tipoCompra,
        f.nome,
        p.totalPedido
    )
    FROM PedidoCompra p
    LEFT JOIN p.fornecedor f
    ORDER BY p.id DESC
""")
List<PedidoResumoDTO> listarResumo();
}

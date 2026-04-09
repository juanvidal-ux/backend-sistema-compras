package br.com.seuprojeto.sistemacompras.repository;

import br.com.seuprojeto.sistemacompras.model.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Integer> {
    
    // --- Métodos usados na Migração de Dados ---
    void deleteByLocalEmissao(String localEmissao);
    List<PedidoCompra> findByLocalEmissao(String localEmissao);

    // --- Método 1: Filtro de Anos (Resolve erro: method findDistinctAnos is undefined) ---
    @Query("SELECT DISTINCT YEAR(p.dataEmissao) FROM PedidoCompra p WHERE p.dataEmissao IS NOT NULL ORDER BY YEAR(p.dataEmissao) DESC")
    List<Integer> findDistinctAnos();

    // --- Método 2: Listagem Otimizada (Resolve erro: method findAllComItens is undefined) ---
    // Este método traz o Pedido e os Itens numa única consulta (JOIN FETCH),
    // evitando o problema de "N+1 selects" que trava o sistema.
    @Query("SELECT DISTINCT p FROM PedidoCompra p LEFT JOIN FETCH p.itens ORDER BY p.id DESC")
    List<PedidoCompra> findAllComItens();
}
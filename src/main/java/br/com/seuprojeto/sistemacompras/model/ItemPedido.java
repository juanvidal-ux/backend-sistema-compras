package br.com.seuprojeto.sistemacompras.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; 
import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_num", nullable = false)
    private Integer itemNum;

    @Lob 
    @Column(nullable = false, columnDefinition="TEXT")
    private String descricao;

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario", nullable = false)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    // --- NOVO CAMPO ---
    @Column(name = "area")
    private String area;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "id_pedido_compra", nullable = false)
    @JsonIgnore 
    private PedidoCompra pedidoCompra;

    // --- CONSTRUTOR VAZIO ---
    public ItemPedido() {
    }

    // --- GETTERS E SETTERS ---
    // (Todos os seus getters/setters existentes)

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getItemNum() { return itemNum; }
    public void setItemNum(Integer itemNum) { this.itemNum = itemNum; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public PedidoCompra getPedidoCompra() { return pedidoCompra; }
    public void setPedidoCompra(PedidoCompra pedidoCompra) { this.pedidoCompra = pedidoCompra; }

    // --- GETTER E SETTER PARA O NOVO CAMPO ---
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
}
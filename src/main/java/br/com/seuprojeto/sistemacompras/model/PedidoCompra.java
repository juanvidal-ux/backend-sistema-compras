package br.com.seuprojeto.sistemacompras.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList; // <-- IMPORTAR
import java.util.List;

@Entity
@Table(name = "pedidos_compra")
public class PedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_pcn", unique = true, nullable = false)
    private String codigoPcn;

    @Column(name = "local_emissao", nullable = false)
    private String localEmissao;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "id_entidade_faturamento")
    private EntidadeFaturamento entidadeFaturamento;

    @ManyToOne
    @JoinColumn(name = "id_local_entrega")
    private LocalEntrega localEntrega;

    @Column(name = "contato_fornecedor_nome")
    private String contatoFornecedorNome;

    @Column(name = "contato_fornecedor_email")
    private String contatoFornecedorEmail;

    private String prezado;

    @Column(name = "num_orcamento")
    private String numOrcamento;

    @Column(name = "data_orcamento")
    private LocalDate dataOrcamento;

    @Column(name = "total_pedido", nullable = false)
    private BigDecimal totalPedido;

    @Column(name = "prazo_entrega")
    private String prazoEntrega;

    @Column(name = "condicoes_pagamento")
    private String condicoesPagamento;

    @Column(name = "numero_projeto")
    private String numeroProjeto;

    @Lob 
    @Column(columnDefinition="TEXT")
    private String observacoes;

    @Column(name = "responsavel")
    private String responsavel;
    
    @Column(name = "tipo_compra")
    private String tipoCompra;


    // --- MUDANÇA CRÍTICA AQUI ---
    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>(); // <-- ADICIONAR "= new ArrayList<>()"

    
    // --- CONSTRUTOR VAZIO ---
    public PedidoCompra() {
    }

    // --- GETTERS E SETTERS ---
    // (Cole todos os seus getters e setters aqui... não mude nada neles)
    // ...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCodigoPcn() { return codigoPcn; }
    public void setCodigoPcn(String codigoPcn) { this.codigoPcn = codigoPcn; }
    public String getLocalEmissao() { return localEmissao; }
    public void setLocalEmissao(String localEmissao) { this.localEmissao = localEmissao; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }
    public EntidadeFaturamento getEntidadeFaturamento() { return entidadeFaturamento; }
    public void setEntidadeFaturamento(EntidadeFaturamento entidadeFaturamento) { this.entidadeFaturamento = entidadeFaturamento; }
    public LocalEntrega getLocalEntrega() { return localEntrega; }
    public void setLocalEntrega(LocalEntrega localEntrega) { this.localEntrega = localEntrega; }
    public String getContatoFornecedorNome() { return contatoFornecedorNome; }
    public void setContatoFornecedorNome(String contatoFornecedorNome) { this.contatoFornecedorNome = contatoFornecedorNome; }
    public String getContatoFornecedorEmail() { return contatoFornecedorEmail; }
    public void setContatoFornecedorEmail(String contatoFornecedorEmail) { this.contatoFornecedorEmail = contatoFornecedorEmail; }
    public String getPrezado() { return prezado; }
    public void setPrezado(String prezado) { this.prezado = prezado; }
    public String getNumOrcamento() { return numOrcamento; }
    public void setNumOrcamento(String numOrcamento) { this.numOrcamento = numOrcamento; }
    public LocalDate getDataOrcamento() { return dataOrcamento; }
    public void setDataOrcamento(LocalDate dataOrcamento) { this.dataOrcamento = dataOrcamento; }
    public BigDecimal getTotalPedido() { return totalPedido; }
    public void setTotalPedido(BigDecimal totalPedido) { this.totalPedido = totalPedido; }
    public String getPrazoEntrega() { return prazoEntrega; }
    public void setPrazoEntrega(String prazoEntrega) { this.prazoEntrega = prazoEntrega; }
    public String getCondicoesPagamento() { return condicoesPagamento; }
    public void setCondicoesPagamento(String condicoesPagamento) { this.condicoesPagamento = condicoesPagamento; }
    public String getNumeroProjeto() { return numeroProjeto; }
    public void setNumeroProjeto(String numeroProjeto) { this.numeroProjeto = numeroProjeto; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getTipoCompra() { return tipoCompra; }
    public void setTipoCompra(String tipoCompra) { this.tipoCompra = tipoCompra; }
}
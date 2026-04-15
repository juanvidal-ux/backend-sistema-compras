package br.com.seuprojeto.sistemacompras.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PedidoResumoDTO {

    private Integer id;
    private String codigoPcn;
    private LocalDate dataEmissao;
    private String responsavel;
    private String tipoCompra;
    private String fornecedorNome;
    private BigDecimal totalPedido;

    public PedidoResumoDTO(
            Integer id,
            String codigoPcn,
            LocalDate dataEmissao,
            String responsavel,
            String tipoCompra,
            String fornecedorNome,
            BigDecimal totalPedido
    ) {
        this.id = id;
        this.codigoPcn = codigoPcn;
        this.dataEmissao = dataEmissao;
        this.responsavel = responsavel;
        this.tipoCompra = tipoCompra;
        this.fornecedorNome = fornecedorNome;
        this.totalPedido = totalPedido;
    }

    public Integer getId() { return id; }
    public String getCodigoPcn() { return codigoPcn; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public String getResponsavel() { return responsavel; }
    public String getTipoCompra() { return tipoCompra; }
    public String getFornecedorNome() { return fornecedorNome; }
    public BigDecimal getTotalPedido() { return totalPedido; }
}

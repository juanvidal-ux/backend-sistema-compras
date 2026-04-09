package br.com.seuprojeto.sistemacompras.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// Esta classe é o "Espelho" da sua planilha (image_a28226.png)
// Os nomes das colunas foram CORRIGIDOS para bater 100%
public class MigracaoRequestDTO {

    // Nomes das colunas da sua planilha
    @JsonProperty("PCN") 
    private String pcn;
    
    @JsonProperty("Data Emissão") 
    private String dataEmissao;
    
    @JsonProperty("Responsável") 
    private String responsavel;
    
    @JsonProperty("Tipo Compra (BI)") 
    private String tipoCompra;
    
    @JsonProperty("Fornecedor") 
    private String fornecedor;
    
    @JsonProperty("Faturar em (Recurso)") 
    private String entidade;
    
    @JsonProperty("Item Descrição") 
    private String itemDescricao;
    
    @JsonProperty("Item Área (BI)") 
    private String itemArea;
    
    // --- CORREÇÕES AQUI ---
    @JsonProperty("Item Qtd") 
    private Double qtd; 
    
    @JsonProperty("Item Valor Unit") 
    private String valorUnit;
    
    @JsonProperty("Item Valor Total") 
    private String valorTotal;
    

    // --- GETTERS E SETTERS ---
    // (O MigracaoService.java chama estes nomes)
    
    public String getPcn() { return pcn; }
    public void setPcn(String pcn) { this.pcn = pcn; }
    public String getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(String dataEmissao) { this.dataEmissao = dataEmissao; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getTipoCompra() { return tipoCompra; }
    public void setTipoCompra(String tipoCompra) { this.tipoCompra = tipoCompra; }
    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
    public String getEntidade() { return entidade; }
    public void setEntidade(String entidade) { this.entidade = entidade; }
    public String getItemDescricao() { return itemDescricao; }
    public void setItemDescricao(String itemDescricao) { this.itemDescricao = itemDescricao; }
    public String getItemArea() { return itemArea; }
    public void setItemArea(String itemArea) { this.itemArea = itemArea; }
    
    // Getters/Setters com os nomes corrigidos
    public Double getQtd() { return qtd; }
    public void setQtd(Double qtd) { this.qtd = qtd; }
    public String getValorUnit() { return valorUnit; }
    public void setValorUnit(String valorUnit) { this.valorUnit = valorUnit; }
    public String getItemValorTotal() { return valorTotal; }
    public void setItemValorTotal(String valorTotal) { this.valorTotal = valorTotal; }
}
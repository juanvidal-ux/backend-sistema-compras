package br.com.seuprojeto.sistemacompras.model;

import java.math.BigDecimal;

public class AbcResultDTO {

    private String descricaoItem;
    private String areaItem;      // <--- Este campo é novo
    private BigDecimal valorTotal;

    // --- CONSTRUTOR CRÍTICO (O ERRO ESTÁ AQUI) ---
    // O Hibernate precisa EXATAMENTE deste construtor com 3 argumentos
    // para a query: SELECT new AbcResultDTO(descricao, area, valor)
    public AbcResultDTO(String descricaoItem, String areaItem, BigDecimal valorTotal) {
        this.descricaoItem = descricaoItem;
        this.areaItem = areaItem;
        this.valorTotal = valorTotal;
    }

    // Construtor antigo (opcional, mas bom manter para compatibilidade)
    public AbcResultDTO(String descricaoItem, BigDecimal valorTotal) {
        this.descricaoItem = descricaoItem;
        this.valorTotal = valorTotal;
    }

    // Getters e Setters
    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public String getAreaItem() {
        return areaItem;
    }

    public void setAreaItem(String areaItem) {
        this.areaItem = areaItem;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
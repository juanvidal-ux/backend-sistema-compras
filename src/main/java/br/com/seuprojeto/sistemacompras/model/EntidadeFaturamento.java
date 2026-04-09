package br.com.seuprojeto.sistemacompras.model;

import jakarta.persistence.*;

@Entity
@Table(name = "entidades_faturamento")
public class EntidadeFaturamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(nullable = false)
    private String cnpj;

    @Lob
    @Column(name = "endereco_completo", columnDefinition="TEXT")
    private String enderecoCompleto;

    @Column(name = "inscricao_estadual")
    private String inscricaoEstadual;

    @Column(name = "numero_cr")
    private String numeroCr;

    // --- GETTERS E SETTERS MANUAIS ---

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNomeFantasia() {
        return nomeFantasia;
    }
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }
    public String getRazaoSocial() {
        return razaoSocial;
    }
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }
    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }
    public String getNumeroCr() {
        return numeroCr;
    }
    public void setNumeroCr(String numeroCr) {
        this.numeroCr = numeroCr;
    }
}
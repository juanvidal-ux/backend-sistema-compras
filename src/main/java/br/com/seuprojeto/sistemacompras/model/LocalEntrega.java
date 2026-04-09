package br.com.seuprojeto.sistemacompras.model;

import jakarta.persistence.*;

@Entity
@Table(name = "locais_entrega")
public class LocalEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "header_instituicao")
    private String headerInstituicao;

    private String departamento;
    private String laboratorio;

    @Lob
    @Column(nullable = false, columnDefinition="TEXT")
    private String endereco;

    @Column(name = "contato_responsavel")
    private String contatoResponsavel;

    @Column(name = "contato_cargo")
    private String contatoCargo;

    @Column(name = "header_contato")
    private String headerContato;

    // --- GETTERS E SETTERS MANUAIS ---

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getHeaderInstituicao() {
        return headerInstituicao;
    }
    public void setHeaderInstituicao(String headerInstituicao) {
        this.headerInstituicao = headerInstituicao;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public String getLaboratorio() {
        return laboratorio;
    }
    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public String getContatoResponsavel() {
        return contatoResponsavel;
    }
    public void setContatoResponsavel(String contatoResponsavel) {
        this.contatoResponsavel = contatoResponsavel;
    }
    public String getContatoCargo() {
        return contatoCargo;
    }
    public void setContatoCargo(String contatoCargo) {
        this.contatoCargo = contatoCargo;
    }
    public String getHeaderContato() {
        return headerContato;
    }
    public void setHeaderContato(String headerContato) {
        this.headerContato = headerContato;
    }
}
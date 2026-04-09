package br.com.seuprojeto.sistemacompras.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha; // Será salva criptografada

    private String papel; // EX: "ADMIN", "USER"

    // Construtores
    public Usuario() {}
    
    public Usuario(String login, String senha, String papel) {
        this.login = login;
        this.senha = senha;
        this.papel = papel;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }
}
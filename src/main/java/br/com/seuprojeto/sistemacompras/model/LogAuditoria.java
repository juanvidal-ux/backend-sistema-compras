package br.com.seuprojeto.sistemacompras.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;
    private String usuario; // Quem fez?
    private String acao;    // O que fez? (CRIAR_PEDIDO, EXCLUIR_USER)
    
    @Column(columnDefinition = "TEXT")
    private String detalhe; // Detalhes (ID 50, Valor R$ 1000)

    // Construtor padrão
    public LogAuditoria() {
        this.dataHora = LocalDateTime.now();
    }

    // Construtor prático
    public LogAuditoria(String usuario, String acao, String detalhe) {
        this.dataHora = LocalDateTime.now();
        this.usuario = usuario;
        this.acao = acao;
        this.detalhe = detalhe;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getUsuario() { return usuario; }
    public String getAcao() { return acao; }
    public String getDetalhe() { return detalhe; }
}
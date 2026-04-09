package br.com.seuprojeto.sistemacompras.model;

public class MigracaoResponseDTO {

    private String mensagem;
    private int linhasProcessadas;
    private int pedidosCriados;
    private int itensInseridos;
    private int fornecedoresCriados;
    private int entidadesCriadas;

    // Construtor
    public MigracaoResponseDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    // Getters e Setters
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public int getLinhasProcessadas() { return linhasProcessadas; }
    public void setLinhasProcessadas(int linhasProcessadas) { this.linhasProcessadas = linhasProcessadas; }
    public int getPedidosCriados() { return pedidosCriados; }
    public void setPedidosCriados(int pedidosCriados) { this.pedidosCriados = pedidosCriados; }
    public int getItensInseridos() { return itensInseridos; }
    public void setItensInseridos(int itensInseridos) { this.itensInseridos = itensInseridos; }
    public int getFornecedoresCriados() { return fornecedoresCriados; }
    public void setFornecedoresCriados(int fornecedoresCriados) { this.fornecedoresCriados = fornecedoresCriados; }
    public int getEntidadesCriadas() { return entidadesCriadas; }
    public void setEntidadesCriadas(int entidadesCriadas) { this.entidadesCriadas = entidadesCriadas; }

    // Métodos "increment"
    public void incrementLinhas() { this.linhasProcessadas++; }
    public void incrementPedidos() { this.pedidosCriados++; }
    public void incrementItens() { this.itensInseridos++; }
    public void incrementFornecedores() { this.fornecedoresCriados++; }
    public void incrementEntidades() { this.entidadesCriadas++; }
}
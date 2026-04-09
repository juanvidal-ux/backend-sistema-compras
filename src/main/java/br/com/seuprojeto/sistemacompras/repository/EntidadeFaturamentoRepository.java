package br.com.seuprojeto.sistemacompras.repository;

import br.com.seuprojeto.sistemacompras.model.EntidadeFaturamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntidadeFaturamentoRepository extends JpaRepository<EntidadeFaturamento, Integer> {

    // ADICIONE ESTA LINHA:
    EntidadeFaturamento findByNomeFantasia(String nomeFantasia);
}
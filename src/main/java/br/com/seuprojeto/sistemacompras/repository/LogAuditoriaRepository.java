package br.com.seuprojeto.sistemacompras.repository;

import br.com.seuprojeto.sistemacompras.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
    // Busca os últimos logs primeiro
    List<LogAuditoria> findAllByOrderByDataHoraDesc();
}
package br.com.seuprojeto.sistemacompras.service;

import br.com.seuprojeto.sistemacompras.model.LogAuditoria;
import br.com.seuprojeto.sistemacompras.repository.LogAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    @Autowired
    private LogAuditoriaRepository repository;

    public void registrar(String acao, String detalhe) {
        // Pega o usuário logado automaticamente
        String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
        
        LogAuditoria log = new LogAuditoria(usuario, acao, detalhe);
        repository.save(log);
    }
}
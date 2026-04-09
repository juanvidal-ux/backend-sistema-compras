package br.com.seuprojeto.sistemacompras.controller;

import br.com.seuprojeto.sistemacompras.model.LogAuditoria;
import br.com.seuprojeto.sistemacompras.repository.LogAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private LogAuditoriaRepository logRepository;

    // Endpoint que a tela auditoria.html chama
    @GetMapping
    public List<LogAuditoria> listarLogs() {
        // Retorna do mais recente para o mais antigo
        return logRepository.findAllByOrderByDataHoraDesc();
    }
}
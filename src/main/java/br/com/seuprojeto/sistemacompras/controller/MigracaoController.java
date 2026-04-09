package br.com.seuprojeto.sistemacompras.controller;

import br.com.seuprojeto.sistemacompras.model.MigracaoRequestDTO;
import br.com.seuprojeto.sistemacompras.model.MigracaoResponseDTO;
import br.com.seuprojeto.sistemacompras.model.MigracaoValidationException;
import br.com.seuprojeto.sistemacompras.service.MigracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod; // <-- ADICIONAR IMPORT

import java.util.List;
import java.util.Map;

// --- CORREÇÃO V7.0: ADICIONAR "methods = ..." ---
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}, 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/migracao")
public class MigracaoController {

    @Autowired
    private MigracaoService migracaoService;

    @PostMapping("/validar-v6")
    public ResponseEntity<MigracaoResponseDTO> validarPlanilhaV6(@RequestBody List<MigracaoRequestDTO> payload) {
        try {
            MigracaoResponseDTO resposta = migracaoService.validarMigracao(payload);
            return ResponseEntity.ok(resposta);
        } catch (MigracaoValidationException e) {
            return ResponseEntity.badRequest().body(new MigracaoResponseDTO(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MigracaoResponseDTO(e.getMessage()));
        }
    }
    
    @PostMapping("/executar-v6")
    public ResponseEntity<MigracaoResponseDTO> executarPlanilhaV6(@RequestBody List<MigracaoRequestDTO> payload) {
        try {
            MigracaoResponseDTO resposta = migracaoService.processarMigracao(payload);
            return ResponseEntity.ok(resposta);
        } catch (MigracaoValidationException e) {
            return ResponseEntity.badRequest().body(new MigracaoResponseDTO(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MigracaoResponseDTO(e.getMessage()));
        }
    }
    
    @DeleteMapping("/excluir-migrados")
    public ResponseEntity<?> excluirDadosMigrados() {
        try {
            long count = migracaoService.excluirDadosMigrados();
            return ResponseEntity.ok(Map.of("mensagem", count + " pedidos migrados (e seus itens) foram excluídos."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("mensagem", "Erro ao excluir dados: " + e.getMessage()));
        }
    }
}
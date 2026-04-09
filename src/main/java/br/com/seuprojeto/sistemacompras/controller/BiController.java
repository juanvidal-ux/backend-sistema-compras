package br.com.seuprojeto.sistemacompras.controller;

import br.com.seuprojeto.sistemacompras.model.AbcResultDTO;
import br.com.seuprojeto.sistemacompras.service.BiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bi")
// --- CORREÇÃO DE SEGURANÇA AQUI ---
// Trocamos "*" por endereços específicos. Isso resolve o erro de "allowCredentials".
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"}, allowedHeaders = "*")
public class BiController {

    @Autowired
    private BiService biService;

    // Endpoint Unificado Inteligente
    @GetMapping("/curva-abc-unificada")
    public List<AbcResultDTO> getCurvaAbc(
            @RequestParam(required = false) String area, 
            @RequestParam(required = false) String grupo, // Adicionado caso precise
            @RequestParam(required = false) String ano) {
        
        // Prioridade: Se tem Área, usa a lógica inteligente da Área
        if (area != null && !area.isEmpty()) {
            return biService.getCurvaAbcInteligente(area, ano);
        }
        
        // Se vier Grupo (caso use o filtro antigo), cai aqui
        if (grupo != null && !grupo.isEmpty()) {
            return biService.getCurvaAbcInteligente(grupo, ano);
        }
        
        return List.of();
    }
    
    // Endpoint Financeiro (Mantido para compatibilidade)
    @GetMapping("/financeiro-mensal")
    public ResponseEntity<Map<Integer, BigDecimal>> getFinanceiroMensal(
            @RequestParam(defaultValue = "2024") Integer ano,
            @RequestParam(defaultValue = "todos") String setor) { 
        return ResponseEntity.ok(biService.getResumoFinanceiro(ano, setor));
    }
}
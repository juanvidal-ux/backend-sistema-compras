package br.com.seuprojeto.sistemacompras.controller;

import br.com.seuprojeto.sistemacompras.model.EntidadeFaturamento;
import br.com.seuprojeto.sistemacompras.model.Fornecedor;
import br.com.seuprojeto.sistemacompras.model.LocalEntrega;
import br.com.seuprojeto.sistemacompras.repository.EntidadeFaturamentoRepository;
import br.com.seuprojeto.sistemacompras.repository.FornecedorRepository;
import br.com.seuprojeto.sistemacompras.repository.LocalEntregaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException; // IMPORTAR ISTO
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suporte") 
public class DadosSuporteController {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private EntidadeFaturamentoRepository entidadeFaturamentoRepository;

    @Autowired
    private LocalEntregaRepository localEntregaRepository;

    // ----- ENDPOINTS 'GET' -----

    @GetMapping("/fornecedores")
    public ResponseEntity<List<Fornecedor>> getFornecedores() {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/entidades")
    public ResponseEntity<List<EntidadeFaturamento>> getEntidadesFaturamento() {
        List<EntidadeFaturamento> entidades = entidadeFaturamentoRepository.findAll();
        return ResponseEntity.ok(entidades);
    }

    @GetMapping("/locais-entrega")
    public ResponseEntity<List<LocalEntrega>> getLocaisEntrega() {
        List<LocalEntrega> locais = localEntregaRepository.findAll();
        return ResponseEntity.ok(locais);
    }

    // ----- ENDPOINTS 'POST' -----

    @PostMapping("/fornecedores")
    public ResponseEntity<Fornecedor> criarFornecedor(@RequestBody Fornecedor fornecedor) {
        try {
            Fornecedor novoFornecedor = fornecedorRepository.save(fornecedor);
            return new ResponseEntity<>(novoFornecedor, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); 
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/entidades")
    public ResponseEntity<EntidadeFaturamento> criarEntidade(@RequestBody EntidadeFaturamento entidade) {
        try {
            EntidadeFaturamento novaEntidade = entidadeFaturamentoRepository.save(entidade);
            return new ResponseEntity<>(novaEntidade, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     @PostMapping("/locais-entrega")
    public ResponseEntity<LocalEntrega> criarLocalEntrega(@RequestBody LocalEntrega local) {
        try {
            LocalEntrega novoLocal = localEntregaRepository.save(local);
            return new ResponseEntity<>(novoLocal, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ----- NOVOS ENDPOINTS 'DELETE' -----

    @DeleteMapping("/fornecedores/{id}")
    public ResponseEntity<Void> excluirFornecedor(@PathVariable Integer id) {
        try {
            fornecedorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/entidades/{id}")
    public ResponseEntity<Void> excluirEntidade(@PathVariable Integer id) {
        try {
            entidadeFaturamentoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/locais-entrega/{id}")
    public ResponseEntity<Void> excluirLocalEntrega(@PathVariable Integer id) {
        try {
            localEntregaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
package br.com.seuprojeto.sistemacompras.controller;

import br.com.seuprojeto.sistemacompras.model.Usuario;
import br.com.seuprojeto.sistemacompras.repository.UsuarioRepository;
import br.com.seuprojeto.sistemacompras.service.AuditoriaService; // <--- Importar Auditoria
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditoriaService auditoriaService; // <--- INJEÇÃO DA AUDITORIA

    // Endpoint para o Frontend descobrir quem está logado
    @GetMapping("/me")
    public ResponseEntity<?> quemSouEu() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return repository.findByLogin(auth.getName())
                .map(u -> ResponseEntity.ok(new UsuarioDTO(u.getId(), u.getLogin(), u.getPapel())))
                .orElse(ResponseEntity.notFound().build());
    }

    // Cadastrar Novo Usuário (Auditado)
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        if (repository.findByLogin(usuario.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().body("Erro: Usuário já existe!");
        }

        // Criptografa a senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        if (usuario.getPapel() == null || usuario.getPapel().isEmpty()) {
            usuario.setPapel("USER");
        }
        
        repository.save(usuario);

        // --- LOG DE AUDITORIA ---
        auditoriaService.registrar("CRIAR_USUARIO", "Novo Usuário: " + usuario.getLogin() + " (" + usuario.getPapel() + ")");

        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }
    
    // Listar Usuários
    @GetMapping("/users")
    public List<Usuario> listar() {
        return repository.findAll();
    }

    // Excluir Usuário (Auditado)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        Optional<Usuario> usuarioAlvo = repository.findById(id);

        if (usuarioAlvo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Guarda o nome antes de apagar para gravar no log
        String loginRemovido = usuarioAlvo.get().getLogin();

        repository.deleteById(id);

        // --- LOG DE AUDITORIA ---
        auditoriaService.registrar("EXCLUIR_USUARIO", "Removeu: " + loginRemovido);

        return ResponseEntity.ok().build();
    }

    // DTO para não devolver a senha
    record UsuarioDTO(Long id, String login, String papel) {}
}
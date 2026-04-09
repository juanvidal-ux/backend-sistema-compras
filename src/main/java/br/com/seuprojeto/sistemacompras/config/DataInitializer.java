package br.com.seuprojeto.sistemacompras.config;

import br.com.seuprojeto.sistemacompras.model.Usuario;
import br.com.seuprojeto.sistemacompras.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe algum usuário
        if (repository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("admin123")); // Criptografa!
            admin.setPapel("ADMIN");
            
            repository.save(admin);
            System.out.println("--- USUÁRIO ADMIN CRIADO AUTOMATICAMENTE ---");
        }
    }
}
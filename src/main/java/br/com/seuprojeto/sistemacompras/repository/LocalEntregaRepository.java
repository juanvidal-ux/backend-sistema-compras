package br.com.seuprojeto.sistemacompras.repository;
import br.com.seuprojeto.sistemacompras.model.LocalEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalEntregaRepository extends JpaRepository<LocalEntrega, Integer> {
}

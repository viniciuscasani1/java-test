package org.example.repository;

import org.example.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCpf(String cpf);
}

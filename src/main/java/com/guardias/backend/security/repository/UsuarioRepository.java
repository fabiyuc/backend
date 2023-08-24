package com.guardias.backend.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.security.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByNombreUsuarioOrEmail(String nombreUsuario, String email);

    Optional<Usuario> findByPassword(String password);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByEmail(String email);

}

package com.guardias.backend.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    /**
     * Busca un usuario por atributo usuario
     * @param usuario
     * @return
     */
    Usuario findByUsuario(String usuario);

    // Usuario getById(Long id);

    // Usuario findByIdUsuario(Long id);

    // Usuario findById_usuario(Long id_usuario);

    // public int loginValidation(String usuario, String contrasenia);

}

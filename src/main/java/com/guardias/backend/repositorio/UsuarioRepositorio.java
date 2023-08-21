package com.guardias.backend.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.Usuario;
import com.guardias.backend.user.User;

@Repository
public interface UsuarioRepositorio extends JpaRepository<User,Integer> {
//Usuario, Long> {

    Optional<User> findByUsername(String username);
    /**
     * Busca un usuario por atributo usuario
     * @param usuario
     * @return
     */
    Usuario findByUsuario(String usuario);

    // Usuario getById(Long id);

     Usuario findByIdUsuario(Long id);

    // Usuario findById_usuario(Long id_usuario);

    // public int loginValidation(String usuario, String contrasenia);

}

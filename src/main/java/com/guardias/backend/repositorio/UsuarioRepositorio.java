 package com.guardias.backend.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.Usuarios;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuarios, Long> {
  
}
 
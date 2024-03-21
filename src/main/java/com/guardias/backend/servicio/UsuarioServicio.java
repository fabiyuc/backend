
  package com.guardias.backend.servicio;
  
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.modelo.Usuarios;
import com.guardias.backend.repositorio.UsuarioRepositorio;

import jakarta.transaction.Transactional;
  
  @Service
  @Transactional
  public class UsuarioServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    
    public List<Usuarios> findAll() {
        return usuarioRepositorio.findAll();
    }
 

 }
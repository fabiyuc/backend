/* package com.guardias.backend.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.modelo.Usuario;
import com.guardias.backend.repositorio.UsuarioRepositorio;

@RestController
@RequestMapping("/api/V2")
public class UsuarioControlador {

    @Autowired
    private UsuarioRepositorio repositorio;

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return repositorio.findAll();
    }

}
 */
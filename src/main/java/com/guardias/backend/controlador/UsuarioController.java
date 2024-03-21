package com.guardias.backend.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.modelo.Usuarios;
import com.guardias.backend.servicio.UsuarioServicio;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")

public class UsuarioController {

    @Autowired
    UsuarioServicio usuarioServicio;

  
    @GetMapping("/list")
    public ResponseEntity<List<Usuarios>> list() {
        List<Usuarios> list = usuarioServicio.findAll();
        return new ResponseEntity<List<Usuarios>>(list, HttpStatus.OK);
    }
   
}
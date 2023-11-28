package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.modelo.RegistroActividad;
import com.guardias.backend.repositorio.RegistroActividadRepositorio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistroActividadServicio {
    
    @Autowired
    RegistroActividadRepositorio registroActividadRepositorio;

    public List<RegistroActividad> list(){
        return registroActividadRepositorio.findAll();
    }

    public Optional<RegistroActividad> getOne(Long id){
        return registroActividadRepositorio.findById(id);
    }

     public void save(RegistroActividad registroActividad) {
        registroActividadRepositorio.save(registroActividad);
    }

    public void delete(Long id) {
        registroActividadRepositorio.deleteById(id);
    }

    public boolean existsById(Long id) {
        return registroActividadRepositorio.existsById(id);
    }
}

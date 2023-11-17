package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Pais;
import com.guardias.backend.repository.PaisRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaisService {

    @Autowired
    PaisRepository paisRepository;

    public List<Pais> list() {
        return paisRepository.findAll();
    }

    public Optional<Pais> getOne(int id) {
        return paisRepository.findById(id);
    }

    public void save(Pais pais) {
        paisRepository.save(pais);
    }

    public void delete(int id) {
        paisRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return paisRepository.existsById(id);
    }

    public Optional<Pais> getById(int id) {
        return paisRepository.findById(id);
    }

    public boolean existsByNombre(String nombre) {
        return paisRepository.existsByNombre(nombre);
    }

     public Optional<Pais> getByNombre(String nombre) {
        return paisRepository.findByNombre(nombre);
    }

    

    
   

   

    

    

    

   /*  public boolean existsById(int id) {
        return false;
    } */

    

    /* public Object getByNombre(String nombre) {
        return null;
    } */

}

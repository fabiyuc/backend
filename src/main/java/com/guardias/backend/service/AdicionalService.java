package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Adicional;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.repository.AdicionalRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class AdicionalService {

    @Autowired
    AdicionalRepository adicionalRepository;

    @Autowired
    RevistaService revistaService;

    public List<Adicional> findByActivo(boolean activo) {
        return adicionalRepository.findByActivo(activo);
    }

    public List<Adicional> findAll() {
        return adicionalRepository.findAll();
    }

    public Optional<Adicional> findById(Long id) {
        return adicionalRepository.findById((Long) id);
    }

    public Optional<Adicional> getByNombre(String nombre) {
        return adicionalRepository.findByNombre(nombre);
    }

    public void save(Adicional adicional) {
        adicionalRepository.save(adicional);
    }

    public void deleteById(Long id) {
        adicionalRepository.deleteById((Long) id);
    }

    public boolean existsById(Long id) {
        return adicionalRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return adicionalRepository.existsByNombre(nombre);
    }

    public boolean activo(Long id) {
        return (adicionalRepository.existsById(id) && adicionalRepository.findById(id).get().isActivo());
    }

    public void agregarRevista(Long adicionalId, Long idRevista) {
        Adicional adicional = adicionalRepository.findById(adicionalId)
                .orElseThrow(
                        () -> new EntityNotFoundException("No se encontró el adicional con el ID: " + adicionalId));

        Revista revista = revistaService.findById(idRevista).get();
        revista.setAdicional(adicional);
        revistaService.save(revista);

        adicional.getRevistas().add(revista);

        adicionalRepository.save(adicional);
    }

}

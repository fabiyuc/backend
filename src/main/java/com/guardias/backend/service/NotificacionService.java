package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Notificacion;
import com.guardias.backend.repository.NotificacionRepository;

@Service
@Transactional
public class NotificacionService {

    @Autowired
    NotificacionRepository notificacionRepository;

    public List<Notificacion> list() {
        return notificacionRepository.findAll();

    }

    public Optional<Notificacion> getone(Long id) {
        return notificacionRepository.findById((Long) id);
    }

    public Optional<Notificacion> getByNombre(String nombre) {
        return notificacionRepository.findByNombre(nombre);
    }

    public void save(Notificacion notificacion) {
        notificacionRepository.save(notificacion);
    }

    public void delete(Long id) {
        notificacionRepository.deleteById((Long) id);
    }

    public boolean existsById(Long id) {
        return notificacionRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return notificacionRepository.existsByNombre(nombre);
    }

}

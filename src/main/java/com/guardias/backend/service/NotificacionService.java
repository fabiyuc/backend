package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Notificacion;
import com.guardias.backend.enums.TipoNotificacion;
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

    // public Optional<Notificacion> getByTipo(String tipo) {

    public List<Notificacion> getByTipoAndActivo(TipoNotificacion tipo, boolean activo) {
        return notificacionRepository.findByTipoAndActivo(tipo, activo);
    }

    // public List<Notificacion> getByActivo(TipoNotificacion tipo) {
    // return notificacionRepository.findByActivo(true);
    // }

    // public void save(Notificacion notificacion) {
    // notificacionRepository.save(notificacion);
    // }

    public void save(Notificacion notificacion) {
        notificacionRepository.save(notificacion);
    }

    public void delete(Long id) {
        notificacionRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return notificacionRepository.existsById(id);
    }

    // public boolean existsByActivo(boolean activo) {
    // return notificacionRepository.existsByActivo(true);
    // }

}

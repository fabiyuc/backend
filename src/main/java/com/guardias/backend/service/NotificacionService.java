package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Notificacion;
import com.guardias.backend.enums.TipoNotificacionEnum;
import com.guardias.backend.repository.NotificacionRepository;

@Service
@Transactional
public class NotificacionService {

    @Autowired
    NotificacionRepository notificacionRepository;

    // @Autowired
    // EfectorService efectorService;

    public List<Notificacion> findByActivo(boolean activo) {
        return notificacionRepository.findByActivo(activo);

    }

    public List<Notificacion> findAll() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> findById(Long id) {
        return notificacionRepository.findById(id);
    }

    public List<Notificacion> findByTipoAndActivo(TipoNotificacionEnum tipo, boolean activo) {
        return notificacionRepository.findByTipoAndActivo(tipo, activo);
    }

    public void save(Notificacion notificacion) {
        notificacionRepository.save(notificacion);
    }

    public void deleteById(Long id) {
        notificacionRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return notificacionRepository.existsById(id);
    }

    // @Transactional
    // public void agregarEfector(Long idNotificacion, Long idEfector) {

    // Notificacion notificacion = findById(idNotificacion).get();
    // Efector efector = efectorService.findEfector(idEfector);

    // efectorService.agregarAutoridad(idEfector, idNotificacion);
    // notificacion.getEfectores().add(efector);
    // save(notificacion);
    // }

}

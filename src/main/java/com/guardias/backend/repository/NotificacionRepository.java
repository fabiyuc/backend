package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Notificacion;
import com.guardias.backend.enums.TipoNotificacionEnum;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByTipoAndActivo(TipoNotificacionEnum tipo, boolean activo);

    Optional<List<Notificacion>> findByActivoTrue();

    Optional<Notificacion> findById(Long id);

    boolean findByTipoAndActivo(String tipo, boolean activo);

    boolean existsById(Long id);

    List<Notificacion> findByActivo(boolean activo);
}

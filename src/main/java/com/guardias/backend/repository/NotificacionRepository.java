package com.guardias.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Notificacion;
import com.guardias.backend.enums.TipoNotificacionEnum;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    List<Notificacion> findByTipoAndActivo(TipoNotificacionEnum tipo, boolean activo);

    Boolean findByTipoAndActivo(String tipo, boolean activo);

    List<Notificacion> findByActivoTrue();
}

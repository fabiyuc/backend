package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    Optional<Servicio> findByDescripcion(String descripcion);

    List<Servicio> findByNivel(int nivel);

    Boolean existsByDescripcion(String descripcion);

    Boolean existsByNivel(int nivel);

}

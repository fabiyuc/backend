package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    Optional<Servicio> findByDescripcion(String descripcion);

    boolean existsByDescripcion(String descripcion);

}

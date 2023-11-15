package com.guardias.backend.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Suspencion;

@Repository
public interface SuspencionRepository extends JpaRepository<Suspencion, Long> {

    Optional<Suspencion> findById(int id);

    boolean existsById(int id);

    Optional<Suspencion> findByFechaInicio(Date fechaInicio);

    boolean existsByFechaInicio(Date fechaInicio);

    Optional<Suspencion> findByFechaFin(Date fechaFin);

    boolean existsByFechaFin(Date fechaFin);

}

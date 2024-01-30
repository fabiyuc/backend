package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.NovedadPersonal;

@Repository
public interface NovedadPersonalRepository extends JpaRepository<NovedadPersonal, Long> {

    // Optional<List<NovedadPersonal>> findByPersona(Long idPersona);

    Optional<List<NovedadPersonal>> findByFechaInicio(LocalDate fechaInicio);

    // boolean existsByPersona(Long idPersona);

    Boolean existsByFechaInicio(LocalDate fechaInicio);
}

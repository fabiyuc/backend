package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.DistribucionConsultorio;

@Repository
public interface DistribucionConsultorioRepository extends JpaRepository<DistribucionConsultorio, Long> {

    Optional<DistribucionConsultorio> findById(Long id);

    List<DistribucionConsultorio> findByFechaInicio(LocalDate fechaInicio);

    @Query("SELECT dc FROM distribucionesConsultorios dc WHERE dc.persona.id = :personaId")
    Optional<List<DistribucionConsultorio>> findByPersonaId(@Param("personaId") Long personaId);

    @Query("SELECT dc FROM distribucionesConsultorios dc WHERE dc.efector.id = :efectorId")
    Optional<List<DistribucionConsultorio>> findByEfectorId(@Param("efectorId") Long efectorId);

    boolean existsById(Long id);

    boolean existsByEfectorId(Long efectorId);

    boolean existsByPersonaId(Long personaId);
}

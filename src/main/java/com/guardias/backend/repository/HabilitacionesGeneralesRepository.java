package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.guardias.backend.entity.HabilitacionesGenerales;

public interface HabilitacionesGeneralesRepository extends JpaRepository<HabilitacionesGenerales, Long>{
    
    Optional<List<HabilitacionesGenerales>> findByActivoTrue();

    boolean existsById(Long id);

    boolean existsByPersonaId(Long personaId);

    Optional<HabilitacionesGenerales> findByPersonaId(Long personaId);

    Optional<HabilitacionesGenerales> findByPersonaIdAndActivoTrue(Long personaId);

    @Query("SELECT p FROM habilitacionesGenerales p JOIN p.efectores e WHERE TYPE(p.persona) = asistenciales AND e.id = :idEfector")
    List<HabilitacionesGenerales> findHabilitacionesGeneralesByEfectorAndAsistencial(@Param("idEfector") Long idEfector);
}

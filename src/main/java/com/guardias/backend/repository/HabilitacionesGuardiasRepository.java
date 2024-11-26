package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.guardias.backend.entity.HabilitacionesGuardias;

public interface HabilitacionesGuardiasRepository extends JpaRepository<HabilitacionesGuardias, Long> {

    Optional<List<HabilitacionesGuardias>> findByActivoTrue();

    boolean existsById(Long id);

    boolean existsByPersonaId(Long personaId);

    Optional<HabilitacionesGuardias> findByPersonaId(Long personaId);

    Optional<HabilitacionesGuardias> findByPersonaIdAndActivoTrue(Long personaId);

    @Query("SELECT p FROM habilitacionesGuardias p JOIN p.efectores e WHERE TYPE(p.persona) = asistenciales AND e.id = :idEfector")
    List<HabilitacionesGuardias> findHabilitacionesGuardiasByEfectorAndAsistencial(@Param("idEfector") Long idEfector);


}

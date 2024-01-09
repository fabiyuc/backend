package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.DistribucionHoraria;

@Repository
public interface DistribucionHorariaRepository extends JpaRepository<DistribucionHoraria, Long> {
    Optional<DistribucionHoraria> findById(Long id);

    Optional<List<DistribucionHoraria>> findByFecha(LocalDate fecha);
    /*
     * @Query("SELECT dh FROM DistribucionHoraria dh WHERE dh.persona.id = :personaId"
     * )
     * Optional<List<DistribucionHoraria>> findByPersonaId(@Param("personaId") Long
     * personaId);
     * 
     * @Query("SELECT dh FROM DistribucionHoraria dh WHERE dh.efector.id = :efectorId"
     * )
     * Optional<List<DistribucionHoraria>> findByEfectorId(@Param("efectorId") Long
     * efectorId);
     */

    boolean existsById(Long id);

    boolean existsByEfectorId(Long efectorId);

    boolean existsByPersonaId(Long personaId);
}

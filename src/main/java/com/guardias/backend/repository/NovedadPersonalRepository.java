package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.NovedadPersonal;

@Repository
public interface NovedadPersonalRepository extends JpaRepository<NovedadPersonal, Long> {

    @Query("SELECT np FROM novedadesPersonales np WHERE np.persona.id = :personaId")
    Optional<List<NovedadPersonal>> findByPersona(@Param("personaId") Long personaId);

    Optional<List<NovedadPersonal>> findByFechaInicio(LocalDate fechaInicio);

    boolean existsByFechaInicio(LocalDate fechaInicio);
}

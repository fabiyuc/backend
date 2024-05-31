package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.PendientesSemanal;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface PendientesSemanalRepository extends JpaRepository<PendientesSemanal, Long> {

    List<PendientesSemanal> findByActivoTrue();

    List<PendientesSemanal> findByAnioAndMesAndEfectorId(int anio, MesesEnum mes, Long idEfector);

    Optional<PendientesSemanal> findByEfectorAndFechaInicio(Efector efector, LocalDate fechaInicio);
}

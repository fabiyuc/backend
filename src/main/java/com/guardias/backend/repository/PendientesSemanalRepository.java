package com.guardias.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.PendientesSemanal;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface PendientesSemanalRepository extends JpaRepository<PendientesSemanal, Long> {

    List<PendientesSemanal> findByActivoTrue();

    List<PendientesSemanal> findByAnioAndMesAndEfectorId(int anio, MesesEnum mes, Long idEfector);
}

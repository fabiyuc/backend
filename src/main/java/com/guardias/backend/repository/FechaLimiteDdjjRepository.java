package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.FechaLimiteDdjj;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface FechaLimiteDdjjRepository extends JpaRepository<FechaLimiteDdjj, Long> {

    List<FechaLimiteDdjj> findByActivoTrue();

    Optional<FechaLimiteDdjj> findFirstByMesAndAnioAndTipoGuardia_IdAndActivoTrue(MesesEnum mes, int anio,
            Long idTipoGuardia);

    List<FechaLimiteDdjj> findByMesAndAnioAndTipoGuardia_IdOrderByFechaHoraCreacionDesc(MesesEnum mes, int anio,
            Long idTipoGuardia);

    List<FechaLimiteDdjj> findAllByOrderByFechaHoraCreacionDesc();
}

package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface RegistroMensualRepository extends JpaRepository<RegistroMensual, Long> {

        Optional<RegistroMensual> findById(Long id);

        List<RegistroMensual> findByAnioAndMesAndEfectorId(int anio, MesesEnum mes, Long idEfector);

        Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId,
                        MesesEnum mes, int anio);

        List<RegistroMensual> findByActivoTrue();

        boolean existsByAnioAndMes(int anio, MesesEnum mes);

        boolean existsByAsistencialId(Long asistencialId);

        List<RegistroMensual> findByAnioAndMes(int anio, MesesEnum mes);
}

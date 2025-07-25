package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface CronogramaDefinitivoRepository extends JpaRepository <CronogramaDefinitivo, Long> {

    Optional<CronogramaDefinitivo> findById(Long id);

        @Query("SELECT DISTINCT cd FROM cronogramasDefinitivos cd JOIN cd.registroActividad ra WHERE cd.anio = :anio AND cd.mes = :mes AND cd.efector.id = :idEfector")
        List<CronogramaDefinitivo> findByAnioMesEfector(@Param("anio") int anio,
                        @Param("mes") MesesEnum mes, @Param("idEfector") Long idEfector);

        Optional<List<CronogramaDefinitivo>> findByActivoTrue();

        boolean existsByAnioAndMes(int anio, MesesEnum mes);

        boolean existsByAsistencialId(Long asistencialId);

        List<CronogramaDefinitivo> findByAnioAndMes(int anio, MesesEnum mes);

        boolean existsById(Long id);

        List<CronogramaDefinitivo> findByActivo(boolean activo);
    
}

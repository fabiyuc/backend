package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface RegistroMensualRepository extends JpaRepository<RegistroMensual, Long> {

        Optional<RegistroMensual> findById(Long id);

        @Query("SELECT DISTINCT rm FROM registrosMensuales rm JOIN rm.registroActividad ra WHERE rm.anio = :anio AND rm.mes = :mes AND rm.efector.id = :idEfector")
        List<RegistroMensual> findByAnioMesEfector(
                @Param("anio") int anio,
                @Param("mes") MesesEnum mes, 
                @Param("idEfector") Long idEfector);

        Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId,
                        MesesEnum mes, int anio);

        Optional<List<RegistroMensual>> findByActivoTrue();

        boolean existsByAnioAndMes(int anio, MesesEnum mes);

        boolean existsByAsistencialId(Long asistencialId);

        List<RegistroMensual> findByAnioAndMes(int anio, MesesEnum mes);

        boolean existsById(Long id);

        List<RegistroMensual> findByActivo(boolean activo);

        @Query("SELECT DISTINCT rm FROM registrosMensuales rm JOIN rm.registroActividad ra "
                        + "WHERE rm.anio = :anio AND rm.mes = :mes AND rm.efector.id = :idEfector "
                        + "AND ra.servicio.id = :idServicio")
        List<RegistroMensual> findByAnioMesEfectorAndServicio(
                @Param("anio") int anio,
                @Param("mes") MesesEnum mes,
                @Param("idEfector") Long idEfector,
                @Param("idServicio") Long idServicio);

        @Query("SELECT r.id FROM registrosMensuales r WHERE r.id IN :ids")
        List<Long> findExistingIds(@Param("ids") List<Long> ids);
}

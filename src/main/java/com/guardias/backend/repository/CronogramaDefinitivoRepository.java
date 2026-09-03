package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface CronogramaDefinitivoRepository extends JpaRepository<CronogramaDefinitivo, Long> {

    Optional<CronogramaDefinitivo> findById(Long id);
    
    Optional<CronogramaDefinitivo> findByIdAndActivoTrue(Long id);

    Optional<List<CronogramaDefinitivo>> findByActivoTrue();

    boolean existsByAnioAndMes(int anio, MesesEnum mes);

    List<CronogramaDefinitivo> findByAnioAndMes(int anio, MesesEnum mes);

    boolean existsById(Long id);

    List<CronogramaDefinitivo> findByActivo(boolean activo);

    List<CronogramaDefinitivo> findByAnioAndMesAndEfectorIdAndActivoTrue(int anio, MesesEnum mes, Long idEfector);

    @Query("SELECT DISTINCT c FROM cronogramasDefinitivos c " +
       "JOIN c.registrosActividades r " +
       "WHERE c.anio = :anio " +
       "AND c.mes = :mes " +
       "AND c.efector.id = :idEfector " +
       "AND c.activo = true " +
       "AND r.tipoGuardia.id = :idTipoGuardia")
    List<CronogramaDefinitivo> findByAnioAndMesAndEfectorIdAndActivoTrueAndTipoGuardia(int anio, MesesEnum mes, Long idEfector, Long idTipoGuardia);

   /*  @Query("SELECT DISTINCT c FROM cronogramasDefinitivos c LEFT JOIN FETCH c.ddjjs WHERE c.id = :id")
    Optional<CronogramaDefinitivo> findByIdWithDdjjs(@Param("id") Long id); */

    Optional<CronogramaDefinitivo> findByEfectorIdAndMesAndAnioAndActivoTrue(
        Long efectorId, MesesEnum mes, int anio);

}

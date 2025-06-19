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

       Optional<List<NovedadPersonal>> findByActivoTrue();

       Optional<NovedadPersonal> findById(Long id);

       @Query("SELECT np FROM novedadesPersonales np WHERE np.persona.id = :personaId ")
       Optional<List<NovedadPersonal>> findByPersona(@Param("personaId") Long personaId);

       @Query("SELECT np FROM novedadesPersonales np WHERE np.persona.id = :personaId AND np.activo = true " +
                     "AND ((FUNCTION('MONTH', np.fechaInicio) = :mes AND FUNCTION('YEAR', np.fechaInicio) = :anio) " +
                     "OR (FUNCTION('MONTH', np.fechaFinal) = :mes AND FUNCTION('YEAR', np.fechaFinal) = :anio))")
       Optional<List<NovedadPersonal>> findActiveByPersonaAndDate(@Param("personaId") Long personaId,
                     @Param("mes") int mes,
                     @Param("anio") int anio);

       Optional<List<NovedadPersonal>> findByFechaInicio(LocalDate fechaInicio);

       @Query("SELECT np FROM novedadesPersonales np WHERE np.fechaInicio = :fechaInicio and np.activo=true")
       Boolean existsByFechaInicio(LocalDate fechaInicio);

       boolean existsByPersonaId(Long personaId);

       boolean existsById(Long id);

       List<NovedadPersonal> findByActivo(boolean activo);

       @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END " +
                     "FROM novedadesPersonales n " +
                     "WHERE n.persona.id = :personaId " +
                     "AND n.tipoLicencia.nombre IN :licencias " +
                     "AND n.activo = true " +
                     "AND :fecha BETWEEN n.fechaInicio AND n.fechaFinal")
       boolean existeNovedadBloqueante(
                     @Param("personaId") Long personaId,
                     @Param("licencias") List<String> licencias,
                     @Param("fecha") LocalDate fecha);

       @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END " +
                     "FROM novedadesPersonales n " +
                     "WHERE n.persona.id = :personaId " +
                     "AND n.tipoLicencia.nombre = :nombreLicencia " +
                     "AND n.activo = true " +
                     "AND :fecha BETWEEN n.fechaInicio AND n.fechaFinal")
       boolean tieneLicenciaActivaEnFecha(
                     @Param("personaId") Long personaId,
                     @Param("nombreLicencia") String nombreLicencia,
                     @Param("fecha") LocalDate fecha);

       @Query("SELECT n FROM novedadesPersonales n " +
                     "WHERE n.persona.id = :personaId " +
                     "AND UPPER(n.tipoLicencia.nombre) = UPPER(:nombreLicencia) " +
                     "AND n.activo = true")
       List<NovedadPersonal> findByPersonaIdAndTipoLicenciaNombreIgnoreCaseAndActivoTrue(
                     @Param("personaId") Long personaId,
                     @Param("nombreLicencia") String nombreLicencia);
}

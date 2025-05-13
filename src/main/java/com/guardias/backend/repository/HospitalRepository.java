package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.dto.servicio.ServicioSummaryDto;
import com.guardias.backend.entity.Hospital;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByNombre(String nombre);

    Optional<List<Hospital>> findByActivoTrue();

    Optional<Hospital> findById(Long id);

    @Query("SELECT h FROM Hospital h WHERE h.admitePasiva = true AND activo = true")
    List<Hospital> findByAdmitePasiva();

    boolean existsById(Long id);

    boolean existsByIdAndActivoTrue(Long idHospital);

    boolean existsByNombre(String nombre);

    List<Hospital> findByActivo(boolean activo);

    @Query("SELECT h FROM Hospital h WHERE h.nivelComplejidad = ?1")
    List<Hospital> findHospitalesPorNivel(int nivelComplejidad);

    @Query("SELECT h FROM Hospital h WHERE h.nivelComplejidad = ?1 AND h.nombre <> ?2")
    List<Hospital> findHospitalesPorNivelExcluyendo(int nivelComplejidad, String nombreAExcluir);

    Optional<Hospital> findByIdAndActivoTrue(Long id);

    @Query("SELECT h.id FROM Hospital h WHERE h.id IN :ids")
    List<Long> findValidIds(List<Long> ids);

    @Query("SELECT new com.guardias.backend.dto.servicio.ServicioSummaryDto(s.id, s.descripcion) " +
            "FROM Hospital h JOIN h.servicios s " +
            "WHERE h.id = :idHospital AND s.activo = true")
    List<ServicioSummaryDto> findActiveServiciosByHospitalId(@Param("idHospital") Long idHospital);

}

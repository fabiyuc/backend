package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Hospital;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByNombre(String nombre);

    Optional<List<Hospital>> findByActivoTrue();

    Optional<Hospital> findById(Long id);

    @Query("SELECT h FROM Hospital h WHERE h.admitePasiva = true AND activo = true")
    List<Hospital> findByAdmitePasiva();

    boolean existsById(Long id);

    boolean existsByNombre(String nombre);

    List<Hospital> findByActivo(boolean activo);

    @Query("SELECT h.nombre FROM Hospital h WHERE h.nivelComplejidad = ?1")
    List<String> findHospitalesPorNivel(int nivelComplejidad);

    @Query("SELECT h.nombre FROM Hospital h WHERE h.nivelComplejidad = ?1 AND h.nombre <> ?2")
    List<String> findHospitalesPorNivelExcluyendo(int nivelComplejidad, String nombreAExcluir);

    Optional<Hospital> findByIdAndActivoTrue(Long id);
}

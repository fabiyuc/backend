package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Asistencial;

@Repository
public interface AsistencialRepository extends JpaRepository<Asistencial, Long> {

    Optional<List<Asistencial>> findByActivoTrue();

    Optional<Asistencial> findById(Long id);

    Optional<Asistencial> findByDni(int dni);

    Optional<Asistencial> findByCuil(String cuil);

    boolean existsById(Long id);

    boolean existsByDni(int dni);

    boolean existsByCuil(String cuil);

    List<Asistencial> findByActivo(boolean activo);

    @Query("SELECT a FROM asistenciales a JOIN a.legajos l JOIN l.udo u WHERE u.id = :idUdo AND u.activo = true AND a.activo = true")
    List<Asistencial> findByUdoAndActivoTrue(@Param("idUdo") Long idUdo);
}

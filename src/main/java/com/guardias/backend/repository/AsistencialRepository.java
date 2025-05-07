package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.enums.TipoGuardiaEnum;

@Repository
public interface AsistencialRepository extends JpaRepository<Asistencial, Long> {

  Optional<List<Asistencial>> findByActivoTrue();

  Optional<Asistencial> findById(Long id);

  Optional<Asistencial> findByDniAndActivoTrue(int dni);

  Optional<Asistencial> findByCuil(String cuil);

  boolean existsById(Long id);

  boolean existsByDniAndActivoTrue(int dni);

  boolean existsByDni(int dni);

  boolean existsByCuil(String cuil);

  boolean existsByEmailAndActivoTrue(String email);

  Optional<Asistencial> findByEmailAndActivoTrue(String email);

  List<Asistencial> findByActivo(boolean activo);

  @Query("""
          SELECT DISTINCT a
          FROM asistenciales a
          JOIN a.legajos l
          JOIN l.efectores e
          WHERE a.activo = true
            AND l.activo = true
            AND e.id = :idEfector
      """)
  List<Asistencial> findByEfectorByActivoTrue(@Param("idEfector") Long idEfector);

  @Query("SELECT a FROM asistenciales a JOIN a.legajos l JOIN l.udo u WHERE u.id = :idUdo AND u.activo = true AND a.activo = true")
  List<Asistencial> findByUdoAndActivoTrue(@Param("idUdo") Long idUdo);

  @Query("SELECT a FROM asistenciales a JOIN a.legajos l JOIN l.efectores e WHERE e.id = :idEfector AND e.activo = true AND a.activo = true")
  List<Asistencial> findByEfectorAndActivoTrue(@Param("idEfector") Long idEfector);

  @Query("""
          SELECT DISTINCT a
          FROM asistenciales a
          JOIN a.legajos l
          JOIN l.tipoGuardias tg
          JOIN l.efectores e
          WHERE a.activo = true
            AND l.activo = true
            AND e.id = :idEfector
            AND tg.nombre = :tipoGuardia
      """)
  List<Asistencial> findByEfectorAndActivoTrueAndTG(
      @Param("idEfector") Long idEfector,
      @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia);

  @Query("""
          SELECT DISTINCT a
          FROM asistenciales a
          JOIN a.legajos l
          JOIN l.revista r
          JOIN r.categoria c
          JOIN r.cargaHoraria ch
          JOIN l.efectores e
          WHERE a.activo = true
            AND l.activo = true
            AND e.id = :idEfector
            AND c.id = :idCategoria
            AND ch.id = :idCargaHoraria
      """)
  List<Asistencial> findByEfectorAndCategoriaAndCargaHoraria(
      @Param("idEfector") Long idEfector,
      @Param("idCargaHoraria") Long idCargaHoraria,
      @Param("idCategoria") Long idCategoria);
}
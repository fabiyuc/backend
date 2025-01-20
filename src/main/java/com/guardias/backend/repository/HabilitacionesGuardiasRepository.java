package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.HabilitacionesGuardia;
import com.guardias.backend.enums.TipoGuardiaEnum;
@Repository
public interface HabilitacionesGuardiasRepository extends JpaRepository<HabilitacionesGuardia, Long> {

    Optional<List<HabilitacionesGuardia>> findByActivoTrue();

    boolean existsById(Long id);

    boolean existsByAsistencialId(Long asistencialId);

    Optional<HabilitacionesGuardia> findByAsistencialId(Long asistencialId);

    Optional<HabilitacionesGuardia> findByAsistencialIdAndActivoTrue(Long asistencialId);

    @Query("SELECT p FROM habilitacionesGuardias p JOIN p.efectores e WHERE e.id = :idEfector")
    List<HabilitacionesGuardia> findHabilitacionesGuardiasByEfectorAndAsistencial(@Param("idEfector") Long idEfector);


    @Query("""
                SELECT DISTINCT hg.asistencial
                FROM habilitacionesGuardias hg
                JOIN hg.asistencial a
                JOIN a.legajos l
                JOIN l.tipoGuardias tg
                JOIN hg.efectores e
                WHERE hg.activo = true
                  AND l.activo = true
                  AND e.id = :idEfector
                  AND tg.nombre = :tipoGuardia
            """)
    List<Asistencial> findByEfectorAndActivoTrueAndTG(
            @Param("idEfector") Long idEfector,
            @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia);
}

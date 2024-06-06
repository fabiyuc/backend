package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.SumaHoras;

@Repository
public interface SumaHorasRepository extends JpaRepository<SumaHoras, Long> {
    Optional<List<SumaHoras>> findByActivoTrue();

    Optional<SumaHoras> findById(Long id);

    @Query("SELECT COUNT(sh) > 0 FROM sumaHoras sh WHERE sh.registroMensual.id = :idRegistroMensual")
    boolean existByRegistroMensual(@Param("idRegistroMensual") Long idRegistroMensual);

    @Query("SELECT sh FROM sumaHoras sh WHERE sh.registroMensual.id = :idRegistroMensual AND sh.registroMensual.asistencial.id = :idAsistencial")
    List<SumaHoras> findByRegistroMensual(@Param("idRegistroMensual") Long idRegistroMensual,
            @Param("idAsistencial") Long idAsistencial);

    boolean existsById(Long id);

    List<SumaHoras> findByActivo(boolean activo);

}

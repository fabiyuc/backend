package com.guardias.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.SumaHoras;

@Repository
public interface SumaHorasRepository extends JpaRepository<SumaHoras, Long> {
    List<SumaHoras> findByActivoTrue();

    @Query("SELECT COUNT(sh) > 0 FROM sumaHoras sh WHERE sh.registroMensual.id = :idRegistroMensual")
    boolean existByRegistroMensual(@Param("idRegistroMensual") Long idRegistroMensual);

    @Query("SELECT sh FROM sumaHoras sh WHERE sh.registroMensual.id = :idRegistroMensual AND sh.registroMensual.idAsistencial = :idAsistencial")
    List<SumaHoras> findByRegistroMensual(@Param("idRegistroMensual") Long idRegistroMensual,
            @Param("idAsistencial") Long idAsistencial);

}

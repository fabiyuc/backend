package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.RegistroMensual;

@Repository
public interface RegistroMensualRepository extends JpaRepository<RegistroMensual, Long> {

    Optional<RegistroMensual> findById(Long id);

    // TODO por persona y por mes!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // @Query("SELECT rm FROM RegistrosMensuales rm WHERE
    // rm.registroActividad.asistencial.id = :idAsistencial")
    // Optional<RegistroMensual> findByIdAsistencial(@Param("idAsistencial") Long
    // idAsistencial);

    List<RegistroMensual> findByActivoTrue();

    boolean existsByMes(String mes);

    boolean existsByIdAsistencial(Long idAsistencial);

    Optional<List<RegistroMensual>> findByMes(String mes);

    @Query("SELECT rm FROM RegistrosMensuales rm WHERE rm.registroActividad.id = :idRegistroActividad")
    List<RegistroMensual> findByRegistroActividad(@Param("idRegistroActividad") Long idRegistroActividad);

}

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

    @Query("SELECT rm FROM registrosMensuales rm WHERE rm.idAsistencial = :idAsistencial AND rm.mes = :mes AND rm.anio = :anio")
    List<RegistroMensual> findByIdAsistencialAndMes(@Param("idAsistencial") Long idAsistencial,
            @Param("mes") String mes, @Param("anio") int anio);

    List<RegistroMensual> findByActivoTrue();

    boolean existsByMes(String mes);

    boolean existsByIdAsistencial(Long idAsistencial);

    Optional<List<RegistroMensual>> findByMes(String mes);
}

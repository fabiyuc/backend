package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.RegistrosPendientes;

@Repository
public interface RegistrosPendientesRepository extends JpaRepository<RegistrosPendientes, Long> {

    List<RegistrosPendientes> findByActivoTrue();

    List<RegistrosPendientes> findByFecha(LocalDate fecha);

    @Query("SELECT rp FROM RegistrosPendientes rp WHERE rp.efector.id = :idEfector")
    List<RegistrosPendientes> findByEfectorId(@Param("idEfector") Long idEfector);

    @Query("SELECT rp FROM RegistrosPendientes rp WHERE rp.efector.id = :idEfector AND rp.fecha = :fecha")
    Optional<RegistrosPendientes> findByEfectorAndFecha(@Param("idEfector") Long idEfector,
            @Param("fecha") LocalDate fecha);

}

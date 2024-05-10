package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.RegistrosPendientes;

@Repository
public interface RegistrosPendientesRepository extends JpaRepository<RegistrosPendientes, Long> {

    List<RegistrosPendientes> findByActivoTrue();

    List<RegistrosPendientes> findByFecha(LocalDate fecha);

    List<RegistrosPendientes> findByEfector(@Param("efector") Efector efector);

    Optional<RegistrosPendientes> findByEfectorAndFecha(@Param("efector") Efector efector,
            @Param("fecha") LocalDate fecha);

}

package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardias.backend.entity.CargaHoraria;

public interface CargaHorariaRepository extends JpaRepository<CargaHoraria, Long> {
    Optional<CargaHoraria> findByCantidad(int cantidad);

    boolean existsByCantidad(int cantidad);
}

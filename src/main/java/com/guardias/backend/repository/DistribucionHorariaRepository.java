package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.DistribucionHoraria;

@Repository
public interface DistribucionHorariaRepository extends JpaRepository<DistribucionHoraria, Long> {
    Optional<DistribucionHoraria> findByCantidad(int cantidad);

    boolean existsByCantidad(int cantidad);
}

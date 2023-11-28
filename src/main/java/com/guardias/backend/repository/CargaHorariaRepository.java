package com.guardias.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.CargaHoraria;
import java.util.Optional;

@Repository
public interface CargaHorariaRepository extends JpaRepository<CargaHoraria, Long> {
    Optional<CargaHoraria> findByCantidad(int cantidad);
    boolean existsByCantidad(int cantidad);
}

package com.guardias.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.guardias.backend.entity.CargaHoraria;
import java.util.Optional;


public interface CargaHorariaRepository extends JpaRepository<CargaHoraria, Integer> {
    Optional<CargaHoraria> findByCantidad(int cantidad);
    boolean existsByCantidad(int cantidad);
}

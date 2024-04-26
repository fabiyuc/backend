package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.CargaHoraria;

@Repository
public interface CargaHorariaRepository extends JpaRepository<CargaHoraria, Long> {

    /*
     * Optional<CargaHoraria> findByCantidad(int cantidad);
     * 
     * boolean existsByCantidad(int cantidad);
     * 
     * List<CargaHoraria> findByActivoTrue();
     */
    Optional<List<CargaHoraria>> findByActivoTrue();

    Optional<CargaHoraria> findById(Long id);

    Optional<CargaHoraria> findByCantidad(int cantidad);

    boolean existsByCantidad(int cantidad);

    boolean existsById(Long id);

    List<CargaHoraria> findByActivo(boolean activo);
}

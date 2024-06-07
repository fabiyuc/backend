package com.guardias.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGmi;

@Repository
public interface ValorGmiRepository extends JpaRepository<ValorGmi, Long> {

    // SOLO un valor puede estar activo a la vez
    ValorGmi findByActivoTrue();

    // @Query("SELECT vg FROM ValorGmi vg WHERE vg.fechaInicio <= :fecha AND
    // vg.fechaFin >= :fecha")
    // List<ValorGmi> findByDate(@Param("fecha") LocalDate fecha);

}

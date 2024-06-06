package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGmi;

@Repository
public interface ValorGmiRepository extends JpaRepository<ValorGmi, Long> {

    Optional<List<ValorGmi>> findByActivoTrue();

    Optional<ValorGmi> findById(Long id);

    boolean existsById(Long id);

    List<ValorGmi> findByActivo(boolean activo);

    // @Query("SELECT vg FROM ValorGmi vg WHERE vg.fechaInicio <= :fecha AND
    // vg.fechaFin >= :fecha")
    // List<ValorGmi> findByDate(@Param("fecha") LocalDate fecha);

}

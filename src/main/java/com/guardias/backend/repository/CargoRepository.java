package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<List<Cargo>> findByActivoTrue();

    Optional<Cargo> findByNombre(String nombre);

    Optional<Cargo> findById(Long id);

    @Query("SELECT c FROM cargos c WHERE c.fechaInicio = :fechaInicio")
    List<Cargo> findByFechaInicio(LocalDate fechaInicio);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Cargo> findByActivo(boolean activo);
}

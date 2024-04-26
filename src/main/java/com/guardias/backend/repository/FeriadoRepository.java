package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Feriado;

@Repository
public interface FeriadoRepository extends JpaRepository<Feriado, Long> {

    Optional<List<Feriado>> findByActivoTrue();

    Optional<Feriado> findByMotivo(String motivo);

    Optional<Feriado> findById(Long id);

    Optional<Feriado> findByFecha(LocalDate fecha);

    boolean existsByMotivo(String motivo);

    boolean existsByFecha(LocalDate fecha);

    boolean existsById(Long id);

    List<Feriado> findByActivo(boolean activo);
}

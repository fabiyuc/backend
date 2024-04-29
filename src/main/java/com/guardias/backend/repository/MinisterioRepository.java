package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Ministerio;

@Repository
public interface MinisterioRepository extends JpaRepository<Ministerio, Long> {

    Optional<List<Ministerio>> findByActivoTrue();

    Optional<Ministerio> findById(Long id);

    Optional<Ministerio> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Ministerio> findByActivo(boolean activo);
}

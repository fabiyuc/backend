package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Provincia;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

    Optional<List<Provincia>> findByActivoTrue();

    Optional<Provincia> findByNombre(String nombre);

    Optional<Provincia> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Provincia> findByActivo(boolean activo);
}

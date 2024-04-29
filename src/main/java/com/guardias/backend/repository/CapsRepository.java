package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Caps;

@Repository
public interface CapsRepository extends JpaRepository<Caps, Long> {

    Optional<List<Caps>> findByActivoTrue();

    Optional<Caps> findByNombre(String nombre);

    Optional<Caps> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Caps> findByActivo(boolean activo);
}

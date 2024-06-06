package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.TipoCargo;

@Repository
public interface TipoCargoRepository extends JpaRepository<TipoCargo, Long> {
    Optional<List<TipoCargo>> findByActivoTrue();

    Optional<TipoCargo> findByNombre(String nombre);

    Optional<TipoCargo> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<TipoCargo> findByActivo(boolean activo);
}
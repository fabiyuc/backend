package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.TipoCargo;

@Repository
public interface TipoCargoRepository extends JpaRepository<TipoCargo, Long> {
    Optional<TipoCargo> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
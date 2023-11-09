package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardias.backend.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}

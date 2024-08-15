package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.BonoUti;

@Repository
public interface BonoUtiRepository extends JpaRepository<BonoUti, Long> {
    
    Optional<List<BonoUti>> findByActivoTrue();

    Optional<BonoUti> findById(Long id);

    boolean existsById(Long id);

}

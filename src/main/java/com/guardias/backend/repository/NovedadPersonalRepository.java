package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.NovedadPersonal;

@Repository
public interface NovedadPersonalRepository extends JpaRepository<NovedadPersonal, Long> {

    Optional<NovedadPersonal> findByPersona(Long idPersona);

    boolean existsByPersona(Long idPersona);
}

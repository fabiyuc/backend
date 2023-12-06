package com.guardias.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.NovedadPersonal;

@Repository
public interface NovedadPersonalRepository extends JpaRepository<NovedadPersonal, Long> {

    List<NovedadPersonal> findByPersona(Long idPersona);

    boolean existsByPersona(Long idPersona);
}

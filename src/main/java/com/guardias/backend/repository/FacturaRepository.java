package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long>{

    Optional<List<Factura>> findByActivoTrue();
    
    boolean existsByAsistencialId(Long personaId);

    Optional<Factura> findByAsistencialId(Long asistencialId);
}

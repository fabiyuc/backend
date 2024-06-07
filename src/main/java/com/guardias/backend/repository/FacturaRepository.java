package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Factura;
import com.guardias.backend.entity.Person;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByActivoTrue();

    Optional<Factura> findByAsistencial(Person asistencial);

}

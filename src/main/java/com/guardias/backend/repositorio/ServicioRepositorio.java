package com.guardias.backend.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.Servicio;


@Repository
public interface ServicioRepositorio extends JpaRepository<Servicio, Integer> {

    Optional<Servicio> findByDescripcion(String descripcion);
    boolean existsByDescripcion(String descripcion);

}

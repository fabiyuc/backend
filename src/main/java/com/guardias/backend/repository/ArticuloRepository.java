package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Articulo;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    @Query("SELECT a FROM Articulo a WHERE a.activo = true")
    List<Articulo> findAllActivos();

    Optional<Articulo> findByNumero(String numero);

    Optional<Articulo> findByDenominacion(String denominacion);

    boolean existsByNumero(String numero);

    boolean existsByDenominacion(String denominacion);

}

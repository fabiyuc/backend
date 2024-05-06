package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.TipoLey;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    @Query("SELECT a FROM Articulo a WHERE a.activo = true")
    List<Articulo> findAllActivos();

    Optional<List<Articulo>> findByActivoTrue();

    Optional<Articulo> findByNumero(String numero);

    Optional<Articulo> findById(Long id);

    Optional<Articulo> findByDenominacion(String denominacion);

    Optional<Articulo> findByTipoLey(TipoLey tipoLey);

    boolean existsByNumero(String numero);

    boolean existsByDenominacion(String denominacion);

    boolean existsById(Long id);

    List<Articulo> findByActivo(boolean activo);

}

package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.TipoLey;

@Repository
public interface IncisoRepository extends JpaRepository<Inciso, Long> {

    @Query("SELECT i FROM Inciso i JOIN FETCH i.articulo")
    Optional<List<Inciso>> findByActivoTrue();

    Optional<Inciso> findByNumero(String numero);

    Optional<Inciso> findById(Long id);

    Optional<Inciso> findByDenominacion(String denominacion);

    Optional<Articulo> findByTipoLey(TipoLey tipoLey);

    boolean existsById(Long id);

    boolean existsByNumero(String numero);

    boolean existsByDenominacion(String denominacion);

    List<Inciso> findByActivo(boolean activo);

    @Query("SELECT i FROM Inciso i JOIN FETCH i.articulo")
    List<Inciso> findAllWithArticulo();
}

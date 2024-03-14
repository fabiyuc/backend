package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.TipoLey;

@Repository
public interface IncisoRepository extends JpaRepository<Inciso, Long> {

    Optional<Inciso> findByNumero(String numero);

    Optional<Inciso> findByDenominacion(String denominacion);

    Optional<Articulo> findByTipoLey(TipoLey tipoLey);

    boolean existsByNumero(String numero);

    boolean existsByDenominacion(String denominacion);

    List<Inciso> findByActivoTrue();
}

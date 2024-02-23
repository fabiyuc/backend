package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Inciso;

@Repository
public interface IncisoRepository extends JpaRepository<Inciso, Long> {

    Optional<Inciso> findByNumero(String numero);

    Optional<Inciso> findByDenominacion(String denominacion);

    boolean existsByNumero(String numero);

    boolean existsByDenominacion(String denominacion);

}

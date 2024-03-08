package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Ministerio;
import java.util.List;


@Repository
public interface MinisterioRepository extends JpaRepository<Ministerio, Long> {

    Optional<Ministerio> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Ministerio> findByActivoTrue();
}

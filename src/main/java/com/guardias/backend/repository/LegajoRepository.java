package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Legajo;

@Repository
public interface LegajoRepository extends JpaRepository<Legajo, Long> {

    List<Legajo> findByActivoTrue();

    Optional<Legajo> findById(Long id);

    boolean existsById(Long id);

    List<Legajo> findByActivo(boolean activo);
}
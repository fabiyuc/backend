package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.TipoLey;

@Repository
public interface TipoLeyRepository extends JpaRepository<TipoLey, Long> {

    Optional<TipoLey> findByDescripcion(String descripcion);

    boolean existsByDescripcion(String descripcion);
}
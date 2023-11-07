package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.guardias.backend.entity.TipoRevista;

public interface TipoRevistaRepository extends JpaRepository<TipoRevista, Integer> {
    Optional<TipoRevista> findByNombre(String nombre);
    boolean existsByNombre(String nombre);

}

package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Caps;

@Repository
public interface CapsRepository extends JpaRepository<Caps, Long> {

    Optional<List<Caps>> findByActivoTrue();

    Optional<Caps> findByNombre(String nombre);

    Optional<Caps> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Caps> findByActivo(boolean activo);

    @Query("SELECT c.id FROM Caps c WHERE c.id IN :ids")
    List<Long> findValidIds(List<Long> ids);

    @Query("SELECT c.cabecera.nombre FROM Caps c WHERE c.id = :id")
    Optional<String> findCabeceraNameByCapsId(@Param("id") Long id);
}

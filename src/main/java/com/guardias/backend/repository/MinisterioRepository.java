package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Ministerio;

@Repository
public interface MinisterioRepository extends JpaRepository<Ministerio, Long> {

    Optional<List<Ministerio>> findByActivoTrue();

    Optional<Ministerio> findById(Long id);

    Optional<Ministerio> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Ministerio> findByActivo(boolean activo);

    @Query("SELECT m.id FROM Ministerio m WHERE m.id IN :ids")
    List<Long> findValidIds(List<Long> ids);

    @Query("SELECT m FROM Ministerio m " +
            "JOIN m.legajos l " +
            "JOIN l.persona p " +
            "JOIN p.usuarios u " +
            "WHERE u.id = :idUsuario AND m.activo = true AND l.activo = true AND u.activo = true " +
            "ORDER BY l.fechaInicio DESC")
    Optional<Ministerio> findMinisterioByUsuarioId(@Param("idUsuario") Long idUsuario);
}

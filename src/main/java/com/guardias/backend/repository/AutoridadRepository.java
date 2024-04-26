package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Autoridad;

@Repository
public interface AutoridadRepository extends JpaRepository<Autoridad, Long> {

    /*
     * // Optional<Autoridad> findById(Long id);
     * Optional<Autoridad> findByNombre(String nombre);
     * 
     * // boolean existsById(Long id);
     * 
     * boolean existsByNombre(String nombre);
     * 
     * List<Autoridad> findByActivoTrue();
     */
    Optional<List<Autoridad>> findByActivoTrue();

    Optional<Autoridad> findById(Long id);

    Optional<Autoridad> findByNombre(String nombre);

    @Query("SELECT a FROM autoridades a WHERE a.fechaInicio = :fechaInicio AND a.activo = true")
    Optional<List<Autoridad>> findByFechaInicio(LocalDate fechaInicio);

    @Query("SELECT a FROM autoridades a WHERE a.persona.id = :personaId")
    Optional<List<Autoridad>> findByPersonaId(@Param("personaId") Long personaId);

    @Query("SELECT a FROM autoridades a WHERE a.efector.id = :efectorId")
    Optional<List<Autoridad>> findByEfectorId(@Param("efectorId") Long efectorId);

    boolean existsById(Long id);

    boolean existsByEfectorId(Long efectorId);

    boolean existsByPersonaId(Long personaId);

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);

    List<Autoridad> findByActivo(boolean activo);

}

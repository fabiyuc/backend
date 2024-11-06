package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.guardias.backend.entity.Permisos;

public interface PermisosRepository extends JpaRepository<Permisos, Long> {

   Optional<List<Permisos>> findByActivoTrue();

   boolean existsById(Long id);

   boolean existsByIdAsistencial(Long idAsistencial);

   Optional<Permisos> findByIdAsistencial(Long idAsistencial);

   @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM permisos p WHERE p.id_asistencial = :idAsistencial AND :idEfector = ANY (p.id_efectores) AND p.activo = true """, nativeQuery = true)
    boolean existsByIdPersonaAndIdEfector(
        @Param("idAsistencial") Long idAsistencial,
        @Param("idEfector") Long idEfector
    );
    
}

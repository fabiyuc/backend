package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.NoAsistencial;

@Repository
public interface NoAsistencialRepository extends JpaRepository<NoAsistencial, Long> {

    Optional<List<NoAsistencial>> findByActivoTrue();

    Optional<NoAsistencial> findById(Long id);

    Optional<NoAsistencial> findByDni(int dni);

    Optional<NoAsistencial> findByCuil(String cuil);

    boolean existsById(Long id);

    boolean existsByDni(int dni);

    boolean existsByCuil(String cuil);

    List<NoAsistencial> findByActivo(boolean activo);

    @Query("SELECT n FROM noAsistenciales n JOIN n.legajos l JOIN l.efectores e WHERE e.id = :idEfector AND e.activo = true AND n.activo = true")
    List<NoAsistencial> findByEfectorAndActivoTrue(@Param("idEfector") Long idEfector);
}
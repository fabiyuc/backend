package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.NoAsistencial;

@Repository
public interface NoAsistencialRepository extends JpaRepository<NoAsistencial, Long> {

    Optional<NoAsistencial> findById(Long id);

    Optional<NoAsistencial> findByDni(int dni);

    boolean existsById(Long id);

    boolean existsByDni(int dni);

    boolean existsByCuil(String cuil);

    List<NoAsistencial> findByEstado(Boolean estado);

    List<NoAsistencial> findByActivoTrue();
}

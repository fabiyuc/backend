package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Asistencial;

@Repository
public interface AsistencialRepository extends JpaRepository<Asistencial, Long> {

    Optional<Asistencial> findById(Long id);

    Optional<Asistencial> findByDni(int dni);

    boolean existsById(Long id);

    boolean existsByDni(int dni);

    boolean existsByCuil(String cuil);

    List<Asistencial> findByActivoTrue();

    List<Asistencial> findByActivo(boolean activo);

}

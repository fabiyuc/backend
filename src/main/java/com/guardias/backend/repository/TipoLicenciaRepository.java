package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.TipoLicencia;

@Repository
public interface TipoLicenciaRepository extends JpaRepository<TipoLicencia, Long> {

    Optional<List<TipoLicencia>> findByActivoTrue();

    Optional<TipoLicencia> findByNombre(String nombre);

    Optional<TipoLicencia> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<TipoLicencia> findByActivo(boolean activo);
}

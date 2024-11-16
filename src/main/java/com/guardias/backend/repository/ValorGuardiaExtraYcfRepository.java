package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGuardiaExtrayCF;

@Repository
public interface ValorGuardiaExtraYcfRepository extends JpaRepository<ValorGuardiaExtrayCF, Long> {

    Optional<List<ValorGuardiaExtrayCF>> findByActivoTrue();

    Optional<ValorGuardiaExtrayCF> findById(Long id);

    boolean existsById(Long id);

    List<ValorGuardiaExtrayCF> findByActivo(boolean activo);
}

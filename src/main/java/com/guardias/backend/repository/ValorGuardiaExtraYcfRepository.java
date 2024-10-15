package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGuardiaExtrayCF;

@Repository
public interface ValorGuardiaExtraYcfRepository extends JpaRepository<ValorGuardiaExtrayCF, Long> {

    Optional<List<ValorGuardiaExtrayCF>> findByActivoTrue();

    Optional<ValorGuardiaExtrayCF> findById(Long id);
    
    @Query("SELECT v FROM ValorGuardiaExtrayCF v JOIN v.efectores e WHERE e.nombre = :nombreEfector")
    Optional<ValorGuardiaExtrayCF> findByEfectorNombre(@Param("nombreEfector") String nombreEfector);

    boolean existsById(Long id);

    List<ValorGuardiaExtrayCF> findByActivo(boolean activo);
}

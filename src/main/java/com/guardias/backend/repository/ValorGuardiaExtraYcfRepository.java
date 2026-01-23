package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.enums.TipoGuardiaEnum;

@Repository
public interface ValorGuardiaExtraYcfRepository extends JpaRepository<ValorGuardiaExtrayCF, Long> {

    Optional<List<ValorGuardiaExtrayCF>> findByActivoTrue();

    Optional<ValorGuardiaExtrayCF> findById(Long id);

    boolean existsById(Long id);

    List<ValorGuardiaExtrayCF> findByActivo(boolean activo);

    // Busca valores específicos para un hospital
    Optional<ValorGuardiaExtrayCF> findByHospitalesIdAndActivoTrue(Long hospitalId);
    
    // Busca valores genéricos (sin hospitales asignados)
    Optional<ValorGuardiaExtrayCF> findByActivoTrueAndHospitalesIsEmpty();

    List<ValorGuardiaExtrayCF> findByTipoGuardiaAndNivelComplejidadAndActivoTrue(
        TipoGuardiaEnum tipoGuardia, 
        int nivelComplejidad
    );
}

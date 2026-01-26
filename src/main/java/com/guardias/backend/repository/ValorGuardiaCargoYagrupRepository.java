package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGuardiaCargoYagrup;
import com.guardias.backend.enums.TipoGuardiaEnum;

@Repository
public interface ValorGuardiaCargoYagrupRepository extends JpaRepository<ValorGuardiaCargoYagrup, Long> {

    Optional<List<ValorGuardiaCargoYagrup>> findByActivoTrue();

    Optional<ValorGuardiaCargoYagrup> findById(Long id);

    boolean existsById(Long id);

    List<ValorGuardiaCargoYagrup> findByActivo(boolean activo);

    // Busca valores específicos para un hospital
    Optional<ValorGuardiaCargoYagrup> findByHospitalesIdAndActivoTrue(Long hospitalId);
    
    // Busca valores genéricos (sin hospitales asignados)
    Optional<ValorGuardiaCargoYagrup> findByActivoTrueAndHospitalesIsEmpty();

    List<ValorGuardiaCargoYagrup> findByTipoGuardiaAndNivelComplejidadAndActivoTrue(
        TipoGuardiaEnum tipoGuardia, 
        int nivelComplejidad
    );
}

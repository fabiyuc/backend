package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.CronogramaTentativo;

@Repository
public interface CronogramaTentativoRepository extends JpaRepository<CronogramaTentativo, Long>{

    Optional<List<CronogramaTentativo>> findByActivoTrue();
    
    boolean existsById(Long id);

    Optional<CronogramaTentativo> findById(Long id);
}

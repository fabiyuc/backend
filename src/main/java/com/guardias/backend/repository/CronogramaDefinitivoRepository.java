package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface CronogramaDefinitivoRepository extends JpaRepository <CronogramaDefinitivo, Long> {

    Optional<CronogramaDefinitivo> findById(Long id);

        Optional<List<CronogramaDefinitivo>> findByActivoTrue();

        boolean existsByAnioAndMes(int anio, MesesEnum mes);

        List<CronogramaDefinitivo> findByAnioAndMes(int anio, MesesEnum mes);

        boolean existsById(Long id);

        List<CronogramaDefinitivo> findByActivo(boolean activo);
    
}

package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGmi;

@Repository
public interface ValorGmiRepository extends JpaRepository<ValorGmi, Long> {

    public List<ValorGmi> findByActivoTrue();

    @Query("SELECT vg FROM ValorGmi vg WHERE :fecha BETWEEN vg.fechaInicio AND vg.fechaFin")
    public List<ValorGmi> findByDate(@Param("fecha") LocalDate fecha);
}

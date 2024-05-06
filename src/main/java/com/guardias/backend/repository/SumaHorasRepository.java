package com.guardias.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.SumaHoras;

@Repository
public interface SumaHorasRepository extends JpaRepository<SumaHoras, Long> {
    public List<SumaHoras> findByActivoTrue();
}

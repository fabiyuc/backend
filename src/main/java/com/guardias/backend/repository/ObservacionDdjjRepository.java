package com.guardias.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ObservacionDdjj;

@Repository
public interface ObservacionDdjjRepository extends JpaRepository<ObservacionDdjj, Long> {
    
    List<ObservacionDdjj> findByActivoTrue();
}

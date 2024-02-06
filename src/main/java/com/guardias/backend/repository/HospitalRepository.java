package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Hospital;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByNombre(String nombre);

    @Query("SELECT h FROM hospitales h WHERE h.admitePasiva = true")
    List<Hospital> findByAdmitePasiva();

    boolean existsByNombre(String nombre);
}

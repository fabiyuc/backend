package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Optional<Log> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

}

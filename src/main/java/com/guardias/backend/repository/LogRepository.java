package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    Optional<List<Log>> findByActivoTrue();

    Optional<Log> findById(Long id);

    boolean existsById(Long id);

    List<Log> findByActivo(boolean activo);

}

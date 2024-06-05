package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.JsonFile;

@Repository
public interface JsonFileRepository extends JpaRepository<JsonFile, Long> {

    Optional<List<JsonFile>> findByActivoTrue();
}

package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<List<Categoria>> findByActivoTrue();

    Optional<Categoria> findByNombre(String nombre);

    Optional<Categoria> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Categoria> findByActivo(boolean activo);
}

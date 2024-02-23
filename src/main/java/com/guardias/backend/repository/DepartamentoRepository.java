package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Departamento;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    
    Optional<Departamento> findByNombre(String nombre);

    Optional<Departamento> findByCodigoPostal(String codigoPostal);

    boolean existsByNombre(String nombre);
    
    boolean existsByCodigoPostal(String codigoPostal);

}
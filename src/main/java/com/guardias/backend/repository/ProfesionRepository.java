package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface ProfesionRepository extends JpaRepository<Profesion, Long> {
    
    Optional<List<Profesion>> findByActivoTrue();

    Optional<Profesion> findById(Long id);
    
    boolean existsById(Long id);

    Optional<Profesion> findByNombre(String nombre);
   
    @Query("SELECT p FROM profesiones p JOIN p.especialidades e WHERE p.id = :id AND e.id = :idEspecialidad")
    Optional<Profesion> findByEspecialidad(@Param("id") Long id,@Param("idEspecialidad") Long idEspecialidad );

    boolean existsByNombre(String nombre);

    List<Profesion> findByAsistencialTrue();
    
    List<Profesion> findByAsistencialFalse();

    List<Profesion> findByActivo(boolean activo);
}

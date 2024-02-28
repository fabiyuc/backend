package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    
    Optional<Region> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Region> findByActivo(boolean activo);

}

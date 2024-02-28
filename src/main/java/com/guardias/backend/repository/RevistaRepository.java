package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Revista;

@Repository
public interface RevistaRepository extends JpaRepository<Revista, Long> {

    Optional<Revista> findById(Long id);

    boolean existsById(Long id);

    List<Revista> findByActivo(boolean activo);
}

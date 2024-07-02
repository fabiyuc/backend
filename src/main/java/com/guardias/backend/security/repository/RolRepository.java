package com.guardias.backend.security.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.guardias.backend.security.entity.Rol;
import com.guardias.backend.security.enums.RolNombre;

//@Repository porque no lo tiene?
public interface RolRepository extends JpaRepository <Rol, Integer> {
    
    Optional<Rol> findByRolNombre(RolNombre rolNombre);

}

package com.guardias.backend.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.security.entity.Rol;
import com.guardias.backend.security.enums.RolNombre;

@Repository
public interface RolRepository extends JpaRepository <Rol, Integer> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);
}

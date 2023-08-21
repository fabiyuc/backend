package com.guardias.backend.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.security.entity.Rol;
import com.guardias.backend.security.enums.RolNombre;
import com.guardias.backend.security.repository.RolRepository;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNomnre(RolNombre rolNombre) {
        return rolRepository.findByRolNombre(rolNombre);
    }

    
}

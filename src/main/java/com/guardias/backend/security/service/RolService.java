package com.guardias.backend.security.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.security.entity.Rol;
import com.guardias.backend.security.enums.RolNombre;
import com.guardias.backend.security.repository.RolRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RolService {
    
    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre) {
        return rolRepository.findByRolNombre(rolNombre);
    }
    public void save (Rol rol){
        rolRepository.save(rol);
    }
    
}

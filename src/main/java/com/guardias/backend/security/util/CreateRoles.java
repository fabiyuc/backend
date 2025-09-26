package com.guardias.backend.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.guardias.backend.security.entity.Rol;
import com.guardias.backend.security.enums.RolNombre;
import com.guardias.backend.security.service.RolService;

// clase para crear los roles en la BD, solo se ejecutan una vez por eso se encuentra comentada la clase
@Component
public class CreateRoles implements CommandLineRunner{
    @Autowired
    RolService rolService;
    @Override
    public void run(String... args) throws Exception {
        Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
        Rol rolUser = new Rol(RolNombre.ROLE_USER);
        Rol rolDph = new Rol(RolNombre.ROLE_DPH);
        Rol rolSuperUser = new Rol(RolNombre.ROLE_SUPERUSER);
        Rol rolAutoridad = new Rol(RolNombre.ROLE_AUTORIDAD);
        Rol rolHospital = new Rol(RolNombre.ROLE_HOSPITAL);
        rolService.save(rolAdmin);
        rolService.save(rolUser);
        rolService.save(rolDph);
        rolService.save(rolSuperUser);
        rolService.save(rolAutoridad);
        rolService.save(rolHospital);
    }

}

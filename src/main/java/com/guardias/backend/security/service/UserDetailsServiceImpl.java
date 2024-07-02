package com.guardias.backend.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.security.entity.UsuarioPrincipal;
import com.guardias.backend.security.repository.UsuarioRepository;

/* convierte la clase Usuario en un UsuarioPrincipal  */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    //aqui usa un service?? min 8:50 cap 3
    @Autowired
    UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String nombreOrEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuarioOrEmail(nombreOrEmail,nombreOrEmail).get();
        return UsuarioPrincipal.build(usuario);
    }
}

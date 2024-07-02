package com.guardias.backend.security.entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioPrincipal implements UserDetails{

    // coviente la clase usuario encargada de acceder a la BD donde la clase rol es convertida a la clase GrantedAuthority propia de Spring Security
    private String nombre;
    private String nombreUsuario;
    private String email;
    private String password;
    //GrantedAuthority obtiene la autorizacion del usuario  (lo que puede o no hacer)
    private Collection<? extends GrantedAuthority> authorities;
    

    public UsuarioPrincipal(String nombre, String nombreUsuario, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    //convierte un usuario que representa a la entidad de la BD en un usuario principal que utilizamos para saber los privilegios de cada usuario
    public static UsuarioPrincipal build(Usuario usuario){
        //obtenemos una lista de GrantedAuthority a partir de los roles
        //convertimos la clase rol en clase GrantedAuthority
        List<GrantedAuthority> authorities =
            usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol
            .getRolNombre().name())).collect(Collectors.toList());

        return new UsuarioPrincipal(usuario.getNombre(), usuario.getNombreUsuario(), usuario.getEmail(), usuario.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return nombreUsuario;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    public String getNombre() {
        return nombre;
    }
    public String getEmail() {
        return email;
    }
    
}

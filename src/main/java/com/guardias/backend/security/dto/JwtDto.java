package com.guardias.backend.security.dto;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//lo utilizamos en el momento que hagamos un login, devuelve el responseEntity del controlador, un json web tok en
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {
    
    private String token;
    private String bearer = "Bearer";
    private String nombreUsuario;
    private Collection<? extends GrantedAuthority> authorities;
    
    public JwtDto(String token, String nombreUsuario, Collection<? extends GrantedAuthority> authorities) {
        
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.authorities = authorities;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getBearer() {
        return bearer;
    }
    
    public void setBearer(String bearer) {
        this.bearer = bearer;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
    
}

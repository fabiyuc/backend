package com.guardias.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.guardias.backend.security.jwt.JwtEntryPoint;
import com.guardias.backend.security.jwt.JwtTokenFilter;
import com.guardias.backend.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
/* @EnableGlobalMethodSecurity(prePostEnabled=true) parametro para indicar a que metodos puede acceder solo el administrador
Los metodos que no llevan una anotacion pueden hacerlo tanto el admin como un usuario generico
 */
@EnableMethodSecurity
public class MainSecurity {
   
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    AuthenticationManager authenticationManager;
     
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
        authenticationManager = builder.build();
        http.authenticationManager(authenticationManager);

        http.csrf((csrf)->csrf.disable());
        http.cors(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        /* http.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login",
        "/auth/nuevo",
        "/email-password/**",
        "/v2/api-docs/**",
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/configuration/**").permitAll().anyRequest().authenticated()); */

        http.exceptionHandling(exc -> exc.authenticationEntryPoint(jwtEntryPoint));
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

        

    }

}

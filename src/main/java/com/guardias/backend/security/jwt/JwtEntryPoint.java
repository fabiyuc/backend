package com.guardias.backend.security.jwt;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * comprueba si existe un token y devuelve una respuesta
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint{
    
    //para ver cual es el metodo que esté dando error en caso de que no funcione la aplicacion
    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res,
            AuthenticationException e) throws IOException, ServletException {

        logger.error("Fail en el metodo commence, clase JwtEntryPoint");
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NO AUTORIZADO...");
    }
}

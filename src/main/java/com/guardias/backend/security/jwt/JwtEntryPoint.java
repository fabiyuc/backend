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
    
    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res,
            AuthenticationException e) throws IOException, ServletException {

        logger.error("fail en el metodo commnece");
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "no autorizadooooooo");
    }
}

package com.guardias.backend.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * API q se va a crear
 */

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DemoController {
    
    /**
     * Endpoint para realizar las pruebas
     * @return
     */
    @PostMapping(value="demo")
    public String welcome()
    {
        return "Welcome from secure endpoint.";
    }
}

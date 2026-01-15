package com.guardias.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.DistribucionesConCronogramasDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ResultadoCreacionDto;
import com.guardias.backend.service.DistribucionCompletaService;

@RestController
@RequestMapping("/distribuciones-completas")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionCompletaController {
    
    @Autowired
    private DistribucionCompletaService distribucionCompletaService;


    @PostMapping("/crear")
    public ResponseEntity<?> crearDistribucionesConCronogramas(
            @RequestBody DistribucionesConCronogramasDto dto) {
        System.out.println("DTO recibido: " + dto);
        System.out.println("Tipo de dia en primera guardia: " + dto.getGuardias().get(0).getDia().getClass());
        try {
            ResultadoCreacionDto resultado = distribucionCompletaService
                .crearDistribucionesConCronogramas(dto);
            
            String mensaje = String.format(
                "Se crearon %d distribuciones y %d cronogramas tentativos",
                resultado.getDistribucionesCreadas(),
                resultado.getCronogramasCreados()
            );
            
            return ResponseEntity.ok(new Mensaje(mensaje));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new Mensaje("Error al crear distribuciones: " + e.getMessage()));
        }
    }

}

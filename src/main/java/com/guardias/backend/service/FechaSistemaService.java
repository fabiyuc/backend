package com.guardias.backend.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;

/**
 * Fecha "de hoy" para las reglas de negocio (plazos de facturas y DDJJ).
 *
 * Si en application.properties se define app.fecha-simulada (ej: 2026-11-01 o
 * 2026-11-01T08:00:00), se usa esa fecha como punto de partida y avanza con el
 * tiempo real transcurrido desde que arrancó el backend. Sirve para probar
 * plazos junto con la fecha simulada del front (main.ts).
 * Sin la propiedad se usa la fecha real del servidor.
 */
@Service
public class FechaSistemaService {

    @Value("${app.fecha-simulada:}")
    private String fechaSimulada;

    private LocalDateTime inicioSimulado;
    private LocalDateTime inicioReal;

    @PostConstruct
    void init() {
        if (StringUtils.isBlank(fechaSimulada))
            return;

        String valor = fechaSimulada.trim();
        inicioSimulado = valor.contains("T") ? LocalDateTime.parse(valor) : LocalDate.parse(valor).atStartOfDay();
        inicioReal = LocalDateTime.now();
        System.out.println("⚠️ FECHA SIMULADA ACTIVA: " + inicioSimulado);
    }

    public LocalDateTime ahora() {
        if (inicioSimulado == null)
            return LocalDateTime.now();
        return inicioSimulado.plus(Duration.between(inicioReal, LocalDateTime.now()));
    }

    public LocalDate hoy() {
        return ahora().toLocalDate();
    }
}

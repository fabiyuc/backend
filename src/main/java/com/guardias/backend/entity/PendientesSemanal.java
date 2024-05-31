package com.guardias.backend.entity;

import java.time.LocalDate;

import com.guardias.backend.enums.MesesEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "PendientesSemanal")
@Data
@AllArgsConstructor
@NoArgsConstructor

// VER completar, para generar informes con las salidas que no fueron
// registradas a tiempo
public class PendientesSemanal {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Temporal(TemporalType.DATE)
        private LocalDate fechaInicio;
        @Temporal(TemporalType.DATE)
        private LocalDate fechaFin;
        @Enumerated(EnumType.STRING)
        private MesesEnum mes;
        private int anio;
        private boolean activo;

        private String efectorJson;

        private String registrosPendientesJson;

}

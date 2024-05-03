package com.guardias.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "sumaHoras")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumaHoras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float horasLav;
    private float horasSdf;
    private float bonoLav;
    private float bonoSdf;

    @OneToOne
    @JoinColumn(name = "registro_mensual_id", referencedColumnName = "id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "fechaEgreso", "anio",
            "registroActividad", "idAsistencial", "efector", "ddjj", "sumaHoras" })
    private RegistroMensual registroMensual;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;
}

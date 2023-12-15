package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "novedadesPersonales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovedadPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    private Long idExtensionLicencia; // en caso que se extienda la licecncia, crea una nueva y se la asocia a la
                                      // anterior mediante este id

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Person persona;

    @OneToOne
    @JoinColumn(name = "reemplazante_id")
    private Person reemplazante;

    @OneToOne(mappedBy = "novedadPersonal")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "novedadPersonal" })
    private TipoLicencia tipoLicencia;

}

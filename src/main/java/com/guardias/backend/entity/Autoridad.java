package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "autoridades")
@Data
@AllArgsConstructor
// @RequiredArgsConstructor
@NoArgsConstructor
public class Autoridad {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(columnDefinition = "VARCHAR(25)")
        private String nombre;

        @Temporal(TemporalType.DATE)
        private LocalDate fechaInicio;

        @Temporal(TemporalType.DATE)
        private LocalDate fechaFinal;
        private boolean esActual;
        private boolean esRegional;

        /*
         * //TODO hacer la relacion N:M con PERSONA
         * modificar el resto de los elementos de Autoridad
         * el repositorio debe poder traer el listado de autoridades, el histoirial de
         * un efector y el historial de una persona
         */

        // @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
        // @JoinTable(name = "autoridad_efector", joinColumns = @JoinColumn(name =
        // "id_autoridad"), inverseJoinColumns = @JoinColumn(name = "id_efector"))
        // private Set<Efector> efectores;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_efector")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "autoridades", "domicilio", "telefono", "estado",
                        "observacion", "region", "esCabecera", "admitePasiva", "caps" })
        private Efector efector;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_persona")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "legajos", "novedadesPersonales", "suplentes",
                        "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email", "domicilio",
                        "estado" })
        private Person persona;

}

package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
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
        @Column(columnDefinition = "BIT DEFAULT 1")
        private boolean activo;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_efector")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "autoridades", "domicilio", "telefono",
                        "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias",
                        "legajosUdo", "legajos", "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
                        "areaProgramatica", "tipoCaps", "nivelComplejidad", "cabecera", "ministerios",
                        "registrosActividades",
                        "registroMensual", "ddjjs", "registrosPendientes" })
        private Efector efector;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_persona")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "apellido", "dni", "cuil", "legajos",
                        "novedadesPersonales", "suplentes",
                        "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email", "domicilio",
                        "estado", "activo", "autoridades", "tipoGuardia", "registrosActividades", "descripcion" })
        private Person persona;

        // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
        // "fechaInicio","fechaFinal","esActual","esRegional","activo","efector","persona"
        // })
        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                Autoridad other = (Autoridad) obj;
                if (id == null) {
                        if (other.id != null)
                                return false;
                } else if (!id.equals(other.id))
                        return false;
                if (nombre == null) {
                        if (other.nombre != null)
                                return false;
                } else if (!nombre.equals(other.nombre))
                        return false;
                if (fechaInicio == null) {
                        if (other.fechaInicio != null)
                                return false;
                } else if (!fechaInicio.equals(other.fechaInicio))
                        return false;
                if (fechaFinal == null) {
                        if (other.fechaFinal != null)
                                return false;
                } else if (!fechaFinal.equals(other.fechaFinal))
                        return false;
                if (esActual != other.esActual)
                        return false;
                if (esRegional != other.esRegional)
                        return false;
                if (efector == null) {
                        if (other.efector != null)
                                return false;
                } else if (!efector.equals(other.efector))
                        return false;
                if (persona == null) {
                        if (other.persona != null)
                                return false;
                } else if (!persona.equals(other.persona))
                        return false;
                return true;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
                result = prime * result + ((fechaInicio == null) ? 0 : fechaInicio.hashCode());
                result = prime * result + ((fechaFinal == null) ? 0 : fechaFinal.hashCode());
                result = prime * result + ((efector == null) ? 0 : efector.hashCode());
                result = prime * result + ((persona == null) ? 0 : persona.hashCode());
                return result;
        }

}

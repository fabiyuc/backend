package com.guardias.backend.entity;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "autoridades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Autoridad {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(columnDefinition = "BIT DEFAULT 1")
        private boolean activo;
        
        @Column(columnDefinition = "BIT DEFAULT 1")
        private Boolean confirmado;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_persona")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dni", "cuil", "legajos","novedadesPersonales", "suplentes", "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email", "domicilio", "estado", "activo", "autoridades", "tipoGuardia", "registrosActividades", "descripcion", "usuario", "registrosMensuales" })
        private Person persona;


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
                result = prime * result + ((persona == null) ? 0 : persona.hashCode());
                return result;
        }

}

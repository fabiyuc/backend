package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.MesesEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_efector")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "autoridades", "domicilio", "telefono",
                        "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias",
                        "legajosUdo", "legajos", "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
                        "areaProgramatica", "tipoCaps", "nivelComplejidad", "cabecera", "ministerios",
                        "registrosActividades",
                        "registroMensual", "ddjjs", "registrosPendientes" })
        private Efector efector;
        private String efectorJson;
        @Lob
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "id_json_file")
        private JsonFile jsonFile;

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                PendientesSemanal other = (PendientesSemanal) obj;
                if (id == null) {
                        if (other.id != null)
                                return false;
                } else if (!id.equals(other.id))
                        return false;
                return true;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                return result;
        }

}

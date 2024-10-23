package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "asistenciales")
@Data
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
@AllArgsConstructor
@NoArgsConstructor
public class Asistencial extends Person {

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "asistencial_tipoguardia", joinColumns = @JoinColumn(name = "id_asistencial"), inverseJoinColumns = @JoinColumn(name = "id_tipoGuardia"))
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "descripcion", "activo", "asistenciales", "registrosActividades" })
        private List<TipoGuardia> tiposGuardias = new ArrayList<TipoGuardia>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "asistencial", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
                        "activo", "fechaIngreso", "fechaEgreso", "horaIngreso", "horaEgreso", "tipoGuardia",
                        "asistencial",
                        "servicio", "efector", "registrosPendientes" })
        private List<RegistroActividad> registrosActividades = new ArrayList<>();

        // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
        // "apellido", "dni", "cuil", "legajos",
        // "novedadesPersonales", "suplentes",
        // "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email",
        // "domicilio",
        // "estado", "activo", "autoridades", "tipoGuardia", "registrosActividades",
        // "descripcion","esAsistencial","tiposGuardias" })

        ///// metodo para contrafactura /////

        // Getter para tiposGuardias
        public List<TipoGuardia> getTiposGuardias() {
                return tiposGuardias;
        }

        // Setter para tiposGuardias
        public void setTiposGuardias(List<TipoGuardia> tiposGuardias) {
                this.tiposGuardias = tiposGuardias;
        }
}
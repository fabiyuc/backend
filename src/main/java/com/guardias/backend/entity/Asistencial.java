package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "asistencial", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
                        "fechaIngreso", "fechaEgreso", "horaIngreso", "horaEgreso", "tipoGuardia",
                        "asistencial", "servicio", "efector", "registrosPendientes", "registroMensual",
                        "usuarioIngreso", "usuarioEgreso", "fechaRegistroIngreso", "horaRegistroIngreso",

                        "fechaRegistroEgreso", "horaRegistroEgreso", "horasRealizadas", "activo" })
        private List<RegistroActividad> registrosActividades = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "asistencial", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "fechaIngreso", "fechaEgreso",
                        "horaIngreso", "horaEgreso", "tipoGuardia", "asistencial", "servicio", "efector",
                        "registrosPendientes", "observacion", "aceptado" })
        private List<CronogramaTentativo> cronogramasTentativos = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "asistencial", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
        private List<HabilitacionesGuardia> habilitacionesGuardias = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "asistencial", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "mes", "fechaEgreso", "anio", "totalHoras",
                        "registroActividad", "efector", "ddjj", "sumaHoras", "asistencial", "efector" })
        List<CronogramaDefinitivo> cronogramasDefinitivos = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "asistencial", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "asistencial" })
        private List<Factura> facturas = new ArrayList<>();


}
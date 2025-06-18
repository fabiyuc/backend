package com.guardias.backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.security.entity.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "registrosActividades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroActividad {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Temporal(TemporalType.DATE)
        private LocalDate fechaIngreso;

        @Temporal(TemporalType.DATE)
        private LocalDate fechaEgreso;

        @Temporal(TemporalType.TIME)
        private LocalTime horaIngreso;

        @Temporal(TemporalType.TIME)
        private LocalTime horaEgreso;

        @ManyToOne(fetch = FetchType.LAZY, optional = true)
        @JoinColumn(name = "id_tipo_guardia")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "asistenciales", "legajos", "activo",
                        "registrosActividades", "cronogramasTentativos", "descripcion" })
        private TipoGuardia tipoGuardia;

        @Column(columnDefinition = "BIT DEFAULT 1")
        private boolean activo;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_asistencial")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
                        "estado", "tipoGuardia", "descripcion", "tiposGuardias", "registrosActividades", "dni",
                        "fechaNacimiento", "sexo", "telefono", "email", "domicilio", "esAsistencial", "activo",
                        "suplentes", "autoridades", "cuil", "legajos", "novedadesPersonales", "distribucionesHorarias",
                        "registrosMensuales", "usuarios", "habilitacionesGenerales", "cronogramasTentativos",
                        "habilitacionesGuardias", "facturas" })
        private Asistencial asistencial;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_servicio")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nivel", "activo", "registrosActividades",
                        "distribucionesGuardias", "critico", "efectores", "distribucionesConsultorios" })
        private Servicio servicio;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_efector")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "autoridades", "domicilio", "telefono", "estado",
                        "activo", "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo",
                        "legajos", "tipoCaps", "nivelComplejidad", "cabecera", "ministerios", "registrosActividades",
                        "registroMensual", "ddjjs", "registrosPendientes", "servicios", "habilitacionesGuardias",
                        "habilitacionesGenerales", "cronogramasTentativos", "feriados" })
        private Efector efector;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_registro_mensual")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "fechaEgreso", "anio",
                        "registroActividad", "idAsistencial", "efector", "ddjj", "sumaHoras", "asistencial" })
        private RegistroMensual registroMensual;

        @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_registros_pendientes")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "fecha", "efector",
                        "registrosActividades" })
        RegistrosPendientes registrosPendientes;

        @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "usuarioIngreso")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "nombre", "nombreUsuario", "email",
                        "password", "roles", "registrosIngresos", "registrosEgresos", "person", "asistencial",
                        "noAsistencial" })
        Usuario usuarioIngreso;

        @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "usuarioEgreso")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "nombre", "nombreUsuario", "email",
                        "password", "roles", "registrosIngresos", "registrosEgresos", "person", "asistencial",
                        "noAsistencial" })
        Usuario usuarioEgreso;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_cronograma_definitivo")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "fechaEgreso", "anio",
                        "registroActividad", "efector", "ddjj", "sumaHoras", "asistencial" })
        private CronogramaDefinitivo cronogramaDefinitivo;

        @Temporal(TemporalType.DATE)
        private LocalDate fechaRegistroIngreso;

        @Temporal(TemporalType.TIME)
        private LocalTime horaRegistroIngreso;

        @Temporal(TemporalType.DATE)
        private LocalDate fechaRegistroEgreso;

        @Temporal(TemporalType.TIME)
        private LocalTime horaRegistroEgreso;

        @OneToOne
        @JoinColumn(name = "horas_realizas_id")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "registroActividad" })
        private SumaHoras horasRealizadas;

        @Column(columnDefinition = "VARCHAR(300)")
        private String motivoIngreso;
        @Column(columnDefinition = "VARCHAR(300)")
        private String motivoEgreso;

        /*
         * @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade =
         * CascadeType.REMOVE)
         * 
         * @JoinColumn(name = "id_valor_guardia")
         * 
         * @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler"})
         * private ValorGuardiaBase valorGuardia;
         */

        // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler","activo",
        // "fechaIngreso", "fechaEgreso", "horaIngreso", "horaEgreso", "tipoGuardia",
        // "asistencial","servicio", "efector",
        // "registroMensual","registrosPendientes","usuario","horaRegistro","fechaRegistro"
        // })

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                RegistroActividad other = (RegistroActividad) obj;
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

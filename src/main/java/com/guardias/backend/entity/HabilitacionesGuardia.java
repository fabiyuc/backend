package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "habilitacionesGuardias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabilitacionesGuardia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    /* @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_persona", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaNacimiento", "novedadesPersonales", "suplentes", "distribucionesHorarias", "autoridades", "registrosActividades",
    "registrosMensuales", "usuario","habilitacionesGuardias", "legajos"})
    private Person persona; */

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_asistencial")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
            "estado", "tipoGuardia", "descripcion", "tiposGuardias", "registrosActividades", "dni", "fechaNacimiento",
            "sexo", "telefono", "email", "domicilio", "esAsistencial", "activo", "suplentes", "autoridades" , "legajos","cronogramasTentativos", "habilitacionesGuardias", "habilitacionesGenerales", "novedadesPersonales", "distribucionesHorarias", "registrosMensuales" , "usuarios"})
    private Asistencial asistencial;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "habilitacionesguardias_efectores", joinColumns = @JoinColumn(name = "permiso_id"),
        inverseJoinColumns = @JoinColumn(name = "id_efector"))
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler","domicilio", "telefono", "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo", "legajos", "servicios", "registrosActividades","registroMensual", "ddjjs", "registrosPendientes", "esCabecera", "admitePasiva",  "nivelComplejidad", "caps", "valoresGuardiaBase","habilitacionesGuardias", "habilitacionesGenerales" ,"cronogramasTentativos", "feriados" })
    private List<Efector> efectores = new ArrayList<>();

}

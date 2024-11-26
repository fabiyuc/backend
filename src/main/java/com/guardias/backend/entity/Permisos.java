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

@Entity(name = "permisos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permisos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_persona", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaNacimiento", "novedadesPersonales", "suplentes", "distribucionesHorarias", "autoridades", "registrosActividades",
    "registrosMensuales", "usuario","permisos", "legajos"})
    private Person persona;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "permisos_efectores", joinColumns = @JoinColumn(name = "permiso_id"),
        inverseJoinColumns = @JoinColumn(name = "id_efector"))
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler","domicilio", "telefono", "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo", "legajos", "servicios", "registrosActividades","registroMensual", "ddjjs", "registrosPendientes", "esCabecera", "admitePasiva",  "nivelComplejidad", "caps", "valoresGuardiaBase","permisos" })
    private List<Efector> efectores = new ArrayList<>();

}

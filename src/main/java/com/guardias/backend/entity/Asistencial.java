package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    private TipoGuardiaEnum tipoGuardia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "asistencial", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "fechaIngreso", "fechaEgreso",
            "horaIngreso", "horaEgreso", "tipoGuardia", "asistencial", "servicio", "efector" })
    private List<RegistroActividad> registrosActividades = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "asistenciales", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "asistenciales"})
    private List<TipoGuardia> tiposGuardias = new ArrayList<TipoGuardia>();

    
    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
    // "apellido", "dni", "cuil", "legajos",
    // "novedadesPersonales", "suplentes",
    // "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email",
    // "domicilio",
    // "estado", "activo", "autoridades", "tipoGuardia", "registrosActividades",
    // "descripcion" })
}
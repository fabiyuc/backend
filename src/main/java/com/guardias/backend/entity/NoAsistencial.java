package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "noAsistenciales")
/* @Table */
@Data
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
@AllArgsConstructor
@NoArgsConstructor
public class NoAsistencial extends Person {

    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
    // "apellido", "dni", "cuil", "legajos",
    // "novedadesPersonales", "suplentes",
    // "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email",
    // "domicilio",
    // "estado", "activo", "autoridades", "tipoGuardia", "registrosActividades",
    // "descripcion" })
}
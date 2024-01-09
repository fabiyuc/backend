package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "distribucionesConsultorios")
@Data
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionConsultorio extends DistribucionHoraria {
    private String tipoConsultorio; // TODO debo hacer un enum con los tipos de consultorios?
    private int turno;
}

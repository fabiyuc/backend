package com.guardias.backend.entity;

import com.guardias.backend.enums.TipoGuardiaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "distribucionesGuardias")
@Data
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionGuardia extends DistribucionHoraria {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    private TipoGuardiaEnum tipoGuardia;
}

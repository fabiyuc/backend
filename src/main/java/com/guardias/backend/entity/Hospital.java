package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospitales")
// @DiscriminatorValue("HOSPITAL")
// @PrimaryKeyJoinColumn(name = "id_efector")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hospital extends Efector {

    private boolean esCabecera;
    private int nivelComplejidad;

}

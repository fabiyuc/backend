package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caps")
// @DiscriminatorValue("CAPS")
// @PrimaryKeyJoinColumn(name = "id_efector")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Caps extends Efector {

    private Long idUdo;

    @Column(name = "tipo_caps", columnDefinition = "VARCHAR(25)")
    private String tipoCaps;

}

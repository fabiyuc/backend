package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caps")
// @DiscriminatorValue("CAPS")
// @PrimaryKeyJoinColumn(name = "id_efector")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Caps extends Efector {

    private Long idUdo;

    @Column(name = "tipo_caps", columnDefinition = "VARCHAR(25)")
    private String tipoCaps;

}

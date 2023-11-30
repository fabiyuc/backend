package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caps")
@DiscriminatorValue("CAPS")
@PrimaryKeyJoinColumn(name = "id_efector")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Caps extends Efector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUdo;

    @Column(name = "tipo_caps", columnDefinition = "VARCHAR(25)")
    private String tipoCaps;

}

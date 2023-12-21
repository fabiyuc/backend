package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tiposLicencias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoLicencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(10)")
    private String ley;
    @Column(columnDefinition = "VARCHAR(10)")
    private String articulo;
    @Column(columnDefinition = "VARCHAR(10)")
    private String inciso;

    @OneToOne
    @JoinColumn(name = "id_novedad_personal")
    private NovedadPersonal novedadPersonal;
}
package com.guardias.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "valoresGuardiasExtraYcf")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ValorGuardiaExtrayCF extends ValorGuardiaBase {

    @Column(precision = 20, scale = 2)
    private BigDecimal resolucion2575Lav; // para extra y CF

    @Column(precision = 20, scale = 2)
    private BigDecimal resolucion2575Sdf;
}

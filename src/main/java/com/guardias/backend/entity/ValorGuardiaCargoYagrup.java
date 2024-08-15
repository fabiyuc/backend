package com.guardias.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "valoresGuardiasCargosYagrup")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ValorGuardiaCargoYagrup extends ValorGuardiaBase {
    
    @Column(precision = 20, scale = 2)
    private BigDecimal decreto1178Lav; // para cargo y agrup 
    
    @Column(precision = 20, scale = 2)
    private BigDecimal decreto1178Sdf; 
    
    @Column(precision = 20, scale = 2)
    private BigDecimal decreto1657Lav; // para cargo y agrup

    @Column(precision = 20, scale = 2)
    private BigDecimal decreto1657Sdf; 
}

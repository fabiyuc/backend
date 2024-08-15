package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ValoresGuardias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorGuardia {
 
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    private int nivelComplejidad;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(precision = 20, scale = 2)
    private BigDecimal decreto1178Lav; // para cargo y agrup 
    
    @Column(precision = 20, scale = 2)
    private BigDecimal decreto1178Sdf; 
    
    @Column(precision = 20, scale = 2)
    private BigDecimal decreto1657Lav; // para cargo y agrup

    @Column(precision = 20, scale = 2)
    private BigDecimal decreto1657Sdf; 
    
    @Column(precision = 20, scale = 2)
    private BigDecimal resolucion2575Lav; // para extra y CF

    @Column(precision = 20, scale = 2)
    private BigDecimal resolucion2575Sdf; 
    
    @Column(precision = 20, scale = 2)
    private BigDecimal total; 

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValorGuardia other = (ValorGuardia) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}

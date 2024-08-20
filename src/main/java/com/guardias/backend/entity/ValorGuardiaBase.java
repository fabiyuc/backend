package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "valor_guardia_sequence", sequenceName = "valor_guardia_sequence", allocationSize = 1)

public abstract class ValorGuardiaBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "valor_guardia_sequence")
    private Long id;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private TipoGuardiaEnum tipoGuardia;

    private int nivelComplejidad;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(precision = 20, scale = 2)
    private BigDecimal total;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_valor_gmi")
     @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private ValorGmi valorGmi;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_bono_uti")
     @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private BonoUti bonoUti;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValorGuardiaBase other = (ValorGuardiaBase) obj;
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

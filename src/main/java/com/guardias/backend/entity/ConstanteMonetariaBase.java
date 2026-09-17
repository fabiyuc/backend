package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.FamiliaValorBaseEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ConstantesMonetariasBase")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstanteMonetariaBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(precision = 20, scale = 2)
    private BigDecimal monto;

    //private TipoGuardiaEnum tipoGuardia;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(30)")
    private FamiliaValorBaseEnum familiaValorBase;

    private String documentoLegal;

    /* @OneToMany(fetch = FetchType.LAZY, mappedBy = "valorGmi", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "anio", "subtotal",
            "total", "estadoDdjj", "valorGmi",  })
    private List<Ddjj> ddjjs = new ArrayList<>(); */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "constanteMonetariaBase", cascade = CascadeType.ALL )
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
            "constanteMonetariaBase" ,"activo","fechaInicio","fechaFin","bonoUti","decreto1178Lav","decreto1178Sdf","decreto1657Lav","decreto1657Sdf"})
    private List<ValorGuardiaBase> valoresGuardias = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConstanteMonetariaBase other = (ConstanteMonetariaBase) obj;
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

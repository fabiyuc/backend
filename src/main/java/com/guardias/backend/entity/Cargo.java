package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.AgrupacionEnum;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cargos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
    @Column(columnDefinition = "VARCHAR(10)")
    private String nroresolucion;
    @Column(columnDefinition = "VARCHAR(10)")
    private String nrodecreto;

    /*
     * @Column(columnDefinition = "BIT DEFAULT 1")
     * private Boolean activo;
     */

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    /*
     * @JsonFormat(pattern = "dd-MM-yyyy")
     * 
     * @Column(columnDefinition = "DATE")
     */
    
    @Temporal(TemporalType.DATE)
    private LocalDate fechaResolucion;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cargo", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio", "fechaFinal", "actual", "legal",
            "activo", "matriculaNacional", "matriculaProvincial", "especialidades", "suspencion", "revista", "udo",
            "persona", "cargo", "efectores" })
    private List<Legajo> legajos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(40)")
    private AgrupacionEnum agrupacion;

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
    // "nombre","descripcion","nroresolucion","nrodecreto","activo","fechaResolucion","fechaInicio","fechaFinal","legajos","agrupacion"})

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cargo other = (Cargo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (nroresolucion == null) {
            if (other.nroresolucion != null)
                return false;
        } else if (!nroresolucion.equals(other.nroresolucion))
            return false;
        if (nrodecreto == null) {
            if (other.nrodecreto != null)
                return false;
        } else if (!nrodecreto.equals(other.nrodecreto))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((nroresolucion == null) ? 0 : nroresolucion.hashCode());
        result = prime * result + ((nrodecreto == null) ? 0 : nrodecreto.hashCode());
        return result;
    }

}

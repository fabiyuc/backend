package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.EstadoLey;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "ley_sequence", sequenceName = "ley_sequence", allocationSize = 1)
public abstract class Ley {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ley_sequence")
    private Long id;
    @Column(columnDefinition = "VARCHAR(20)")
    private String numero;
    @Column(columnDefinition = "VARCHAR(80)")
    private String denominacion;
    @Column(columnDefinition = "VARCHAR(2000)")
    private String detalle;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    private EstadoLey estado;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaAlta;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaBaja;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaModificacion;
    @Column(columnDefinition = "VARCHAR(80)")
    private String motivoModificacion;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ley", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<NovedadPersonal> novedadesPersonales;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_tipo_ley")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "leyes" })
    private TipoLey tipoLey;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ley other = (Ley) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (numero == null) {
            if (other.numero != null)
                return false;
        } else if (!numero.equals(other.numero))
            return false;
        if (denominacion == null) {
            if (other.denominacion != null)
                return false;
        } else if (!denominacion.equals(other.denominacion))
            return false;
        if (detalle == null) {
            if (other.detalle != null)
                return false;
        } else if (!detalle.equals(other.detalle))
            return false;
        if (fechaAlta == null) {
            if (other.fechaAlta != null)
                return false;
        } else if (!fechaAlta.equals(other.fechaAlta))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
        result = prime * result + ((denominacion == null) ? 0 : denominacion.hashCode());
        result = prime * result + ((detalle == null) ? 0 : detalle.hashCode());
        result = prime * result + ((fechaAlta == null) ? 0 : fechaAlta.hashCode());
        return result;
    }

}

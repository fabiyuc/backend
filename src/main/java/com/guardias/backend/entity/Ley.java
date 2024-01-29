package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.EstadoLey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_ley")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "leyes" })
    TipoLey ley;

}

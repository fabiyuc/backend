package com.guardias.backend.entity;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "servicios")
@Data
@AllArgsConstructor
// @RequiredArgsConstructor
@NoArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    @Column(columnDefinition = "VARCHAR(20)")
    private int nivel;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicio", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "servicio" })
    private Set<RegistroActividad> registrosActividades;
}

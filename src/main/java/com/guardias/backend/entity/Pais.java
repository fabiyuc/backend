package com.guardias.backend.entity;

import java.util.List;
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

@Entity(name = "paises")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nacionalidad;

    @Column(columnDefinition = "VARCHAR(5)")
    private String codigo;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pais", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "pais" })
    List<Provincia> provincias;

}
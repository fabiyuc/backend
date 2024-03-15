package com.guardias.backend.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "regiones")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
    generator= ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class Region implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(15)")
    private String nombre;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    
    @JsonManagedReference
    @OneToMany( mappedBy = "region")
    /* @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "region", "localidad", "distribucionesHorarias", "legajosUdo","autoridades","legajos", "notificaciones" }) */
    //@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "region"})
    //@JsonIgnore
    
    //@JsonIgnoreProperties("efectores")
    //@JsonIgnore
    
    private List<Efector> efectoresSet = new ArrayList<Efector>();

      

}

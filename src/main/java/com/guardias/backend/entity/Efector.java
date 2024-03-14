package com.guardias.backend.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "efector_sequence", sequenceName = "efector_sequence", allocationSize = 1)
@JsonIdentityInfo(
    generator= ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public abstract class Efector implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "efector_sequence")
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(80)")
    private String domicilio;
    @Column(columnDefinition = "VARCHAR(15)")
    private String telefono;
    private boolean estado;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    private String observacion;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_region")
    /* @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "efectores"}) */
    
    private Region region;

    @ManyToOne
    @JoinColumn(name = "id_localidad")
    private Localidad localidad;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    //@JsonIgnoreProperties({ "efector" })
    @JsonIgnore
    private Set<DistribucionHoraria> distribucionesHorarias;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "udo", cascade = CascadeType.ALL)
    //@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "udo" })
    @JsonIgnore
    private Set<Legajo> legajosUdo = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "efectores")
    //@JsonIgnoreProperties({ "efectores" })
    @JsonIgnore
    private Set<Legajo> legajos;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "efectores", cascade = CascadeType.ALL)
    //@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "efectores" })
    @JsonIgnore
    private Set<Notificacion> notificaciones;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    //@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "efector" })
    @JsonIgnore
    private Set<Autoridad> autoridades;

    /* por alguna razon no estaba la siguiente relacion? */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    //@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "efector" })
    //@JsonManagedReference
    @JsonIgnore
    private Set<RegistroActividad> registrosActividades;

    

}

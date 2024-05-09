package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
@SequenceGenerator(name = "person_sequence", sequenceName = "person_sequence", allocationSize = 1)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_sequence")
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(30)")
    private String apellido;
    private int dni;
    @Column(columnDefinition = "VARCHAR(15)")
    private String cuil;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaNacimiento;
    @Column(columnDefinition = "VARCHAR(15)")
    private String sexo;
    @Column(columnDefinition = "VARCHAR(15)")
    private String telefono;
    @Column(columnDefinition = "VARCHAR(25)")
    private String email;
    @Column(columnDefinition = "VARCHAR(50)")
    private String domicilio;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean esAsistencial;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio", "fechaFinal", "legal",
            "activo", "matriculaNacional", "matriculaProvincial", "profesion", "suspencion", "udo",
            "persona", "cargo", "efectores" })
    private List<Legajo> legajos = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer",
            "handler",  "puedeRealizarGuardia", "cobraSueldo", "necesitaReemplazo",
            "persona", "suplente", "ley", "articulo", "inciso",
            "activo" })
    private List<NovedadPersonal> novedadesPersonales = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "suplente", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer",
            "handler", "fechaInicio", "fechaFinal", "puedeRealizarGuardia", "cobraSueldo", "necesitaReemplazo",
            "actual", "descripcion", "persona", "suplente", "ley", "articulo", "inciso",
            "activo" })
    private List<NovedadPersonal> suplentes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dia", "fechaInicio", "fechaFinalizacion",
            "horaIngreso", "cantidadHoras", "activo", "efector", "persona", "lugar", "especialidad", "cantidadTurnos",
            "destino", "descripcion", "tipoGuardia" })
    private List<DistribucionHoraria> distribucionesHorarias = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "fechaInicio", "fechaFinal", "esActual",
            "esRegional", "activo", "efector", "persona" })
    private List<Autoridad> autoridades = new ArrayList<>();

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
    // "apellido", "dni", "cuil", "legajos",
    // "novedadesPersonales", "suplentes",
    // "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email",
    // "domicilio",
    // "estado", "activo", "autoridades", "tipoGuardia", "registrosActividades",
    // "descripcion","esAsistencial" })

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (dni != other.dni)
            return false;
        if (cuil == null) {
            if (other.cuil != null)
                return false;
        } else if (!cuil.equals(other.cuil))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + dni;
        result = prime * result + ((cuil == null) ? 0 : cuil.hashCode());
        return result;
    }

}

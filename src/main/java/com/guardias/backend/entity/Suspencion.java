package com.guardias.backend.entity;

//import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "suspenciones")
@Data
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Suspencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
    //@Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;
    //@Temporal(TemporalType.DATE)
    private LocalDate fechaFin;

    @OneToMany(mappedBy = "suspencion")
    private Set<Legajo> legajos;
}

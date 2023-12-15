package com.guardias.backend.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "legajos")
@Data
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Legajo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  //@Temporal(TemporalType.DATE)
  private LocalDate fechaInicio;
  
  //@Temporal(TemporalType.DATE)
  private LocalDate fechaFinal;
 
  private Boolean actual;
  private Boolean legal;
 
  @Column(columnDefinition = "VARCHAR(10)")
  private String matriculaNacional;
  
  @Column(columnDefinition = "VARCHAR(10)")
  private String matriculaProvincial;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_profesion")
  private Profesion profesion;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_suspencion")
  private Suspencion suspencion;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_revista")
  private Revista revista;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_asistencial")
  private Asistencial asistencial;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_noAsistencial")
  private NoAsistencial noAsistencial;

}

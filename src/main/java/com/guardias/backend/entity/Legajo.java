package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "legajos")
@Data
// @RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Legajo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // @Temporal(TemporalType.DATE)
  private LocalDate fechaInicio;

  // @Temporal(TemporalType.DATE)
  private LocalDate fechaFinal;

  private Boolean actual;
  private Boolean legal;

  @Column(columnDefinition = "VARCHAR(10)")
  private String matriculaNacional;

  @Column(columnDefinition = "VARCHAR(10)")
  private String matriculaProvincial;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_profesion")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "legajos", "especialidades" })
  private Profesion profesion;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_suspencion")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "legajos" })
  private Suspencion suspencion;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_revista")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "legajos" })
  private Revista revista;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_udo")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "legajosUdo", "domicilio", "telefono", "estado",
      "observacion", "region", "localidad", "esCabecera", "admitePasiva", "caps" })
  private Efector udo;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_persona")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "legajos", "registrosActividades" })
  private Person persona;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "legajo_efector", joinColumns = @JoinColumn(name = "id_legajo"), inverseJoinColumns = @JoinColumn(name = "id_efector"))
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "legajos", "domicilio", "telefono", "estado",
      "observacion", "region", "localidad", "esCabecera", "admitePasiva", "caps" })
  private Set<Efector> efectores = new HashSet<>();

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Legajo other = (Legajo) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

}

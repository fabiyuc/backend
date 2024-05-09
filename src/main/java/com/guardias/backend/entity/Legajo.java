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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "legajos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Legajo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDate fechaInicio;
  private LocalDate fechaFinal;
  private Boolean actual;
  private Boolean legal;
  @Column(columnDefinition = "BIT DEFAULT 1")
  private boolean activo;

  @Column(columnDefinition = "VARCHAR(10)")
  private String matriculaNacional;

  @Column(columnDefinition = "VARCHAR(10)")
  private String matriculaProvincial;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_profesion")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "asistencial", "especialidades",
      "legajos" })
  private Profesion profesion;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_suspencion")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "descripcion", "fechaInicio", "activo", "fechaFin",
      "legajos" })
  private Suspencion suspencion;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_revista")
  @JsonIgnoreProperties({ "hibernateLazyInitializer",
      "legajos", "agrupacion", "activo" })
  private Revista revista;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_udo")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
      "autoridades", "domicilio", "telefono",
      "estado", "activo",
      "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo",
      "legajos",
      "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
      "areaProgramatica",
      "tipoCaps", "nivelComplejidad", "cabecera", "ministerios", "registroActividad" })
  private Efector udo;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_persona")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dni", "cuil", "fechaNacimiento", "sexo", "telefono", "email", "domicilio", "esAsistencial", "activo", "legajos", "novedadesPersonales", "suplentes", "distribucionesHorarias", "autoridades", "tiposGuardias", "registrosActividades", "descripcion"})
  private Person persona;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_cargo")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "descripcion", "nroresolucion", "nrodecreto",
      "activo", "fechaResolucion", "fechaInicio", "fechaFinal", "legajos", "agrupacion" })
  private Cargo cargo;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "legajo_efector", joinColumns = @JoinColumn(name = "id_legajo"), inverseJoinColumns = @JoinColumn(name = "id_efector"))
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
      "autoridades", "domicilio", "telefono",
      "estado", "activo",
      "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo",
      "legajos",
      "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
      "areaProgramatica",
      "tipoCaps", "nivelComplejidad", "cabecera", "ministerios", "registroActividad" })
  private List<Efector> efectores = new ArrayList<>();

  // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio",
  // "fechaFinal", "actual", "legal", "activo", "matriculaNacional",
  // "matriculaProvincial", "profesion", "suspencion", "revista", "udo","persona",
  // "cargo", "efectores" })

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

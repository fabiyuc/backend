package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.AgrupacionEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "revistas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Revista {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(columnDefinition = "BIT DEFAULT 1")
  private boolean activo;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_tipo_revista")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "revistas", "activo" })
  private TipoRevista tipoRevista;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_categoria")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "activo", "revistas" })
  private Categoria categoria;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_adicional")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "activo", "revistas" })
  private Adicional adicional;

  @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "id_carga_horaria")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "cantidad", "descripcion", "activo", "revistas" })
  private CargaHoraria cargaHoraria;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "revista", cascade = CascadeType.ALL)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio", "fechaFinal", "actual", "legal",
      "activo", "matriculaNacional", "matriculaProvincial", "profesion", "suspencion", "revista", "udo", "persona",
      "cargo", "efectores" })
  private List<Legajo> legajos = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(40)")
  private AgrupacionEnum agrupacion;

  /*
   * @JsonIgnoreProperties({ "hibernateLazyInitializer", "tipoRevista",
   * "categoria", "adicional", "cargaHoraria",
   * "legajos", "agrupacion", "activo" })
   */

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Revista other = (Revista) obj;
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

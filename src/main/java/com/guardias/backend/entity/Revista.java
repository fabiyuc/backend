package com.guardias.backend.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
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

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_tipo_revista")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "revistas" })
  private TipoRevista tipoRevista;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_categoria")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "revistas" })
  private Categoria categoria;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_adicional")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "revistas" })
  private Adicional adicional;

  @ManyToOne(optional = true)
  @JoinColumn(name = "id_carga_horaria")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "revistas" })
  private CargaHoraria cargaHoraria;

  @OneToMany(mappedBy = "revista")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "revistas" })
  private Set<Legajo> legajos;
}

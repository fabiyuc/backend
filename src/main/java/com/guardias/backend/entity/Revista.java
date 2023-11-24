package com.guardias.backend.entity;

import java.util.Set;

import com.guardias.backend.modelo.RegistroActividad;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Revista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_tipo_revista")
    private TipoRevista tipoRevista;

    

  /*  @ManyToOne(optional = true)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria; */
 
    /* @ManyToOne(optional = true)
    @JoinColumn(name = "id_adicional")
    private Adicional adicional;
*/

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_carga_horaria")
    private CargaHoraria cargaHoraria;


    @OneToMany(mappedBy = "revista")
    private Set<Legajo> legajos;

    public Revista(TipoRevista tipoRevista, CargaHoraria cargaHoraria, Set<Legajo> legajos) {
      this.tipoRevista = tipoRevista;
      this.cargaHoraria = cargaHoraria;
      this.legajos = legajos;
    }
    
}

package com.guardias.backend.entity;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tiposLicencias")
@Data
@AllArgsConstructor
@NoArgsConstructor

/*
 * TODO debe ser reemplazado por las clases Articulo e Inciso
 */
public class TipoLicencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(10)")
    private String ley;
    @Column(columnDefinition = "VARCHAR(10)")
    private String articulo;
    @Column(columnDefinition = "VARCHAR(10)")
    private String inciso;

    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoLicencia", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<NovedadPersonal> novedadesPersonales;
   
    /* @OneToOne
    @JoinColumn(name = "id_novedad_personal")
    private NovedadPersonal novedadPersonal; */
}

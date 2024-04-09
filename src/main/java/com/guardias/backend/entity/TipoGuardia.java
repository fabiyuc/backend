package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tiposGuardias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoGuardia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tipoguardia_asistencial", joinColumns = @JoinColumn(name = "id_tipoGuardia"), inverseJoinColumns = @JoinColumn(name = "id_asistencial"))
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "tiposGuardias" })
    private List<Asistencial> asistenciales = new ArrayList<Asistencial>();
    
    /*
     * @OneToMany(mappedBy = "tipoGuardia")
     * 
     * @JsonIgnore // Añade esta anotación para evitar la recursión infinita
     * private Set<RegistroActividad> registroActividades;
     */
    /*
     * @OneToMany(mappedBy = "tipoGuardia", cascade = CascadeType.ALL)
     * private Set<Asistencial> asistenciales;
     */
    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "descripcion",
    // "nombre", "activo" })

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TipoGuardia other = (TipoGuardia) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        return result;
    }

}

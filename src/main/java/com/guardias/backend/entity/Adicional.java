package com.guardias.backend.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Adicional {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
/* 
    @OneToMany(mappedBy = "adicional")
    private Set<Revista> revistas; */

    public Adicional() {
    }

    public Adicional(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

   /*  public Set<Revista> getRevistas() {
        return revistas;
    }

    public void setRevistas(Set<Revista> revistas) {
        this.revistas = revistas;
    }
 */
    

}

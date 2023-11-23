package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity(name = "tiposGuardias")
@Data
@RequiredArgsConstructor
@AllArgsConstructor

public class TipoGuardia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoGuardia;
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "tipoGuardia")
    private Set<RegistroActividad> registroActividades;

    public TipoGuardia(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /*
     * public TipoGuardia() {
     * }
     * 
     * public TipoGuardia(int idTipoGuardia, String nombre, String descripcion) {
     * this.idTipoGuardia = idTipoGuardia;
     * this.nombre = nombre;
     * this.descripcion = descripcion;
     * }
     * 
     * 
     */
    // getters y setters

    /*
     * public int getIdTipoGuardia() {
     * return idTipoGuardia;
     * }
     * 
     * public void setIdTipoGuardia(int idTipoGuardia) {
     * this.idTipoGuardia = idTipoGuardia;
     * }
     * 
     * public String getNombre() {
     * return nombre;
     * }
     * 
     * public void setNombre(String nombre) {
     * this.nombre = nombre;
     * }
     * 
     * public String getDescripcion() {
     * return descripcion;
     * }
     * 
     * public void setDescripcion(String descripcion) {
     * this.descripcion = descripcion;
     * }
     */

}
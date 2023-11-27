package com.guardias.backend.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class CargoDto {

    @Null
    private Long id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
    @NotBlank
    private String nroresolucion;
    @NotBlank
    private String nrodecreto;
    @NotNull
    private LocalDate fecharesolucion;
    @NotNull
    private LocalDate fechainicio;
    @NotNull
    private LocalDate fechafinal;

    public CargoDto() {
    }

    public CargoDto(@Null Long id, @NotBlank String nombre, @NotBlank Long tipocargoid,
            @NotBlank String descripcion, @NotBlank String nroresolucion, @NotBlank String nrodecreto,
            @NotNull LocalDate fecharesolucion, @NotNull LocalDate fechainicio, @NotNull LocalDate fechafinal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nroresolucion = nroresolucion;
        this.nrodecreto = nrodecreto;
        this.fecharesolucion = fecharesolucion;
        this.fechainicio = fechainicio;
        this.fechafinal = fechafinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNroresolucion() {
        return nroresolucion;
    }

    public void setNroresolucion(String nroresolucion) {
        this.nroresolucion = nroresolucion;
    }

    public String getNrodecreto() {
        return nrodecreto;
    }

    public void setNrodecreto(String nrodecreto) {
        this.nrodecreto = nrodecreto;
    }

    public LocalDate getFecharesolucion() {
        return fecharesolucion;
    }

    public void setFecharesolucion(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.fecharesolucion = LocalDate.parse(fecha, formatter);
    }

    public LocalDate getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.fechainicio = LocalDate.parse(fecha, formatter);
    }

    public LocalDate getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.fechafinal = LocalDate.parse(fecha, formatter);
    }

}
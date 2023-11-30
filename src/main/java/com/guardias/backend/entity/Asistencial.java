package com.guardias.backend.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity(name = "asistenciales")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Asistencial extends Person {
    
    @OneToMany(mappedBy = "asistencial")
    private Set<Legajo> legajos;


   
}

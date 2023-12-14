package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "novedadesPersonales")
@Data
// @AllArgsConstructor
// @NoArgsConstructor
public class NovedadPersonal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}

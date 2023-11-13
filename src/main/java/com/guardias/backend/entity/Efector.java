package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "efectores")
public class Efector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

}

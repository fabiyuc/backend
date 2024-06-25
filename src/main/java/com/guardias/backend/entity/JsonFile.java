package com.guardias.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "JsonFiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "VARBINARY(MAX)")
    private byte[] contenido = new byte[0];

    private boolean activo = true;

    private LocalDateTime fechaGuardado;

    @OneToOne(mappedBy = "jsonFile")
    private PendientesSemanal pendientesSemanal;

    @OneToOne(mappedBy = "jsonFile")
    private RegistroMensual registroMensual;
}

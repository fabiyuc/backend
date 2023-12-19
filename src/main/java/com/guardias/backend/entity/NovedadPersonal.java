package com.guardias.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "novedadesPersonales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovedadPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    private Long idExtensionLicencia; // en caso que se extienda la licecncia, crea una nueva y se la asocia a la
                                      // anterior mediante este id

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_novedadesAsistencial")
    private Person novedadesAsistencial;

    // VER!!!
    // Person novedad = new Asistencial();
    // Person suplente = new NoAsistencial();

    // @ManyToOne(optional = true)
    // @JoinColumn(name = "id_asistencialReemplazante")
    // private Asistencial asistencialReemplazante;

    // @ManyToOne(optional = true)
    // @JoinColumn(name = "id_novedadesNoAistencial")
    // private NoAsistencial novedadesNoAsistencial;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_reemplazantesNoAsistencial")
    private Person reemplazantesNoAsistencial;

    @OneToOne(mappedBy = "novedadPersonal")
    private TipoLicencia tipoLicencia;

}

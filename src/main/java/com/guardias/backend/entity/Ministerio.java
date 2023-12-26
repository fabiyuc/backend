package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ministerios")
// @DiscriminatorValue("MINISTERIO")
// @PrimaryKeyJoinColumn(name = "id_efector")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Ministerio extends Efector {

    private Long idCabecera;

}

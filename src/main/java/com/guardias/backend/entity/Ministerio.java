package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ministerios")
// @DiscriminatorValue("MINISTERIO")
// @PrimaryKeyJoinColumn(name = "id_efector")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ministerio extends Efector {

    private Long idCabecera;

}

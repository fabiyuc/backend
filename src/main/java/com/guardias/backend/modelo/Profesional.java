package com.guardias.backend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
//@Table(name = "profesional")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Profesional extends Persona {

	private String matricula;
	
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	/* private Long id_profesional;

	public Long getIdProfesional() {
		return id_profesional;
	}

	public void setIdProfesional(Long id_profesional) {
		this.id_profesional = id_profesional;
	} */

//	@Column(name = "id_persona")
	/* private Long idPersona;

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	} */

	//@Column(name = "matricula")
	/* private String matricula;

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	} */

	//@Column(name = "id_tipo_guardia")
	/* private Long idTipoguardia;

	public Long getIdTipoguardia() {
		return idTipoguardia;
	}

	public void setIdTipoguardia(Long idTipoguardia) {
		this.idTipoguardia = idTipoguardia;
	}
 */
	//@Column(name = "id_distribucion_horaria")
	/* private Long idDistribucionHoraria;

	public Long getIdDistribucionHoraria() {
		return idDistribucionHoraria;
	}

	public void setIdDistribucionHoraria(Long idDistribucionHoraria) {
		this.idDistribucionHoraria = idDistribucionHoraria;
	} */

//	@Column(name = "id_especialidad")
	/* private Long idEspecialidad;

	public Long getIdEspecialidad() {
		return idEspecialidad;
	}

	public void setIdEspecialidad(Long idEspecialidad) {
		this.idEspecialidad = idEspecialidad;
	}

	public Profesional() {
	}

	public Profesional(Long id_profesional, Long idPersona, String matricula, Long idTipoguardia,
			Long idDistribucionHoraria, Long idEspecialidad) {
		this.id_profesional = id_profesional;
		this.idPersona = idPersona;
		this.matricula = matricula;
		this.idTipoguardia = idTipoguardia;
		this.idDistribucionHoraria = idDistribucionHoraria;
		this.idEspecialidad = idEspecialidad;
	} */

}
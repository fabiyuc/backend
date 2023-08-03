package com.guardias.backend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "profesional")
public class Profesional {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "idPersona")
	private Long idPersona;

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	@Column(name = "matricula")
	private String matricula;

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@Column(name = "idTipoguardia")
	private Long idTipoguardia;

	public Long getIdTipoguardia() {
		return idTipoguardia;
	}

	public void setIdTipoguardia(Long idTipoguardia) {
		this.idTipoguardia = idTipoguardia;
	}

	@Column(name = "idDistribucionHoraria")
	private Long idDistribucionHoraria;

	public Long getIdDistribucionHoraria() {
		return idDistribucionHoraria;
	}

	public void setIdDistribucionHoraria(Long idDistribucionHoraria) {
		this.idDistribucionHoraria = idDistribucionHoraria;
	}

	@Column(name = "idEspecialidad")
	private Long idEspecialidad;

	public Long getIdEspecialidad() {
		return idEspecialidad;
	}

	public void setIdEspecialidad(Long idEspecialidad) {
		this.idEspecialidad = idEspecialidad;
	}

	public Profesional() {
	}

	public Profesional(Long id, Long idPersona, String matricula, Long idTipoguardia,
			Long idDistribucionHoraria, Long idEspecialidad) {
		this.id = id;
		this.idPersona = idPersona;
		this.matricula = matricula;
		this.idTipoguardia = idTipoguardia;
		this.idDistribucionHoraria = idDistribucionHoraria;
		this.idEspecialidad = idEspecialidad;
	}

}

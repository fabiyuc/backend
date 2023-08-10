package com.guardias.backend.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.Profesional;

@Repository
public interface ProfesionalRepositorio extends JpaRepository<Profesional, Long> {

    @Query(value = "SELECT *from profesional pro where pro.id= :idProfesional", nativeQuery = true)
    // @Query(value = "SELECT *from persona where idPersona= :idProfesional",
    // nativeQuery = true)

    /*
     * @Query(value = "SELECT " +
     * "p.idPersona as idPerdona, p.nombre, p.apellido,p.dni,p.cuil,
     * p.sexo,p.direccion, p.telefono,p.email, " +
     * " p.idUdo, p.idLegajo, p.idUsuario, p.idHospital,p.idCargo,p.idProfesion, " +
     * "u.nombre, " +
     * "h.nombre, " +
     * "profesion.descripcion, " +
     * "e.descripcion, " +
     * "pro.matricula, " +
     * "tg.descripcion, " +
     * "ch.descripcion " +
     * "FROM profesional pro " +
     * "inner join persona p on pro.idPersona=p.idPersona " +
     * "inner join profesion on p.idProfesion=profesion.idProfesion " +
     * "inner join especialidad e on pro.idEspecialidad=e.isEspecialidad " +
     * "inner join tipoGuardia tg on pro.idTipoGuardia=tg.idTipoGuardia " +
     * "inner join cargaHoraria ch on pro.idDistribucionHoraria=ch.idCargaHoraria "
     * +
     * "inner join udo u on p.idUdo=u.idUdo " +
     * "inner join udo h on p.idHospital=h.idUdo where pro.id= :idProfesional"
     * ,nativeQuery = true)
     */
    List<Profesional> getProfesionalById(Long idProfesional);
}

package com.guardias.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ObservacionDdjj;

@Repository
public interface ObservacionDdjjRepository extends JpaRepository<ObservacionDdjj, Long> {
    
    List<ObservacionDdjj> findByActivoTrue();

    @Query("SELECT o FROM observacionesDdjj o JOIN FETCH o.usuario u JOIN FETCH u.person WHERE o.ddjj.id = :idDdjj AND o.tipoDph = :tipoDph AND o.activo = true ORDER BY o.id DESC")
    List<ObservacionDdjj> findUltimaObservacion(
    @Param("idDdjj") Long idDdjj, 
    @Param("tipoDph") Boolean tipoDph);

   /*  @Query("SELECT o FROM observacionesDdjj o " +
           "WHERE o.ddjj.id = :idDdjj " +
           "AND o.tipoDph = :tipoDph " +
           "AND o.activo = true" +
           "ORDER BY o.id DESC")
    List<ObservacionDdjj> findByDdjjIdAndTipoDphAndActivo(
        @Param("idDdjj") Long idDdjj, 
        @Param("tipoDph") Boolean tipoDph); */
}

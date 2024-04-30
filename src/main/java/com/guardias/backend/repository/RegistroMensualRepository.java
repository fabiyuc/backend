package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface RegistroMensualRepository extends JpaRepository<RegistroMensual, Long> {

        Optional<RegistroMensual> findById(Long id);

        @Query("SELECT rm FROM registrosMensuales rm WHERE rm.idAsistencial = :idAsistencial AND rm.efector.id = :idEfector AND rm.mes = :mes AND rm.anio = :anio")
        Optional<RegistroMensual> findByIdAsistencialAndMes(@Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector, @Param("mes") MesesEnum mes, @Param("anio") int anio);

        List<RegistroMensual> findByActivoTrue();

        boolean existsByMes(String mes);

        boolean existsByIdAsistencial(Long idAsistencial);

        @Query("SELECT id FROM registrosMensuales rm WHERE rm.idAsistencial = :idAsistencial AND rm.efector.id = :idEfector AND rm.mes = :mes AND rm.anio = :anio")
        Optional<Long> idByIdAsistencialAndMes(@Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector, @Param("mes") MesesEnum mes, @Param("anio") int anio);

        Optional<List<RegistroMensual>> findByMes(String mes);
}

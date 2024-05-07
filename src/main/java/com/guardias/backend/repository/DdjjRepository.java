package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.enums.MesesEnum;

@Repository
public interface DdjjRepository extends JpaRepository<Ddjj, Long> {

    List<Ddjj> findByActivoTrue();

    Optional<Ddjj> findById(Long id);

    boolean existsById(Long id);

    boolean existsByAnioAndMes(int anio, MesesEnum mes);

    boolean existsByAnio(int anio);

    List<Ddjj> findByAnio(int anio);

    List<Ddjj> findByAnioAndMes(int anio, MesesEnum mes);

    List<Ddjj> findByEfectorIdAndMesAndAnio(Long efectorId, MesesEnum mes, int anio);

}

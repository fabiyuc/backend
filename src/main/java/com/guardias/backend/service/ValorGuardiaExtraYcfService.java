package com.guardias.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.constants.GruposZonalesGuardia;
import com.guardias.backend.entity.BonoUti;
import com.guardias.backend.entity.ConstanteMonetariaBase;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.enums.FamiliaValorBaseEnum;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.repository.ValorGuardiaExtraYcfRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ValorGuardiaExtraYcfService {

    @Autowired
    ValorGuardiaExtraYcfRepository valorGuardiaExtraYcfRepository;
    @Autowired
    HospitalRepository hospitalRepository;
    @Autowired
    BonoUtiService bonoUtiService;
    @Autowired
    ConstanteMonetariaBaseService constanteMonetariaBaseService;

    public Optional<List<ValorGuardiaExtrayCF>> findByActivoTrue() {
        return valorGuardiaExtraYcfRepository.findByActivoTrue();
    }

    public List<ValorGuardiaExtrayCF> findAll() {
        return valorGuardiaExtraYcfRepository.findAll();
    }

    public Optional<ValorGuardiaExtrayCF> findById(Long id) {
        return valorGuardiaExtraYcfRepository.findById(id);
    }

    /*
     * public Optional<ValorGuardiaExtrayCF> buscarPorIdEfector(Long idEfector) {
     * return valorGuardiaExtraYcfRepository.buscarPorIdEfector(idEfector);
     * }
     */

    public boolean existsById(Long id) {
        return valorGuardiaExtraYcfRepository.existsById(id);
    }

    public void save(ValorGuardiaExtrayCF valorGuardiaCargoYagrup) {
        valorGuardiaExtraYcfRepository.save(valorGuardiaCargoYagrup);
    }

    public void deleteById(Long id) {
        valorGuardiaExtraYcfRepository.deleteById(id);
    }

    public boolean activo(Long id) {
        return (valorGuardiaExtraYcfRepository.existsById(id)
                && valorGuardiaExtraYcfRepository.findById(id).get().isActivo());
    }

    public Optional<ValorGuardiaExtrayCF> obtenerValorGuardiaExtraPorHospital(Hospital hospital,
            boolean esServicioCritico, LocalDate fecha) {

        if (esServicioCritico) {
            return valorGuardiaExtraYcfRepository.findServicioCriticoVigente(fecha);
        }

        Optional<ValorGuardiaExtrayCF> especifico = valorGuardiaExtraYcfRepository
                .findEspecificoPorHospitalVigente(hospital.getId(), fecha);
        if (especifico.isPresent()) {
            return especifico;
        }

        return valorGuardiaExtraYcfRepository.findGenericoVigente(fecha);
    }

    /*
     * public Optional<ValorGuardiaExtrayCF>
     * obtenerValorGuardiaExtraPorHospital(Long idHospital) {
     * // Verificar existencia del hospital
     * 
     * if (!hospitalRepository.existsById(idHospital))
     * return Optional.empty();
     * 
     * // 1. Buscar valor específico para el hospital
     * Optional<ValorGuardiaExtrayCF> valorEspecifico =
     * valorGuardiaExtraYcfRepository
     * .findByHospitalesIdAndActivoTrue(idHospital);
     * 
     * if (valorEspecifico.isPresent()) {
     * return valorEspecifico;
     * }
     * 
     * // 2. Buscar valor genérico (sin hospitales asignados)
     * return valorGuardiaExtraYcfRepository.findByActivoTrueAndHospitalesIsEmpty();
     * }
     */

    public List<ValorGuardiaExtrayCF> generarValoresExtraCF(LocalDate fecha) {

        ConstanteMonetariaBase baseExtraCf = constanteMonetariaBaseService
                .obtenerVigente(FamiliaValorBaseEnum.EXTRA_CONTRAFACTURA, fecha)
                .orElseThrow(() -> new RuntimeException(
                        "No hay ConstanteMonetariaBase EXTRA_CONTRAFACTURA vigente para " + fecha));

        BonoUti bonoUti = bonoUtiService.obtenerVigente(fecha)
                .orElseThrow(() -> new RuntimeException("No hay BonoUti vigente para " + fecha));

        BigDecimal baseLav = baseExtraCf.getMonto();

        List<ValorGuardiaExtrayCF> generados = new ArrayList<>();

        // Nivel 4 (convención) - Servicios Críticos + SAME: base + Bono UTI, sin
        // hospitales (se resuelve por Servicio.critico)
        generados.add(construirFilaExtra(baseLav, bonoUti.getMonto(), List.of(), true, baseExtraCf, bonoUti, fecha));

        // Nivel 1 (convención) - Resto, sin premio de zona, sin hospitales
        generados.add(construirFilaExtra(baseLav, null, List.of(), false, baseExtraCf, null, fecha));

        // Zona +20%
        List<Hospital> zona20 = hospitalRepository.findByNombreIn(GruposZonalesGuardia.ZONA_20_EXTRA_CF);
        if (zona20.size() != GruposZonalesGuardia.ZONA_20_EXTRA_CF.size()) {
            throw new RuntimeException("Faltan hospitales del grupo +20%; encontrados: "
                    + zona20.stream().map(Hospital::getNombre).toList());
        }
        BigDecimal zona20Lav = baseLav.multiply(new BigDecimal("1.20"));
        generados.add(construirFilaExtra(zona20Lav, null, zona20, false, baseExtraCf, null, fecha));

        // Uro +30%
        Hospital uro = hospitalRepository.findByNombre(GruposZonalesGuardia.HOSPITAL_URO)
                .orElseThrow(() -> new RuntimeException("Falta cargar hospital: " + GruposZonalesGuardia.HOSPITAL_URO));
        BigDecimal uroLav = baseLav.multiply(new BigDecimal("1.30"));
        generados.add(construirFilaExtra(uroLav, null, List.of(uro), false, baseExtraCf, null, fecha));

        // Susques +40%
        Hospital susques = hospitalRepository.findByNombre(GruposZonalesGuardia.HOSPITAL_SUSQUES)
                .orElseThrow(
                        () -> new RuntimeException("Falta cargar hospital: " + GruposZonalesGuardia.HOSPITAL_SUSQUES));
        BigDecimal susquesLav = baseLav.multiply(new BigDecimal("1.40"));
        generados.add(construirFilaExtra(susquesLav, null, List.of(susques), false, baseExtraCf, null, fecha));

        // Cierra la grilla vigente anterior antes de guardar la nueva
        cerrarVigenciaAnterior(fecha);

        return valorGuardiaExtraYcfRepository.saveAll(generados);
    }

    private ValorGuardiaExtrayCF construirFilaExtra(BigDecimal montoLav, BigDecimal bonoUtiLav,
            List<Hospital> hospitales, boolean esServicioCritico, ConstanteMonetariaBase baseExtraCf, BonoUti bonoUti,
            LocalDate fecha) {

        ValorGuardiaExtrayCF fila = new ValorGuardiaExtrayCF();
        fila.setFamiliaValorBase(FamiliaValorBaseEnum.EXTRA_CONTRAFACTURA);
        fila.setNivelComplejidad(0); // no aplica en Extra/CF, se deja fijo
        fila.setEsServicioCritico(esServicioCritico);
        fila.setHospitales(hospitales);
        fila.setFechaInicio(fecha);
        fila.setActivo(true);
        fila.setConstanteMonetariaBase(baseExtraCf);
        fila.setBonoUti(bonoUti);

       fila.setConstanteMonetariaBase(baseExtraCf);
        fila.setBonoUti(bonoUti);

        BigDecimal recargoSdf = new BigDecimal("1.10");

        // Redondeo único, al final, a partir del valor exacto
        BigDecimal resolucionLav = montoLav.setScale(2, RoundingMode.HALF_UP);
        BigDecimal resolucionSdf = montoLav.multiply(recargoSdf).setScale(2, RoundingMode.HALF_UP);
        fila.setResolucion2575Lav(resolucionLav);
        fila.setResolucion2575Sdf(resolucionSdf);

        // El TOTAL es la suma de las filas mostradas, para que la tabla siempre cierre
        BigDecimal totalLav = resolucionLav;
        BigDecimal totalSdf = resolucionSdf;

        if (bonoUtiLav != null) {
            BigDecimal bonoLav = bonoUtiLav.setScale(2, RoundingMode.HALF_UP);
            BigDecimal bonoSdf = bonoUtiLav.multiply(recargoSdf).setScale(2, RoundingMode.HALF_UP);
            fila.setBono1580Lav(bonoLav);
            fila.setBono1580Sdf(bonoSdf);
            totalLav = totalLav.add(bonoLav);
            totalSdf = totalSdf.add(bonoSdf);
        }

        fila.setTotalLav(totalLav);
        fila.setTotalSdf(totalSdf);

        return fila;
    }

    private void cerrarVigenciaAnterior(LocalDate fechaNueva) {
        List<ValorGuardiaExtrayCF> abiertos = valorGuardiaExtraYcfRepository.findByActivoTrueAndFechaFinIsNull();

        for (ValorGuardiaExtrayCF v : abiertos) {
            if (!v.getFechaInicio().isBefore(fechaNueva)) {
                throw new RuntimeException("Ya existen valores de Extra/CF vigentes desde "
                        + v.getFechaInicio() + ". La fecha de la nueva generación debe ser posterior.");
            }
        }

        abiertos.forEach(v -> v.setFechaFin(fechaNueva.minusDays(1)));
        valorGuardiaExtraYcfRepository.saveAll(abiertos);
    }

}

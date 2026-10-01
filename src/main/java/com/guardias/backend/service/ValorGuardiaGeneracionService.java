package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.repository.BonoUtiRepository;
import com.guardias.backend.repository.ConstanteMonetariaBaseRepository;
import com.guardias.backend.repository.ValorGuardiaCargoYagrupRepository;

import jakarta.transaction.Transactional;

@Service
public class ValorGuardiaGeneracionService {
    
    @Autowired
    ValorGuardiaCargoYagrupService valorGuardiaCargoYagrupService;
    @Autowired
    ValorGuardiaExtraYcfService valorGuardiaExtraYcfService;
    @Autowired
    ConstanteMonetariaBaseRepository constanteMonetariaBaseRepository;
    @Autowired
    BonoUtiRepository bonoUtiRepository;    
    @Autowired
    ValorGuardiaCargoYagrupRepository valorGuardiaCargoYagrupRepository;

    /**
     * Fechas en las que empieza alguna constante (GMI, Base Extra/CF o Bono UTI),
     * de hoy en adelante, que todavía no tienen valores de guardia generados.
     * Ordenadas de la más antigua a la más nueva.
     */
    public List<LocalDate> obtenerVigenciasPendientes() {
        LocalDate hoy = LocalDate.now();

        TreeSet<LocalDate> fechas = new TreeSet<>();   // ordena y elimina repetidas
        fechas.addAll(constanteMonetariaBaseRepository.fechasInicioDesde(hoy));
        fechas.addAll(bonoUtiRepository.fechasInicioDesde(hoy));

        fechas.removeIf(f -> valorGuardiaCargoYagrupRepository.existsByActivoTrueAndFechaInicio(f));

        return new ArrayList<>(fechas);
    }

    @Transactional
    public void generarTodosLosValores(LocalDate fecha) {
        List<LocalDate> pendientes = obtenerVigenciasPendientes();

        if (!pendientes.contains(fecha)) {
            throw new RuntimeException("La fecha " + fecha + " no es una vigencia pendiente de generar. "
                    + "Solo se pueden generar fechas de hoy en adelante en las que empiece un valor base cargado.");
        }
        if (!pendientes.get(0).equals(fecha)) {
            throw new RuntimeException("Primero debe generar la vigencia " + pendientes.get(0) + ".");
        }
        valorGuardiaCargoYagrupService.generarValoresCargoAgrupacion(fecha);
        valorGuardiaExtraYcfService.generarValoresExtraCF(fecha);
    }

    
}

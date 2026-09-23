package com.guardias.backend.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ValorGuardiaGeneracionService {
    
    @Autowired
    ValorGuardiaCargoYagrupService valorGuardiaCargoYagrupService;

    @Autowired
    ValorGuardiaExtraYcfService valorGuardiaExtraYcfService;

    @Transactional
    public void generarTodosLosValores(LocalDate fecha) {
        valorGuardiaCargoYagrupService.generarValoresCargoAgrupacion(fecha);
        valorGuardiaExtraYcfService.generarValoresExtraCF(fecha);
    }
}

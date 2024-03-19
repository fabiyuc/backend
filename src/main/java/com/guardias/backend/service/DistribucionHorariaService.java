package com.guardias.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.DistribucionHoraria;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionHorariaService {

    @Autowired
    DistribucionConsultorioService distribucionConsultorioService;
    @Autowired
    DistribucionGiraService distribucionGiraService;
    @Autowired
    DistribucionGuardiaService distribucionGuardiaService;
    @Autowired
    DistribucionOtraService distribucionOtraService;

    public DistribucionHoraria findById(Long id) {
        DistribucionHoraria distribucionHoraria = distribucionConsultorioService.findById(id).orElse(null);

        if (distribucionHoraria == null)
            distribucionHoraria = distribucionGiraService.findById(id).orElse(null);

        if (distribucionHoraria == null)
            distribucionHoraria = distribucionGuardiaService.findById(id).orElse(null);

        if (distribucionHoraria == null)
            distribucionHoraria = distribucionOtraService.findById(id).orElse(null);

        return distribucionHoraria;
    }
}

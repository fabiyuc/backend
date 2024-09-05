package com.guardias.backend.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.entity.ValorGuardiaCargoYagrup;
import com.guardias.backend.repository.ValorGuardiaCargoYagrupRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ValorGuardiaCargoYagrupService {
    
    @Autowired
    ValorGuardiaCargoYagrupRepository valorGuardiaCargoYagrupRepository;

    @Autowired
    private BonoUtiService bonoUtiService;

    @Autowired
    private ValorGmiService valorGmiService;

    @Autowired
    private HospitalService hospitalService;


     public Optional<List<ValorGuardiaCargoYagrup>> findByActivoTrue() {
        return valorGuardiaCargoYagrupRepository.findByActivoTrue();
    }

     public List<ValorGuardiaCargoYagrup> findAll() {
        return valorGuardiaCargoYagrupRepository.findAll();
    }

    public Optional<ValorGuardiaCargoYagrup> findById(Long id) {
        return valorGuardiaCargoYagrupRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return valorGuardiaCargoYagrupRepository.existsById(id);
    }

    public void save(ValorGuardiaCargoYagrup valorGuardiaCargoYagrup) {
        valorGuardiaCargoYagrupRepository.save(valorGuardiaCargoYagrup);
    }

    public void deleteById(Long id) {
        valorGuardiaCargoYagrupRepository.deleteById(id);
    }

    public boolean activo(Long id) {
        return (valorGuardiaCargoYagrupRepository.existsById(id) && valorGuardiaCargoYagrupRepository.findById(id).get().isActivo());
    }

    public void crearValoresGuardiaCargoYagrup() {
        // Buscar el valorGMI activo
        ValorGmi valorGmi = valorGmiService.findByActivoTrue().get().get(0);
        // Buscar el BonoUti activo
        Long idBonoUti = bonoUtiService.findByActivoTrue().get().get(0).getId();

        // Crear valores de guardia por nivel
        crearPorNivel(1, valorGmi, idBonoUti);
        crearPorNivel(2, valorGmi, idBonoUti);
        crearPorNivel(3, valorGmi, idBonoUti);
        crearPorNivel(4, valorGmi, idBonoUti);
    }

    private void crearPorNivel(int nivel, ValorGmi valorGmi, Long idBonoUti) {
        List<String> hospitales;
        ValorGuardiaCargoYagrup valorGuardia;

        switch (nivel) {
            case 1:
                // Crear ValorGuardiaCargoYagrup con el efector "Susques"
                hospitales = Collections.singletonList("Susques");
                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospitales, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);

                // Crear ValorGuardiaCargoYagrup con otros efectores de nivel 1 excepto "Susques"
                hospitales = hospitalService.obtenerHospitalesPorNivelExcluyendo(1, "Susques");
                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospitales, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);
                break;

            case 2:
                // Crear ValorGuardiaCargoYagrup con el efector "Uro"
                hospitales = Collections.singletonList("Uro");
                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospitales, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);

                // Crear ValorGuardiaCargoYagrup con otros efectores de nivel 2 excepto "Uro"
                hospitales = hospitalService.obtenerHospitalesPorNivelExcluyendo(2, "Uro");
                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospitales, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);
                break;

            case 3:
                // Crear ValorGuardiaCargoYagrup con efectores de nivel 3
                hospitales = hospitalService.findHospitalesPorNivel(3);
                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospitales, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);
                break;

            case 4:
                // Crear ValorGuardiaCargoYagrup con el efector "SAME"
                hospitales = Collections.singletonList("SAME");
                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospitales, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);
                break;

            default:
                throw new IllegalArgumentException("Nivel de complejidad no soportado: " + nivel);
        }
    }

    private ValorGuardiaCargoYagrup crearValorGuardiaCargoYagrup(int nivel, List<String> efectores, ValorGmi valorGmi, Long idBonoUti) {
        ValorGuardiaCargoYagrup valorGuardia = new ValorGuardiaCargoYagrup();
        valorGuardia.setActivo(true);
        valorGuardia.setTipoGuardia(valorGmi.getTipoGuardia());
        valorGuardia.setNivelComplejidad(nivel);
        valorGuardia.setEfectores(efectores);
        valorGuardia.setFechaInicio(valorGmi.getFechaInicio());
        valorGuardia.setFechaFin(valorGmi.getFechaFin());
        valorGuardia.setValorGmi(valorGmiService.findById(valorGmi.getId()).get());
        valorGuardia.setBonoUti(bonoUtiService.findById(idBonoUti).get());
        
        BigDecimal montoValorGmi = valorGmi.getMonto();
        BigDecimal montoMultiplicado = montoValorGmi.multiply(BigDecimal.valueOf(2));

    switch (nivel) {
        case 4:
            valorGuardia.setDecreto1178Lav(montoMultiplicado);
            valorGuardia.setDecreto1178Sdf(montoMultiplicado.add(montoMultiplicado.multiply(BigDecimal.valueOf(0.10))));
            valorGuardia.setDecreto1657Lav(null);
            valorGuardia.setDecreto1657Sdf(null);
            valorGuardia.setTotalLav(
                valorGuardia.getDecreto1178Lav()
                    .add(valorGuardia.getBonoUti().getMonto()));
            valorGuardia.setTotalSdf(
                valorGuardia.getDecreto1178Sdf()
                    .add(valorGuardia.getBonoUti().getMonto())
                    .add(valorGuardia.getBonoUti().getMonto().multiply(BigDecimal.valueOf(0.10)))
);
            break;

        case 3:
            BigDecimal seventyPercent = montoMultiplicado.multiply(BigDecimal.valueOf(0.70));
            valorGuardia.setDecreto1178Lav(seventyPercent);
            valorGuardia.setDecreto1178Sdf(seventyPercent
                .add(seventyPercent.multiply(BigDecimal.valueOf(0.10))));

            valorGuardia.setDecreto1657Lav(montoMultiplicado.subtract(seventyPercent));

            valorGuardia.setDecreto1657Sdf(
                valorGuardia.getDecreto1657Lav()
                    .add(valorGuardia.getDecreto1657Lav().multiply(BigDecimal.valueOf(0.10))));
            
            valorGuardia.setTotalLav(valorGuardia.getDecreto1178Lav()
                .add(valorGuardia.getDecreto1657Lav()));
            
            valorGuardia.setTotalSdf(
                valorGuardia.getDecreto1178Sdf()
                .add(valorGuardia.getDecreto1657Sdf()));
            break;

        case 2:
            BigDecimal seventyPercent2 = valorGmi.getMonto().multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.70));

            if (efectores.contains("Uro")) {
                BigDecimal eightyPercentOfSeventyPercent = seventyPercent2.multiply(BigDecimal.valueOf(0.80)).multiply(BigDecimal.valueOf(2));
                valorGuardia.setDecreto1178Lav(eightyPercentOfSeventyPercent);
                valorGuardia.setDecreto1178Sdf(eightyPercentOfSeventyPercent.add(eightyPercentOfSeventyPercent.multiply(BigDecimal.valueOf(0.10))));
                valorGuardia.setDecreto1657Lav(montoMultiplicado.subtract(eightyPercentOfSeventyPercent));
                valorGuardia.setDecreto1657Sdf(valorGuardia.getDecreto1657Lav().add(eightyPercentOfSeventyPercent.multiply(BigDecimal.valueOf(0.10))));
            } else {
                BigDecimal sixtyPercent = montoMultiplicado.multiply(BigDecimal.valueOf(0.60));
                valorGuardia.setDecreto1178Lav(sixtyPercent);
                valorGuardia.setDecreto1178Sdf(sixtyPercent.add(sixtyPercent.multiply(BigDecimal.valueOf(0.10))));
                valorGuardia.setDecreto1657Lav( montoMultiplicado
                .subtract(valorGuardia.getDecreto1178Lav()));
                valorGuardia.setDecreto1657Sdf(
                    valorGuardia.getDecreto1657Lav()
                    .add(valorGuardia.getDecreto1657Lav().multiply(BigDecimal.valueOf(0.10))));
                valorGuardia.setTotalLav( valorGuardia.getDecreto1178Lav()
                    .add(valorGuardia.getDecreto1657Lav()));
                valorGuardia.setTotalSdf(valorGuardia.getDecreto1178Sdf()
                    .add(valorGuardia.getDecreto1657Sdf()));
            }
            break;

        case 1:
            if (efectores.contains("Susques")) {
                BigDecimal oneFortyPercent = montoMultiplicado.multiply(BigDecimal.valueOf(1.40));
                valorGuardia.setDecreto1178Lav(oneFortyPercent);
                valorGuardia.setDecreto1178Sdf(oneFortyPercent.add(oneFortyPercent.multiply(BigDecimal.valueOf(0.10))));
                valorGuardia.setDecreto1657Lav(montoMultiplicado);
                valorGuardia.setDecreto1657Sdf(montoMultiplicado);
            } else {
                BigDecimal fiftyPercent = montoMultiplicado.multiply(BigDecimal.valueOf(0.50));
                valorGuardia.setDecreto1178Lav(fiftyPercent);
                valorGuardia.setDecreto1178Sdf(fiftyPercent.add(fiftyPercent.multiply(BigDecimal.valueOf(0.10))));
                valorGuardia.setDecreto1657Lav(montoMultiplicado.subtract(valorGuardia.getDecreto1178Lav()));
                valorGuardia.setDecreto1657Sdf(valorGuardia.getDecreto1657Lav()
                    .add(valorGuardia.getDecreto1657Lav().multiply(BigDecimal.valueOf(0.10))));
                valorGuardia.setTotalLav( valorGuardia.getDecreto1178Lav()
                    .add(valorGuardia.getDecreto1657Lav()));
                valorGuardia.setTotalSdf(valorGuardia.getDecreto1178Sdf()
                    .add(valorGuardia.getDecreto1657Sdf()));
            }
            break;

        default:
            throw new IllegalArgumentException("Nivel de complejidad no soportado: " + nivel);
    }
        return valorGuardia;
    }


}

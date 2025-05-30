package com.guardias.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Hospital;
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

    /*
     * public Optional<ValorGuardiaCargoYagrup> buscarPorIdEfector(Long idEfector) {
     * return valorGuardiaCargoYagrupRepository.buscarPorIdEfector(idEfector);
     * }
     */

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
        return (valorGuardiaCargoYagrupRepository.existsById(id)
                && valorGuardiaCargoYagrupRepository.findById(id).get().isActivo());
    }

    /* Crea registros de ValorGuardiaCargoYagrup basados en el ValorGmi activo */
    public void crearValoresGuardiaCargoYagrup() {

        System.out.println("············ busco el valor Gmi activo ");

        // Obtiene el valorGmi activo
        ValorGmi valorGmi = valorGmiService.findByActivoTrue().get().get(0);
        System.out.println("········· el valor de GMI es:  " + valorGmi);

        System.out.println("······busco el id del BonoUti ativo ");
        // Obtiene el BonoUti activo
        Long idBonoUti = bonoUtiService.findByActivoTrue().get().get(0).getId();
        System.out.println("·······el id del bono uti es: " + idBonoUti);

        /* Ejecuta crearPorNivel() para 4 niveles de complejidad (1 al 4) */
        crearPorNivel(1, valorGmi, idBonoUti);
        crearPorNivel(2, valorGmi, idBonoUti);
        crearPorNivel(3, valorGmi, idBonoUti);
        crearPorNivel(4, valorGmi, idBonoUti);
    }

    /*
     * Crea registros de ValorGuardiaCargoYagrup por nivel de complejidad de
     * hospitales
     */
    private void crearPorNivel(int nivel, ValorGmi valorGmi, Long idBonoUti) {

        ValorGuardiaCargoYagrup valorGuardia;

        switch (nivel) {
            case 1:
                System.out.println("......nivel 1 caso especial hospital SUSQUES");

                /* Crea un registro solo para "SUSQUES" (con montos incrementados en 140%) */
                Hospital hospital1 = hospitalService.findByNombre("SUSQUES").get();
                System.out.println("····· hospital nivel 1: " + hospital1.getNombre());

                List<Hospital> hospital1ConSusques = new ArrayList<>();
                hospital1ConSusques.add(hospital1);

                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospital1ConSusques, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);

                /*
                 * Crea otro registro para todos los demás hospitales de nivel 1 (montos al 50%)
                 */
                List<Hospital> hospital1SinSusques = hospitalService.findHospitalesPorNivelExcluyendo(1, "SUSQUES");
                System.out.println("hospitales nivel 1" + hospital1SinSusques.get(0));

                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospital1SinSusques, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);
                break;

            case 2:
                System.out.println("·········nivel 2 caso especial hospital JORGE URO");
                /* Crea un registro solo para "JORGE URO" (montos al 80% del 70% base) */
                Hospital hospital2 = hospitalService.findByNombre("JORGE URO").get();
                System.out.println("······· hospital nivel 2: " + hospital2.getNombre());

                List<Hospital> hospital2ConUro = new ArrayList<>();
                hospital2ConUro.add(hospital2);

                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospital2ConUro, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);

                /* Crea otro registro para otros hospitales de nivel 2 (montos al 60%) */
                List<Hospital> hospital2SinUro = hospitalService.findHospitalesPorNivelExcluyendo(2, "JORGE URO");

                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospital2SinUro, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);
                break;

            case 3:
                /*
                 * Crear un registro para todos los hospitales de nivel 3 (montos distribuidos:
                 * 70% decreto 1178, 30% decreto 1657).
                 */
                List<Hospital> hospital3 = hospitalService.findHospitalesPorNivel(3);

                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospital3, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);
                break;

            case 4:
                /* Caso especial efector "SAME" */
                // Crea un registro con montos al doble del ValorGmi (100% decreto 1178, sin
                // decreto 1657).
                Hospital hospital4 = hospitalService.findByNombre("SAME").get();
                System.out.println("····· efector nivel 4: " + hospital4.getNombre());

                List<Hospital> hospital4ConSame = new ArrayList<>();
                hospital4ConSame.add(hospital4);

                valorGuardia = crearValorGuardiaCargoYagrup(nivel, hospital4ConSame, valorGmi, idBonoUti);
                valorGuardiaCargoYagrupRepository.save(valorGuardia);
                break;

            default:
                throw new IllegalArgumentException("Nivel de complejidad no soportado: " + nivel);
        }
    }

    private ValorGuardiaCargoYagrup crearValorGuardiaCargoYagrup(
            int nivel,
            List<Hospital> hospitales,
            ValorGmi valorGmi,
            Long idBonoUti) {
        System.out.println("entro a crear valor guardia cargo y agrup ");
        /*Construyo un objeto con datos base del ValorGmi(fechas, tipo de guardias) */
        ValorGuardiaCargoYagrup valorGuardia = new ValorGuardiaCargoYagrup();
        valorGuardia.setActivo(true);
        valorGuardia.setTipoGuardia(valorGmi.getTipoGuardia());
        valorGuardia.setNivelComplejidad(nivel);
        valorGuardia.setHospitales(hospitales);
        valorGuardia.setFechaInicio(valorGmi.getFechaInicio());
        valorGuardia.setFechaFin(valorGmi.getFechaFin());
        valorGuardia.setValorGmi(valorGmiService.findById(valorGmi.getId()).get());
        valorGuardia.setBonoUti(bonoUtiService.findById(idBonoUti).get());

        BigDecimal montoValorGmi = valorGmi.getMonto();
        BigDecimal montoMultiplicado = montoValorGmi.multiply(BigDecimal.valueOf(2));

        /*Cálculo de montos segun nivel y hospital */
        switch (nivel) {
            case 4:
                /*decreto1178Lav / decreto1178Sdf: Montos base + 10% para SDF. */
                valorGuardia.setDecreto1178Lav(montoMultiplicado);
                valorGuardia
                        .setDecreto1178Sdf(montoMultiplicado.add(montoMultiplicado.multiply(BigDecimal.valueOf(0.10))));
                
                /*Decreto1657Lav / decreto1657Sdf: Complemento (según nivel). */
                valorGuardia.setDecreto1657Lav(null);
                valorGuardia.setDecreto1657Sdf(null);
                
                /*totalLav / totalSdf: Suma de los montos de decretos + bono UTI. */
                valorGuardia.setTotalLav(
                        valorGuardia.getDecreto1178Lav()
                                .add(valorGuardia.getBonoUti().getMonto()));
                valorGuardia.setTotalSdf(
                        valorGuardia.getDecreto1178Sdf()
                                .add(valorGuardia.getBonoUti().getMonto())
                                .add(valorGuardia.getBonoUti().getMonto().multiply(BigDecimal.valueOf(0.10))));
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
                BigDecimal seventyPercent2 = valorGmi.getMonto().multiply(BigDecimal.valueOf(2))
                        .multiply(BigDecimal.valueOf(0.70));

                if (hospitales.stream().anyMatch(hospital -> hospital.getNombre().equals("JORGE URO"))) {
                    BigDecimal eightyPercentOfSeventyPercent = seventyPercent2.multiply(BigDecimal.valueOf(0.80))
                            .multiply(BigDecimal.valueOf(2));
                    valorGuardia.setDecreto1178Lav(eightyPercentOfSeventyPercent);
                    valorGuardia.setDecreto1178Sdf(eightyPercentOfSeventyPercent
                            .add(eightyPercentOfSeventyPercent.multiply(BigDecimal.valueOf(0.10))));
                    valorGuardia.setDecreto1657Lav(montoMultiplicado.subtract(eightyPercentOfSeventyPercent));
                    valorGuardia.setDecreto1657Sdf(valorGuardia.getDecreto1657Lav()
                            .add(eightyPercentOfSeventyPercent.multiply(BigDecimal.valueOf(0.10))));
                } else {
                    BigDecimal sixtyPercent = montoMultiplicado.multiply(BigDecimal.valueOf(0.60));
                    valorGuardia.setDecreto1178Lav(sixtyPercent);
                    valorGuardia.setDecreto1178Sdf(sixtyPercent.add(sixtyPercent.multiply(BigDecimal.valueOf(0.10))));
                    valorGuardia.setDecreto1657Lav(montoMultiplicado
                            .subtract(valorGuardia.getDecreto1178Lav()));
                    valorGuardia.setDecreto1657Sdf(
                            valorGuardia.getDecreto1657Lav()
                                    .add(valorGuardia.getDecreto1657Lav().multiply(BigDecimal.valueOf(0.10))));
                    valorGuardia.setTotalLav(valorGuardia.getDecreto1178Lav()
                            .add(valorGuardia.getDecreto1657Lav()));
                    valorGuardia.setTotalSdf(valorGuardia.getDecreto1178Sdf()
                            .add(valorGuardia.getDecreto1657Sdf()));
                }
                break;

            case 1:
                if (hospitales.stream().anyMatch(hospital -> hospital.getNombre().equals("SUSQUES"))) {
                    BigDecimal oneFortyPercent = montoMultiplicado.multiply(BigDecimal.valueOf(1.40));
                    valorGuardia.setDecreto1178Lav(oneFortyPercent);
                    valorGuardia
                            .setDecreto1178Sdf(oneFortyPercent.add(oneFortyPercent.multiply(BigDecimal.valueOf(0.10))));
                    valorGuardia.setDecreto1657Lav(montoMultiplicado);
                    valorGuardia.setDecreto1657Sdf(montoMultiplicado);
                } else {
                    BigDecimal fiftyPercent = montoMultiplicado.multiply(BigDecimal.valueOf(0.50));
                    valorGuardia.setDecreto1178Lav(fiftyPercent);
                    valorGuardia.setDecreto1178Sdf(fiftyPercent.add(fiftyPercent.multiply(BigDecimal.valueOf(0.10))));
                    valorGuardia.setDecreto1657Lav(montoMultiplicado.subtract(valorGuardia.getDecreto1178Lav()));
                    valorGuardia.setDecreto1657Sdf(valorGuardia.getDecreto1657Lav()
                            .add(valorGuardia.getDecreto1657Lav().multiply(BigDecimal.valueOf(0.10))));
                    valorGuardia.setTotalLav(valorGuardia.getDecreto1178Lav()
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

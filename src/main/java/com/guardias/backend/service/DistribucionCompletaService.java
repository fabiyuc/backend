//service/DistribucionCompletaService
package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.CronogramaTentativoDto;
import com.guardias.backend.dto.DistribucionConsultorioDto;
import com.guardias.backend.dto.DistribucionGiraDto;
import com.guardias.backend.dto.DistribucionGuardiaDto;
import com.guardias.backend.dto.DistribucionOtraDto;
import com.guardias.backend.dto.DistribucionesConCronogramasDto;
import com.guardias.backend.dto.ResultadoCreacionDto;
import com.guardias.backend.entity.CronogramaTentativo;
import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.entity.DistribucionGira;
import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.entity.DistribucionOtra;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.AutorizadoTentativoEnum;
import com.guardias.backend.enums.DiasEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionCompletaService {

    @Autowired
    private DistribucionGuardiaService distribucionGuardiaService;

    @Autowired
    private DistribucionConsultorioService distribucionConsultorioService;

    @Autowired
    private DistribucionGiraService distribucionGiraService;

    @Autowired
    private DistribucionOtraService distribucionOtraService;

    @Autowired
    private CronogramaTentativoService cronogramaTentativoService;

    @Autowired
    DistribucionHorariaService distribucionHorariaService;

    public ResultadoCreacionDto crearDistribucionesConCronogramas(DistribucionesConCronogramasDto dto) {
        int distribucionesCreadas = 0;
        int cronogramasCreados = 0;

        // 1. Crear Distribuciones de Guardias
        if (dto.getGuardias() != null && !dto.getGuardias().isEmpty()) {
            for (DistribucionGuardiaDto guardiaDto : dto.getGuardias()) {

                // Validar la guardia
                ResponseEntity<?> validacion = distribucionHorariaService.validations(guardiaDto);

                if (validacion.getStatusCode() == HttpStatus.OK) {
                    // Crear y guardar la distribución de guardia
                    DistribucionGuardia distribucionGuardia = distribucionGuardiaService.createUpdate(
                            new DistribucionGuardia(), guardiaDto);
                    distribucionGuardiaService.save(distribucionGuardia);
                    distribucionesCreadas++;

                    // Crear Cronogramas Tentativos SOLO para Guardias
                    if (dto.isCrearCronogramasParaGuardias()) {
                        List<CronogramaTentativo> cronogramasGuardia = cronogramaTentativoService.crearCronogramasDesdeGuardia(
                                distribucionGuardia);
                        cronogramasCreados += cronogramasGuardia.size();
                    }
                }
            }
        }

        // 2. Crear Distribuciones de Consultorios (sin cronogramas)
        if (dto.getConsultorios() != null && !dto.getConsultorios().isEmpty()) {
            for (DistribucionConsultorioDto consultorioDto : dto.getConsultorios()) {

                // Validar la guardia
                ResponseEntity<?> validacion = distribucionHorariaService.validations(consultorioDto);
                if (validacion.getStatusCode() == HttpStatus.OK) {
                    // Crear y guardar la distribución de guardia
                    DistribucionConsultorio distribucionConsultorio = distribucionConsultorioService
                            .createUpdate(new DistribucionConsultorio(), consultorioDto);
                    distribucionConsultorioService.save(distribucionConsultorio);
                    distribucionesCreadas++;
                }
            }
        }

        // 3. Crear Distribuciones de Giras (sin cronogramas)
        if (dto.getGiras() != null && !dto.getGiras().isEmpty()) {
            for (DistribucionGiraDto giraDto : dto.getGiras()) {
                // Validar la guardia
                ResponseEntity<?> validacion = distribucionHorariaService.validations(giraDto);
                if (validacion.getStatusCode() == HttpStatus.OK) {
                    // Crear y guardar la distribución de giras
                    DistribucionGira distribucionGira = distribucionGiraService.createUpdate(new DistribucionGira(),
                            giraDto);
                    distribucionGiraService.save(distribucionGira);
                    distribucionesCreadas++;
                }
            }
        }

        // 4. Crear Otras Distribuciones (sin cronogramas)
        if (dto.getOtras() != null && !dto.getOtras().isEmpty()) {
            for (DistribucionOtraDto otraDto : dto.getOtras()) {
                // Validar la guardia
                ResponseEntity<?> validacion = distribucionHorariaService.validations(otraDto);
                if (validacion.getStatusCode() == HttpStatus.OK) {
                    // Crear y guardar la distribución de otras
                    DistribucionOtra distribucionOtra = distribucionOtraService.createUpdate(new DistribucionOtra(),
                            otraDto);
                    distribucionOtraService.save(distribucionOtra);
                    distribucionesCreadas++;
                }
            }
        }

        return new ResultadoCreacionDto(distribucionesCreadas, cronogramasCreados);
    }

}

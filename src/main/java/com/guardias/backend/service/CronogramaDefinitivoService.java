package com.guardias.backend.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.CronogramaDefinitivoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.CronogramaDefinitivoRepository;
import com.guardias.backend.repository.DdjjRepository;

@Service
@Transactional
public class CronogramaDefinitivoService {

    @Autowired
    CronogramaDefinitivoRepository cronogramaDefinitivoRepository;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    DdjjRepository ddjjRepository;
    @Autowired
    RegistroActividadService registroActividadService;

    public Optional<List<CronogramaDefinitivo>> findByActivoTrue() {
        return cronogramaDefinitivoRepository.findByActivoTrue();
    }

    public List<CronogramaDefinitivo> findAll() {
        return cronogramaDefinitivoRepository.findAll();
    }

    public Optional<CronogramaDefinitivo> findById(Long id) {
        return cronogramaDefinitivoRepository.findById(id);
    }

    public List<CronogramaDefinitivo> findByAnioAndMesAndIdEfectorAndActivoTrue(int anio, MesesEnum mes,
            Long idEfector) {
        return cronogramaDefinitivoRepository.findByAnioAndMesAndEfectorIdAndActivoTrue(anio, mes, idEfector);
    }

    boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return cronogramaDefinitivoRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsById(Long id) {
        return cronogramaDefinitivoRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (cronogramaDefinitivoRepository.existsById(id)
                && cronogramaDefinitivoRepository.findById(id).get().isActivo());
    }

    public void save(CronogramaDefinitivo cronogramaDefinitivo) {
        cronogramaDefinitivoRepository.save(cronogramaDefinitivo);
    }

    public void deleteById(Long id) {
        cronogramaDefinitivoRepository.deleteById(id);
    }

    public ResponseEntity<?> validations(CronogramaDefinitivoDto cronogramaDefinitivoDto) {

        if (cronogramaDefinitivoDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cronogramaDefinitivoDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (cronogramaDefinitivoDto.getIdDdjjs() == null)
            return new ResponseEntity(new Mensaje("la lista de ddjj no debe ser nula"), HttpStatus.BAD_REQUEST);

        boolean apto = registroActividadService.validarPrecondicionesCronograma(cronogramaDefinitivoDto.getIdEfector(),
                cronogramaDefinitivoDto.getMes().getNumeroMes(), cronogramaDefinitivoDto.getAnio());
        if (apto != true) {
            return new ResponseEntity(
                    new Mensaje("no cumple con las validaciones de la ddjj con estadoDirector aprobadas"),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public CronogramaDefinitivo createUpdate(CronogramaDefinitivo cronogramaDefinitivo,
            CronogramaDefinitivoDto cronogramaDefinitivoDto) {

        if (cronogramaDefinitivoDto.getMes() != null
                && !cronogramaDefinitivoDto.getMes().equals(cronogramaDefinitivo.getMes()))
            cronogramaDefinitivo.setMes(cronogramaDefinitivoDto.getMes());

        if (cronogramaDefinitivoDto.getAnio() != cronogramaDefinitivo.getAnio())
            cronogramaDefinitivo.setAnio(cronogramaDefinitivoDto.getAnio());

        if (cronogramaDefinitivoDto.getIdEfector() != null && (cronogramaDefinitivo.getEfector() == null
                || !Objects.equals(cronogramaDefinitivo.getEfector().getId(),
                        cronogramaDefinitivoDto.getIdEfector()))) {
            cronogramaDefinitivo.setEfector(efectorService.findById(cronogramaDefinitivoDto.getIdEfector()));
        }

        // Validar si idDdjjs no es null
        if (cronogramaDefinitivoDto.getIdDdjjs() != null) {
            // Si no es null, procesar las ddjj
            for (Long idDdjj : cronogramaDefinitivoDto.getIdDdjjs()) {
                // Lógica para procesar cada idDdjj
                Ddjj ddjj = ddjjRepository.findById(idDdjj).orElse(null);
                if (ddjj != null && !cronogramaDefinitivo.getDdjjs().contains(ddjj)) {
                    cronogramaDefinitivo.getDdjjs().add(ddjj);
                    ddjj.getCronogramasDefinitivos().add(cronogramaDefinitivo);
                }
            }
        }

        /*
         * if (cronogramaDefinitivoDto.getIdDdjjs() != null) {
         * List<Long> idList = new ArrayList<Long>();
         * if (cronogramaDefinitivo.getDdjjs() != null) {
         * for (Ddjj ddjj : cronogramaDefinitivo.getDdjjs()) {
         * for (Long id : cronogramaDefinitivoDto.getIdDdjjs()) {
         * if (!cronogramaDefinitivo.getId().equals(id)) {
         * idList.add(id);
         * }
         * }
         * }
         * }
         * List<Long> idsToAdd = idList.isEmpty() ? cronogramaDefinitivoDto.getIdDdjjs()
         * : idList;
         * for (Long id : idsToAdd) {
         * cronogramaDefinitivo.getDdjjs().add(ddjjRepository.findById(id).get());
         * ddjjRepository.findById(id).get().setCronogramaDefinitivo(
         * cronogramaDefinitivo);
         * }
         * }
         */

        cronogramaDefinitivo.setActivo(true);
        return cronogramaDefinitivo;
    }

    public CronogramaDefinitivo createCronogramaDefinitivo(Long idAsistencial, Long idEfector, MesesEnum mesEnum,
            int anio) {

        CronogramaDefinitivo cronogramaDefinitivo = new CronogramaDefinitivo();
        cronogramaDefinitivo.setMes(mesEnum);
        cronogramaDefinitivo.setAnio(anio);
        cronogramaDefinitivo.setEfector(efectorService.findById(idEfector));
        cronogramaDefinitivo.setActivo(true);

        try {
            save(cronogramaDefinitivo);
            return cronogramaDefinitivo;
        } catch (Exception e) {
            System.out.println(
                    "error al crear el cronograma definitivo-  registroMensualService Ln196 -- " + e.getMessage());
            return null;
        }
    }

}

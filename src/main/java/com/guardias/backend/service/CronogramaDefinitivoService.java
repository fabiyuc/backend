package com.guardias.backend.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.CronogramaDefinitivoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.CronogramaDefinitivoRepository;

@Service
@Transactional
public class CronogramaDefinitivoService {
    
    @Autowired
    CronogramaDefinitivoRepository cronogramaDefinitivoRepository;
    
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    EfectorService efectorService;

    public Optional<List<CronogramaDefinitivo>> findByActivoTrue() {
        return cronogramaDefinitivoRepository.findByActivoTrue();
    }

    public List<CronogramaDefinitivo> findAll() {
        return cronogramaDefinitivoRepository.findAll();
    }

    public List<CronogramaDefinitivo> findByAnioMesEfectorAndTipoGuardiaCargoReagrupacion(int anio, MesesEnum mes,
            Long idEfector) {
        List<CronogramaDefinitivo> cronogramasDefinitivos = cronogramaDefinitivoRepository.findByAnioMesEfector(anio, mes, idEfector);

        for (CronogramaDefinitivo cronogramaDefinitivo : cronogramasDefinitivos) {
            List<RegistroActividad> actividadesFiltradas = cronogramaDefinitivo.getRegistroActividad().stream()
                    .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CARGO
                            || actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.AGRUPACION)
                    .collect(Collectors.toList());
            cronogramaDefinitivo.setRegistroActividad(actividadesFiltradas);
        }

        return cronogramasDefinitivos;
    }

    public List<CronogramaDefinitivo> findByAnioMesEfectorAndTipoGuardiaExtra(int anio, MesesEnum mes,
            Long idEfector) {
        List<CronogramaDefinitivo> cronogramasDefinitivos = cronogramaDefinitivoRepository.findByAnioMesEfector(anio, mes, idEfector);

        for (CronogramaDefinitivo cronogramaDefinitivo : cronogramasDefinitivos) {
            List<RegistroActividad> actividadesFiltradas = cronogramaDefinitivo.getRegistroActividad().stream()
                    .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.EXTRA)
                    .collect(Collectors.toList());
            cronogramaDefinitivo.setRegistroActividad(actividadesFiltradas);
        }

        return cronogramasDefinitivos;
    }
    
    public List<CronogramaDefinitivo> findByAnioMesEfectorAndTipoGuardiaCF(int anio, MesesEnum mes,
            Long idEfector) {
        List<CronogramaDefinitivo> cronogramasDefinitivos = cronogramaDefinitivoRepository.findByAnioMesEfector(anio, mes, idEfector);

        for (CronogramaDefinitivo cronogramaDefinitivo : cronogramasDefinitivos) {
            List<RegistroActividad> actividadesFiltradas = cronogramaDefinitivo.getRegistroActividad().stream()
                    .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CONTRAFACTURA)
                    .collect(Collectors.toList());
            cronogramaDefinitivo.setRegistroActividad(actividadesFiltradas);
        }

        return cronogramasDefinitivos;
    }

    public Optional<CronogramaDefinitivo> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId, MesesEnum mes, int anio) {
        return cronogramaDefinitivoRepository.findByAsistencialIdAndEfectorIdAndMesAndAnio(asistencialId, efectorId, mes, anio);
    }

    // public Optional<Long> idByIdAsistencialAndMes(Long idAsistencial, Long
    // idEfector, MesesEnum mes, int anio) {
    // return registroMensualRepository.idByIdAsistencialAndMes(idAsistencial,
    // idEfector, mes, anio);
    // }

    public Optional<CronogramaDefinitivo> findById(Long id) {
        return cronogramaDefinitivoRepository.findById(id);
    }

    boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return cronogramaDefinitivoRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsByAsistencialId(Long idAsistencial) {
        return cronogramaDefinitivoRepository.existsByAsistencialId(idAsistencial);
    }

    public boolean existsById(Long id) {
        return cronogramaDefinitivoRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (cronogramaDefinitivoRepository.existsById(id) && cronogramaDefinitivoRepository.findById(id).get().isActivo());
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

        if (cronogramaDefinitivoDto.getIdAsistencial() < 1)
            return new ResponseEntity(new Mensaje("El id de la persona es incorrecto"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public CronogramaDefinitivo createUpdate(CronogramaDefinitivo cronogramaDefinitivo,
            CronogramaDefinitivoDto cronogramaDefinitivoDto) {

        if (cronogramaDefinitivoDto.getMes() != null && !cronogramaDefinitivoDto.getMes().equals(cronogramaDefinitivo.getMes()))
            cronogramaDefinitivo.setMes(cronogramaDefinitivoDto.getMes());

        if (cronogramaDefinitivoDto.getAnio() != cronogramaDefinitivo.getAnio())
            cronogramaDefinitivo.setAnio(cronogramaDefinitivoDto.getAnio());

        // if (registroMensualDto.getIdAsistencial() !=
        // registroMensual.getIdAsistencial())
        // registroMensual.setIdAsistencial(registroMensualDto.getIdAsistencial());

        if (cronogramaDefinitivoDto.getIdAsistencial() != null && (cronogramaDefinitivo.getAsistencial() == null || !Objects.equals(cronogramaDefinitivo.getAsistencial().getId(), cronogramaDefinitivoDto.getIdAsistencial()))) {
            cronogramaDefinitivo.setAsistencial(asistencialService.findById(cronogramaDefinitivoDto.getIdAsistencial()).get());
        }

        if (cronogramaDefinitivoDto.getIdEfector() != null && (cronogramaDefinitivo.getEfector() == null
                || !Objects.equals(cronogramaDefinitivo.getEfector().getId(), cronogramaDefinitivoDto.getIdEfector()))) {
            cronogramaDefinitivo.setEfector(efectorService.findById(cronogramaDefinitivoDto.getIdEfector()));
        }

        cronogramaDefinitivo.setActivo(true);
        return cronogramaDefinitivo;
    }

     public CronogramaDefinitivo createCronogramaDefinitivo(Long idAsistencial, Long idEfector, MesesEnum mesEnum, int anio) {

        CronogramaDefinitivo cronogramaDefinitivo = new CronogramaDefinitivo();
        cronogramaDefinitivo.setMes(mesEnum);
        cronogramaDefinitivo.setAnio(anio);
        cronogramaDefinitivo.setAsistencial(asistencialService.findById(idAsistencial).get());
        cronogramaDefinitivo.setEfector(efectorService.findById(idEfector));
        cronogramaDefinitivo.setActivo(true);

        try {
            save(cronogramaDefinitivo);
            return cronogramaDefinitivo;
        } catch (Exception e) {
            System.out.println("error al crear el cronograma definitivo-  registroMensualService Ln196 -- " + e.getMessage());
            return null;
        }
    }

    public RegistroActividad setCronogramaDefinitivo(RegistroActividad registroActividad) {

        Long idAsistencial = registroActividad.getAsistencial().getId();
        Long idEfector = registroActividad.getEfector().getId();
        int mes = registroActividad.getFechaIngreso().getMonth().getValue();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        int anio = registroActividad.getFechaIngreso().getYear();
        Long id;
        CronogramaDefinitivo cronogramaDefinitivo = new CronogramaDefinitivo();

        try {
            cronogramaDefinitivo = findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector, mesEnum, anio).get();
            System.out.println("##### id del cronograma definitivo encontrado: " + cronogramaDefinitivo.getId());
        } catch (Exception exception) {
            System.out.println("id no encontrado cronogramaDefinitivoService Ln215 - " + exception.getMessage());
            cronogramaDefinitivo = createCronogramaDefinitivo(idAsistencial, idEfector, mesEnum, anio);
        }
        id = cronogramaDefinitivo.getId();
        System.out.println("##### id del cronograma definitivo fuera del try: " + cronogramaDefinitivo.getId());

        
        // JsonFile jsonFile = addRegistroActividadToJsonFile(new JsonFile(),
        // registroActividad);
        //luego vemos el json //JsonFile jsonFile = new JsonFile();
        try {
            registroActividad.setCronogramaDefinitivo(findById(id).get());
            /*luego vemos el json // if (registroMensual.getJsonFile() != null) {
                jsonFile = registroMensual.getJsonFile();
            } */
        } catch (Exception e) {
            System.out.println("error: idCronogramaDefinitivo nulo  cronogramaDefinitivoService Ln247 -- " + e.getMessage());
        }
        // jsonFileService.save(jsonFile);
        //luego vemos el json //registroMensual.setJsonFile(addRegistroActividadToJsonFile(jsonFile, registroActividad));

        return registroActividad;
    }
}

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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroMensualDto;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.RegistroMensualRepository;

@Service
@Transactional
public class RegistroMensualService {

    @Autowired
    RegistroMensualRepository registroMensualRepository;
    @Autowired
    DdjjService ddjjService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    SumaHorasService sumaHorasService;

    public Optional<List<RegistroMensual>> findByActivoTrue() {
        return registroMensualRepository.findByActivoTrue();
    }

    public List<RegistroMensual> findAll() {
        return registroMensualRepository.findAll();
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCargoReagrupacion(int anio, MesesEnum mes,
            Long idEfector) {
        List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes, idEfector);

        for (RegistroMensual registroMensual : registrosMensuales) {
            List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                    .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CARGO
                            || actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.AGRUPACION)
                    .collect(Collectors.toList());
            registroMensual.setRegistroActividad(actividadesFiltradas);
        }

        return registrosMensuales;
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaExtra(int anio, MesesEnum mes,
            Long idEfector) {
        List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes, idEfector);

        for (RegistroMensual registroMensual : registrosMensuales) {
            List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                    .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.EXTRA)
                    .collect(Collectors.toList());
            registroMensual.setRegistroActividad(actividadesFiltradas);
        }

        return registrosMensuales;
    }
    
    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCF(int anio, MesesEnum mes,
            Long idEfector) {
        List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes, idEfector);

        for (RegistroMensual registroMensual : registrosMensuales) {
            List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                    .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CONTRAFACTURA)
                    .collect(Collectors.toList());
            registroMensual.setRegistroActividad(actividadesFiltradas);
        }

        return registrosMensuales;
    }

    public Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId,
            MesesEnum mes, int anio) {
        return registroMensualRepository.findByAsistencialIdAndEfectorIdAndMesAndAnio(asistencialId, efectorId, mes,
                anio);
    }

    // public Optional<Long> idByIdAsistencialAndMes(Long idAsistencial, Long
    // idEfector, MesesEnum mes, int anio) {
    // return registroMensualRepository.idByIdAsistencialAndMes(idAsistencial,
    // idEfector, mes, anio);
    // }

    public Optional<RegistroMensual> findById(Long id) {
        return registroMensualRepository.findById(id);
    }

    boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return registroMensualRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsByAsistencialId(Long idAsistencial) {
        return registroMensualRepository.existsByAsistencialId(idAsistencial);
    }

    public boolean existsById(Long id) {
        return registroMensualRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (registroMensualRepository.existsById(id) && registroMensualRepository.findById(id).get().isActivo());
    }

    public void save(RegistroMensual registroMensual) {
        registroMensualRepository.save(registroMensual);
    }

    public void deleteById(Long id) {
        registroMensualRepository.deleteById(id);
    }

    public ResponseEntity<?> validations(RegistroMensualDto registroMensualDto) {

        if (registroMensualDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (registroMensualDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (registroMensualDto.getIdAsistencial() < 1)
            return new ResponseEntity(new Mensaje("El id de la persona es incorrecto"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public RegistroMensual createUpdate(RegistroMensual registroMensual,
            RegistroMensualDto registroMensualDto) {

        if (registroMensualDto.getMes() != null && !registroMensualDto.getMes().equals(registroMensual.getMes()))
            registroMensual.setMes(registroMensualDto.getMes());

        if (registroMensualDto.getAnio() != registroMensual.getAnio())
            registroMensual.setAnio(registroMensualDto.getAnio());

        // if (registroMensualDto.getIdAsistencial() !=
        // registroMensual.getIdAsistencial())
        // registroMensual.setIdAsistencial(registroMensualDto.getIdAsistencial());

        if (registroMensualDto.getIdAsistencial() != null && (registroMensual.getAsistencial() == null
                || !Objects.equals(registroMensual.getAsistencial().getId(), registroMensualDto.getIdAsistencial()))) {
            registroMensual.setAsistencial(asistencialService.findById(registroMensualDto.getIdAsistencial()).get());
        }

        if (registroMensualDto.getIdEfector() != null && (registroMensual.getEfector() == null
                || !Objects.equals(registroMensual.getEfector().getId(), registroMensualDto.getIdEfector()))) {
            registroMensual.setEfector(efectorService.findById(registroMensualDto.getIdEfector()));
        }

        if (registroMensualDto.getIdDdjj() != null && (registroMensual.getDdjj() == null
                || !Objects.equals(registroMensual.getDdjj().getId(), registroMensualDto.getIdDdjj()))) {
            registroMensual.setDdjj(ddjjService.findById(registroMensualDto.getIdDdjj()).get());
        }

        registroMensual.setActivo(true);
        return registroMensual;
    }

     public RegistroMensual createRegistroMensual(Long idAsistencial, Long idEfector, MesesEnum mesEnum, int anio) {

        RegistroMensual registroMensual = new RegistroMensual();
        registroMensual.setMes(mesEnum);
        registroMensual.setAnio(anio);
        registroMensual.setAsistencial(asistencialService.findById(idAsistencial).get());
        registroMensual.setEfector(efectorService.findById(idEfector));
        registroMensual.setActivo(true);
        SumaHoras horas = new SumaHoras();

        horas.setHorasLav(0L);
        horas.setHorasSdf(0L);
        horas.setBonoLav(0L);
        horas.setBonoSdf(0L);
        registroMensual.setTotalHoras(horas);

        try {
            save(registroMensual);

            // registroMensual = findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial,
            // idEfector,
            // mesEnum, anio)
            // .get();

            return registroMensual;
        } catch (Exception e) {
            System.out.println("error al crear registro mensual  registroMensualService Ln196 -- " + e.getMessage());
            return null;
        }
    }

    public RegistroActividad setRegistroMensual(RegistroActividad registroActividad) {

        Long idAsistencial = registroActividad.getAsistencial().getId();
        Long idEfector = registroActividad.getEfector().getId();
        int mes = registroActividad.getFechaIngreso().getMonth().getValue();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        int anio = registroActividad.getFechaIngreso().getYear();
        Long id;
        RegistroMensual registroMensual = new RegistroMensual();

        try {
            registroMensual = findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector,
                    mesEnum, anio)
                    .get();
        } catch (Exception exception) {
            System.out.println("id no encontrado registroMensualService Ln243 - " + exception.getMessage());
            registroMensual = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio);
        }
        id = registroMensual.getId();

        // sumo las horas y los montos
        SumaHoras horas = sumaHorasService.sumarHorasMensuales(registroMensual.getTotalHoras(),
                registroActividad.getHorasRealizadas());
        sumaHorasService.save(horas);
        registroMensual.setTotalHoras(horas);
        // JsonFile jsonFile = addRegistroActividadToJsonFile(new JsonFile(),
        // registroActividad);
        //luego vemos el json //JsonFile jsonFile = new JsonFile();
        try {
            registroActividad.setRegistroMensual(findById(id).get());
            /*luego vemos el json // if (registroMensual.getJsonFile() != null) {
                jsonFile = registroMensual.getJsonFile();
            } */
        } catch (Exception e) {
            System.out.println("error: idRegistroMensual nulo  registroMensualService Ln259 -- " + e.getMessage());
        }
        // jsonFileService.save(jsonFile);
        //luego vemos el json //registroMensual.setJsonFile(addRegistroActividadToJsonFile(jsonFile, registroActividad));

        return registroActividad;
    }

}

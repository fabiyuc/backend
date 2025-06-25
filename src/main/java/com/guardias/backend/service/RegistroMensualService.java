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
import com.guardias.backend.repository.DdjjRepository;
import com.guardias.backend.repository.RegistroMensualRepository;

@Service
@Transactional
public class RegistroMensualService {

    @Autowired
    RegistroMensualRepository registroMensualRepository;
    @Autowired
    DdjjRepository ddjjRepository;
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

        return registrosMensuales.stream()
                .map(registroMensual -> {
                    List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                            .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CARGO
                                    || actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.AGRUPACION)
                            .collect(Collectors.toList());
                    registroMensual.setRegistroActividad(actividadesFiltradas);
                    return registroMensual;
                })
                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir registros con
                                                                                              // lista vacía
                .collect(Collectors.toList());
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaExtra(int anio, MesesEnum mes,
            Long idEfector) {
        List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes, idEfector);

        return registrosMensuales.stream()
                .map(registroMensual -> {
                    List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                            .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.EXTRA)
                            .collect(Collectors.toList());
                    registroMensual.setRegistroActividad(actividadesFiltradas);
                    return registroMensual;
                })
                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir registros con
                                                                                              // lista vacía
                .collect(Collectors.toList());
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCF(int anio, MesesEnum mes,
            Long idEfector) {
        List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes, idEfector);

        return registrosMensuales.stream()
                .map(registroMensual -> {
                    List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                            .filter(actividad -> actividad.getTipoGuardia()
                                    .getNombre() == TipoGuardiaEnum.CONTRAFACTURA)
                            .collect(Collectors.toList());
                    registroMensual.setRegistroActividad(actividadesFiltradas);
                    return registroMensual;
                })
                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir registros con
                                                                                              // lista vacía
                .collect(Collectors.toList());
    }

    public Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId,
            MesesEnum mes, int anio) {
        return registroMensualRepository.findByAsistencialIdAndEfectorIdAndMesAndAnio(asistencialId, efectorId, mes,
                anio);
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCargoReagrupacionAndServicio(
            int anio, MesesEnum mes, Long idEfector, Long idServicio) {

        // Utilizamos directamente la consulta personalizada del repositorio
        List<RegistroMensual> registrosMensuales = registroMensualRepository
                .findByAnioMesEfectorAndServicio(anio, mes, idEfector, idServicio);

        // Filtro adicional para dejar solo las actividades de tipo CARGO o AGRUPACION
        return registrosMensuales.stream()
                .map(registroMensual -> {
                    List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                            .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CARGO
                                    || actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.AGRUPACION)
                            .collect(Collectors.toList());
                    registroMensual.setRegistroActividad(actividadesFiltradas);
                    return registroMensual;
                })
                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir registros con
                                                                                              // lista vacía
                .collect(Collectors.toList());
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaExtraAndServicio(int anio, MesesEnum mes,
            Long idEfector, Long idServicio) {

        // Utilizamos directamente la consulta personalizada del repositorio
        List<RegistroMensual> registrosMensuales = registroMensualRepository
                .findByAnioMesEfectorAndServicio(anio, mes, idEfector, idServicio);

        // Filtro adicional para dejar solo las actividades de tipo EXTRA
        return registrosMensuales.stream()
                .map(registroMensual -> {
                    List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                            .filter(actividad -> actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.EXTRA)
                            .collect(Collectors.toList());
                    registroMensual.setRegistroActividad(actividadesFiltradas);
                    return registroMensual;
                })
                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir registros con
                                                                                              // lista vacía
                .collect(Collectors.toList());
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCFAndServicio(int anio, MesesEnum mes,
            Long idEfector, Long idServicio) {

        // Utilizamos directamente la consulta personalizada del repositorio
        List<RegistroMensual> registrosMensuales = registroMensualRepository
                .findByAnioMesEfectorAndServicio(anio, mes, idEfector, idServicio);

        // Filtro adicional para dejar solo las actividades de tipo CONTRAFACTURA
        return registrosMensuales.stream()
                .map(registroMensual -> {
                    List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                            .filter(actividad -> actividad.getTipoGuardia()
                                    .getNombre() == TipoGuardiaEnum.CONTRAFACTURA)
                            .collect(Collectors.toList());
                    registroMensual.setRegistroActividad(actividadesFiltradas);
                    return registroMensual;
                })
                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir registros con
                                                                                              // lista vacía
                .collect(Collectors.toList());
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
            registroMensual.setDdjj(ddjjRepository.findById(registroMensualDto.getIdDdjj()).get());
        }

        registroMensual.setActivo(true);
        return registroMensual;
    }

    /*Crea un nuevo RegistroMensual con valores iniciales (horas/montos en 0) */
    public RegistroMensual createRegistroMensual(Long idAsistencial, Long idEfector, MesesEnum mesEnum, int anio) {

        /*Inicializa un nuevo RegistroMensual con mes/año, asistencial,efector */
        RegistroMensual registroMensual = new RegistroMensual();
        registroMensual.setMes(mesEnum);
        registroMensual.setAnio(anio);
        registroMensual.setAsistencial(asistencialService.findById(idAsistencial).get());
        registroMensual.setEfector(efectorService.findById(idEfector));
        registroMensual.setActivo(true);
        
        //Creo SumaHoras vacio 
        SumaHoras horas = new SumaHoras();
        horas.setHorasLav(0L);
        horas.setHorasSdf(0L);
        registroMensual.setTotalHoras(horas);

        try {
            save(registroMensual);
            return registroMensual;
        } catch (Exception e) {
            System.out.println("error al crear registro mensual  registroMensualService Ln196 -- " + e.getMessage());
            return null;
        }
    }

    /*Busca o crea un registro mensual para el asistencial/efector/mes/año y acumula horas/montos*/
    public RegistroActividad setRegistroMensual(RegistroActividad registroActividad) {

        Long idAsistencial = registroActividad.getAsistencial().getId();
        Long idEfector = registroActividad.getEfector().getId();
        int mes = registroActividad.getFechaIngreso().getMonth().getValue();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        int anio = registroActividad.getFechaIngreso().getYear();
        Long id;
        RegistroMensual registroMensual = new RegistroMensual();

        try {
            /*Busca el registro mensual existente */
            registroMensual = findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector, mesEnum, anio).get();
            System.out.println("##### id del registro mensual encontrado: " + registroMensual.getId());
        } catch (Exception exception) {
            /*Si no existe, crea uno nuevo */
            System.out.println("id no encontrado registroMensualService Ln215 - " + exception.getMessage());
            registroMensual = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio);
        }
        id = registroMensual.getId();
        System.out.println("##### id del registro mensual fuera del try: " + registroMensual.getId());

        // sumo las horas y los montos
        System.out.println("... CREANDO UN NUEVO SUMAHORAS.... : ");

        System.out.println("... id sumahoras del reg mensual.... : " + registroMensual.getTotalHoras().getId());

        SumaHoras horas = registroMensual.getTotalHoras();

        if (horas == null) {
            horas = new SumaHoras();
            registroMensual.setTotalHoras(horas);
        }

        /*Acumula horas LAV/SDF y montos de un registros de actividad al total mensual */
        sumaHorasService.sumarHorasMensuales(horas, registroActividad.getHorasRealizadas());

        sumaHorasService.save(horas);

        // JsonFile jsonFile = addRegistroActividadToJsonFile(new JsonFile(), registroActividad);
        // luego vemos el json //JsonFile jsonFile = new JsonFile();
        try {
            /*Vincular registro de actividad al mensual */
            registroActividad.setRegistroMensual(findById(id).get());
            /*
             * luego vemos el json // if (registroMensual.getJsonFile() != null) {
             * jsonFile = registroMensual.getJsonFile();
             * }
             */
        } catch (Exception e) {
            System.out.println("error: idRegistroMensual nulo  registroMensualService Ln247 -- " + e.getMessage());
        }
        // jsonFileService.save(jsonFile);
        // luego vemos el json
        // //registroMensual.setJsonFile(addRegistroActividadToJsonFile(jsonFile,
        // registroActividad));

        return registroActividad;
    }

}

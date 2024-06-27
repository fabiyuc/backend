package com.guardias.backend.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroMensualDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.entity.JsonFile;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.entity.Servicio;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.RegistroMensualRepository;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.security.service.UsuarioService;

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
    SumaHorasService sumaHorasService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    JsonFileService jsonFileService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ServicioService servicioService;
    @Autowired
    TipoGuardiaService tipoGuardiaService;

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
                    .filter(actividad -> actividad.getTipoGuardia().getId() == 1
                            || actividad.getTipoGuardia().getId() == 2)
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
                    .filter(actividad -> actividad.getTipoGuardia().getId() == 3)
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

    public List<RegistroActividad> mapFromJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        List<RegistroActividad> registrosActividades = new ArrayList<>();

        for (JsonNode node : rootNode) {
            RegistroActividad registroActividad = new RegistroActividad();

            // Asignar los valores del JSON al objeto RegistroActividad manualmente
            registroActividad.setId(node.get("id").asLong());
            registroActividad.setFechaIngreso(LocalDate.parse(node.get("fechaIngreso").asText()));
            registroActividad.setFechaEgreso(LocalDate.parse(node.get("fechaEgreso").asText()));
            registroActividad.setHoraIngreso(LocalTime.parse(node.get("horaIngreso").asText()));
            registroActividad.setHoraEgreso(LocalTime.parse(node.get("horaEgreso").asText()));
            registroActividad.setActivo(node.get("activo").asBoolean());

            if (node.has("tipoGuardia") && node.get("tipoGuardia").has("id")) {
                registroActividad
                        .setTipoGuardia(tipoGuardiaService.findById(node.get("tipoGuardia").get("id").asLong()).get());
            } else {
                registroActividad.setTipoGuardia(new TipoGuardia());
            }

            if (node.has("asistencial") && node.get("asistencial").has("id")) {
                registroActividad
                        .setAsistencial(asistencialService.findById(node.get("asistencial").get("id").asLong()).get());
            } else {
                registroActividad.setAsistencial(new Asistencial());
            }

            if (node.has("servicio") && node.get("servicio").has("id")) {
                registroActividad.setServicio(servicioService.findById(node.get("servicio").get("id").asLong()).get());
            } else {
                registroActividad.setServicio(new Servicio());
            }

            if (node.has("efector") && node.get("efector").has("id")) {
                registroActividad.setEfector(efectorService.findById(node.get("efector").get("id").asLong()));
            } else {
                registroActividad.setEfector(new Hospital());
            }

            if (node.has("registroMensual") && node.get("registroMensual").has("id")) {
                registroActividad.setRegistroMensual(findById(node.get("registroMensual").get("id").asLong()).get());
            } else {
                registroActividad.setRegistroMensual(new RegistroMensual());
            }

            if (node.has("usuarioIngreso") && node.get("usuarioIngreso").has("id")) {
                registroActividad.setUsuarioIngreso(
                        usuarioService.findById(node.get("usuarioIngreso").get("id").asLong()).get());
            } else {
                registroActividad.setUsuarioIngreso(new Usuario());
            }

            if (node.has("usuarioEgreso") && node.get("usuarioEgreso").has("id")) {
                registroActividad
                        .setUsuarioEgreso(usuarioService.findById(node.get("usuarioEgreso").get("id").asLong()).get());
            } else {
                registroActividad.setUsuarioEgreso(new Usuario());
            }

            registroActividad.setFechaRegistroIngreso(LocalDate.parse(node.get("fechaRegistroIngreso").asText()));
            registroActividad.setHoraRegistroIngreso(LocalTime.parse(node.get("horaRegistroIngreso").asText()));
            registroActividad.setFechaRegistroEgreso(LocalDate.parse(node.get("fechaRegistroEgreso").asText()));
            registroActividad.setHoraRegistroEgreso(LocalTime.parse(node.get("horaRegistroEgreso").asText()));

            registrosActividades.add(registroActividad);
        }

        return registrosActividades;
    }

    public JsonFile addRegistroActividadToJsonFile(JsonFile jsonFile, RegistroActividad registroActividad) {
        String json = null;
        JsonFile updatedJsonFile = jsonFile;
        List<RegistroActividad> registroActividadList = new ArrayList<RegistroActividad>();

        try {
            json = jsonFileService.decodeToString(jsonFile);
            // registroActividadList = objectMapper.readValue(json, new
            // TypeReference<List<RegistroActividad>>() {});
            registroActividadList = mapFromJson(json);
        } catch (Exception e) {
            System.out.println("JsonFile nulo - " + e.getMessage());
        }
        try {
            registroActividadList.add(registroActividad);
            String updatedJson = objectMapper.writeValueAsString(registroActividadList);
            System.out.println(updatedJson);
            updatedJsonFile = jsonFileService.encodeToJsonAndSave(updatedJson);
            // updatedJsonFile.setId(jsonFile.getId());
            // jsonFileService.save(updatedJsonFile);

        } catch (JsonProcessingException e) {
            System.out.println("Error procesando Json registroMensualService Ln222 - " + e.getMessage());
            e.printStackTrace();
        }
        return updatedJsonFile;
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
        JsonFile jsonFile = new JsonFile();
        try {
            registroActividad.setRegistroMensual(findById(id).get());
            if (registroMensual.getJsonFile() != null) {
                jsonFile = registroMensual.getJsonFile();
            }
        } catch (Exception e) {
            System.out.println("error: idRegistroMensual nulo  registroMensualService Ln259 -- " + e.getMessage());
        }
        // jsonFileService.save(jsonFile);
        registroMensual.setJsonFile(addRegistroActividadToJsonFile(jsonFile, registroActividad));

        return registroActividad;
    }

}

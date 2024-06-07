package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroMensualDto;
import com.guardias.backend.entity.JsonFile;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.RegistroMensualRepository;

@Service
@Transactional
public class RegistroMensualService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    RegistroMensualRepository registroMensualRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    DdjjService ddjjService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    SumaHorasService sumaHorasService;
    @Autowired
    JsonFileService jsonFileService;

    public Optional<List<RegistroMensual>> findByActivoTrue() {
        return registroMensualRepository.findByActivoTrue();
    }

    public List<RegistroMensual> findAll() {
        return registroMensualRepository.findAll();
    }

    public Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId,
            MesesEnum mes, int anio) {
        return registroMensualRepository.findByAsistencialIdAndEfectorIdAndMesAndAnio(asistencialId, efectorId, mes,
                anio);
    }

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
        RegistroMensualDto registroMensualDto = new RegistroMensualDto();

        registroMensualDto.setMes(mesEnum);
        registroMensualDto.setAnio(anio);
        registroMensualDto.setIdAsistencial(idAsistencial);
        registroMensualDto.setIdEfector(idEfector);

        try {
            RegistroMensual registroMensual = createUpdate(new RegistroMensual(), registroMensualDto);
            return registroMensual;
        } catch (Exception e) {
            return null;
        }
    }

    public RegistroActividad addToRegistroMensual(RegistroActividad registroActividad) {

        Long idAsistencial = registroActividad.getAsistencial().getId();
        Long idEfector = registroActividad.getEfector().getId();
        int mes = registroActividad.getFechaIngreso().getMonth().getValue();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        int anio = registroActividad.getFechaIngreso().getYear();
        Long id;
        RegistroMensual registroMensual;

        try {
            registroMensual = findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector,
                    mesEnum, anio).get();
            id = registroMensual.getId();
        } catch (Exception exception) {
            System.out.println("id no encontrado");
            registroMensual = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio);
            id = registroMensual.getId();
        }

        try {
            registroActividad.setRegistroMensual(findById(id).get());
            addToJson(registroActividad, registroMensual);

        } catch (Exception e) {
            System.out.println("error: idRegistroMensual nulo -- " + e.getMessage());
        }
        return registroActividad;
    }

    public void addToJson(RegistroActividad registroActividad, RegistroMensual registroMensual) {

        try {

            List<RegistroActividad> registroActividadList = new ArrayList<>();
            String registroMensualJson = jsonFileService
                    .decodeToJson(registroMensual.getJsonFile());

            if (registroMensualJson != null && !registroMensualJson.isEmpty()) {
                registroActividadList = objectMapper.readValue(registroMensualJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, RegistroActividad.class));
            }
            registroActividadList.add(registroActividad);

            String updatedJson = objectMapper.writeValueAsString(registroActividad);

            JsonFile jsonFile = jsonFileService.encodeToJson(updatedJson);
            registroMensual.setJsonFile(jsonFile);

            save(registroMensual);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

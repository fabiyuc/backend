package com.guardias.backend.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.JsonFile;
import com.guardias.backend.repository.JsonFileRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class JsonFileService {
    @Autowired
    JsonFileRepository jsonFileRepository;

    public Optional<JsonFile> findById(Long id) {
        return jsonFileRepository.findById(id);
    }

    public Boolean existsById(Long id) {
        return jsonFileRepository.existsById(id);
    }

    public Optional<List<JsonFile>> findByActivoTrue() {
        return jsonFileRepository.findByActivoTrue();
    }

    public List<JsonFile> findAll() {
        return jsonFileRepository.findAll();
    }

    public void save(JsonFile jsonFile) {
        try {
            jsonFile.setFechaGuardado(LocalDateTime.now());
            jsonFileRepository.save(jsonFile);
        } catch (Exception e) {
            System.out.println("Error al intentar guardar el archivo Json JsonFileService Ln44 - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public JsonFile encodeToJson(String json) {
        try {
            byte[] encodedJson = Base64.getEncoder().encode(json.getBytes(StandardCharsets.UTF_8));

            JsonFile jsonFile = new JsonFile();
            jsonFile.setContenido(encodedJson);

            return jsonFile;
        } catch (Exception e) {
            System.out.println("Error Json JsonFileService Ln58 - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String decodeToString(JsonFile jsonFile) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(jsonFile.getContenido());
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Error Json JsonFileService Ln69 - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void deleteById(Long id) {
        jsonFileRepository.deleteById(id);
    }
}

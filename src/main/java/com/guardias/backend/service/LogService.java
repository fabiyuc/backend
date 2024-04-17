package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Log;
import com.guardias.backend.repository.LogRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LogService {

    @Autowired
    LogRepository logRepository;

    public Optional<List<Log>> findByActivoTrue() {
        return logRepository.findByActivoTrue();
    }

    public List<Log> findAll() {
        return logRepository.findAll();
    }

    public Optional<Log> findById(Long id) {
        return logRepository.findById((Long) id);
    }

    public void save(Log log) {
        logRepository.save(log);
    }

    public void deleteById(Long id) {
        logRepository.deleteById((Long) id);
    }

    public boolean existsById(long id) {
        return logRepository.existsById((Long) id);
    }

    public boolean activo(long id) {
        return (logRepository.existsById(id) && logRepository.findById(id).get().isActivo());
    }

}

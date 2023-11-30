package com.guardias.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.repository.AsistencialRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AsistencialService {
    
    @Autowired
    AsistencialRepository asistencialRepository;

    public List<Asistencial> list() {
        return asistencialRepository.findAll();
    }
}

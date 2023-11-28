package com.guardias.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.repository.NoAsistencialRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoAsistencialService {
   
    @Autowired
    NoAsistencialRepository noAsistencialRepository;

    public List<NoAsistencial> list() {
        return noAsistencialRepository.findAll();
    }
}

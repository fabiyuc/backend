package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guardias.backend.entity.Cargo;
import com.guardias.backend.repository.CargoRepository;

@Service
@Transactional
public class CargoService {

    @Autowired
    CargoRepository cargoRepository;

    public List<Cargo> list() {
        return cargoRepository.findAll();
    }

    public Optional<Cargo> findById(Long id) {
        return cargoRepository.findById((Long) id);
    }

    public Optional<Cargo> getByNombre(String nombre) {
        return cargoRepository.findByNombre(nombre);
    }

    public void save(Cargo cargo) {
        cargoRepository.save(cargo);

    }

    public void deleteById(Long id) {
        cargoRepository.deleteById((Long) id);
    }

    public boolean existsById(Long id) {
        return cargoRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return cargoRepository.existsByNombre(nombre);
    }

}

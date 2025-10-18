package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.CargoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Cargo;
import com.guardias.backend.repository.CargoRepository;

@Service
@Transactional
public class CargoService {

    @Autowired
    CargoRepository cargoRepository;

    public Optional<List<Cargo>> findByActivoTrue() {
        return cargoRepository.findByActivoTrue();
    }

    public List<Cargo> findAll() {
        return cargoRepository.findAll();
    }

    public Optional<Cargo> findById(Long id) {
        return cargoRepository.findById(id);
    }

    public Optional<Cargo> findByNombre(String nombre) {
        return cargoRepository.findByNombre(nombre);
    }

    public boolean existsById(Long id) {
        return cargoRepository.existsById(id);
    }

    public boolean activoByNombre(String nombre) {
        return (cargoRepository.existsByNombre(nombre) && cargoRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean existsByNombre(String nombre) {
        return cargoRepository.existsByNombre(nombre);
    }

    public boolean activo(Long id) {
        return (cargoRepository.existsById(id) && cargoRepository.findById(id).get().isActivo());

    }

    public boolean activoById(Long id) {
        return cargoRepository.existsById(id) && cargoRepository.findById(id).get().isActivo();
    }

    public void save(Cargo cargo) {
        cargoRepository.save(cargo);
    }

    public void deleteById(Long id) {
        cargoRepository.deleteById(id);
    }

    public ResponseEntity<?> validations(CargoDto cargoDto, Long id) {
        if (cargoDto.getNombre() == null)
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getDescripcion() == null)
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);

        if (activoByNombre(cargoDto.getNombre())
                && (findByNombre(cargoDto.getNombre()).get().getId() != id))
            return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    public Cargo createUpdate(Cargo cargo, CargoDto cargoDto) {

        if (cargoDto.getNombre() != null && !cargoDto.getNombre().isEmpty()
                && !cargoDto.getNombre().equals(cargo.getNombre()))
            cargo.setNombre(cargoDto.getNombre());

        if (cargoDto.getDescripcion() != null && !cargoDto.getDescripcion().isEmpty()
                && !cargoDto.getDescripcion().equals(cargo.getDescripcion()))
            cargo.setDescripcion(cargoDto.getDescripcion());

        cargo.setActivo(true);
        return cargo;
    }
}

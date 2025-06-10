package com.guardias.backend.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.FacturaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Factura;
import com.guardias.backend.repository.FacturaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FacturaService {

    private final AsistencialService asistencialService;
    
    @Autowired
    FacturaRepository facturaRepository;

    FacturaService(AsistencialService asistencialService) {
        this.asistencialService = asistencialService;
    }

    public Optional<List<Factura>> findByActivoTrue() {
        return facturaRepository.findByActivoTrue();
    }

    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> findById(Long id) {
        return facturaRepository.findById(id);
    }

    public boolean activo(Long id) {
        return (facturaRepository.existsById(id)
                && facturaRepository.findById(id).get().isActivo());
    }

    public boolean activoByAsistencial(Long idAsistencial) {
        return (facturaRepository.existsByAsistencialId(idAsistencial)
                && facturaRepository.findByAsistencialId(idAsistencial).get().isActivo());
    }

    public Optional<Factura> findByAsistencial(Long idAsistencial) {
        return facturaRepository.findByAsistencialId(idAsistencial);
    }

    public ResponseEntity<?> validations(FacturaDto facturaDto) {
        if (facturaDto.getIdAsistencial() == null)
            return new ResponseEntity(new Mensaje("el id delasistencial es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getContribuyente() == null)
            return new ResponseEntity(new Mensaje("es obligatorio ingresar contribuyente"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getTipo() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar el tipo"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getPuntoVenta() == null)
            return new ResponseEntity(new Mensaje("es obligatorio ingresar punto de venta"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getNumeroFactura() == null)
            return new ResponseEntity(new Mensaje("es obligatorio ingresar el numero de factura"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getFechaEmision() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar la fecha de emision"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getMonto() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar el monto"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Factura createUpdate(Factura factura,
            FacturaDto facturaDto) {

        if (factura.getAsistencial() == null || !Objects
                .equals(factura.getAsistencial().getId(), facturaDto.getIdAsistencial()))
            factura.setAsistencial(asistencialService.findById(facturaDto.getIdAsistencial()).get());

        if (factura.getContribuyente() != facturaDto.getContribuyente() &&
                facturaDto.getContribuyente() != null)
            factura.setContribuyente(facturaDto.getContribuyente());

        if (factura.getTipo() != facturaDto.getTipo() &&
                facturaDto.getTipo() != null)
            factura.setTipo(facturaDto.getTipo());

        if (factura.getPuntoVenta() != facturaDto.getPuntoVenta() &&
                facturaDto.getPuntoVenta() != null)
            factura.setPuntoVenta(facturaDto.getPuntoVenta());

        if (factura.getNumeroFactura() != facturaDto.getNumeroFactura() &&
                facturaDto.getNumeroFactura() != null)
            factura.setNumeroFactura(facturaDto.getNumeroFactura());

        if (factura.getFechaEmision() != facturaDto.getFechaEmision() &&
                facturaDto.getFechaEmision() != null)
            factura.setFechaEmision(facturaDto.getFechaEmision());

        if (factura.getMonto() != facturaDto.getMonto() &&
                facturaDto.getMonto() != null)
            factura.setMonto(facturaDto.getMonto());

        factura.setActivo(true);
        return factura;
    }

    public void save(Factura factura) {
        facturaRepository.save(factura);
    }

    public void deleteById(Long id) {
        facturaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return facturaRepository.existsById(id);
    }

}

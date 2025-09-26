package com.guardias.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.FacturaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialDetailDto;
import com.guardias.backend.dto.factura.FacturaDetailDto;
import com.guardias.backend.dto.factura.FacturaSummaryDto;
import com.guardias.backend.entity.Factura;
import com.guardias.backend.entity.Person;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;
import com.guardias.backend.repository.FacturaRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FacturaService {

    private final AsistencialService asistencialService;

    @Autowired
    FacturaRepository facturaRepository;
    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    EfectorService efectorService;

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
        if (facturaDto.getIdRegistrosMensuales() == null)
            return new ResponseEntity(new Mensaje("la lista de id de registros mensuales no debe ser nula"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getNombreTitular() == null)
            return new ResponseEntity(new Mensaje("es obligatorio ingresar el nombre del titular"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getApellidoTitular() == null)
            return new ResponseEntity(new Mensaje("es obligatorio ingresar el apellido del titular"),
                    HttpStatus.BAD_REQUEST);
        if (facturaDto.getDniTitular() < 1000000)
            return new ResponseEntity(new Mensaje("DNI es incorrecto"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(facturaDto.getCuilTitular()))
            return new ResponseEntity(new Mensaje("El Cuil es obligatorio"),
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

        if (facturaDto.getIdRegistrosMensuales() != null) {

            if (factura.getRegistrosMensuales() == null) {
                factura.setRegistrosMensuales(new ArrayList<>());
            }

            // Crea una nueva lista para almacenar los registros mensuales actualizados
            List<RegistroMensual> rMActualizados = new ArrayList<>();

            // Primero: remover la factura de los registros mensuales que ya no están en la
            // lista
            for (RegistroMensual rm : factura.getRegistrosMensuales()) {
                if (facturaDto.getIdRegistrosMensuales().contains(rm.getId())) {
                    rMActualizados.add(rm);
                } else {
                    // Remover la factura del registro mensual
                    rm.getFacturas().remove(factura);
                }
            }
            factura.setRegistrosMensuales(rMActualizados);

            // Segundo: agrega nuevos registros mensuales si no estan presentes
            for (Long id : facturaDto.getIdRegistrosMensuales()) {
                boolean found = false;
                for (RegistroMensual rm : factura.getRegistrosMensuales()) {
                    if (rm.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    RegistroMensual rmToAdd = registroMensualService.findById(id).get();
                    if (rmToAdd != null) {
                        factura.getRegistrosMensuales().add(rmToAdd);
                        rmToAdd.getFacturas().add(factura);
                    } else {
                        throw new RuntimeException("No se encontró el registro mensual con ID: " + id);
                    }
                }
            }
        }

        if (facturaDto.getNombreTitular() != null && !facturaDto.getNombreTitular().isEmpty()) {
            // Si la factura es nueva (nombreTitular es null) o si el valor es diferente
            if (factura.getNombreTitular() == null
                    || !factura.getNombreTitular().equals(facturaDto.getNombreTitular())) {
                factura.setNombreTitular(facturaDto.getNombreTitular());
            }
        }

        // Para apellidoTitular (aplica la misma lógica)
        if (facturaDto.getApellidoTitular() != null && !facturaDto.getApellidoTitular().isEmpty()) {
            if (factura.getApellidoTitular() == null
                    || !factura.getApellidoTitular().equals(facturaDto.getApellidoTitular())) {
                factura.setApellidoTitular(facturaDto.getApellidoTitular());
            }
        }

        if (facturaDto.getDniTitular() != factura.getDniTitular())
            factura.setDniTitular(facturaDto.getDniTitular());

        if (facturaDto.getCuilTitular() != null && !facturaDto.getCuilTitular().equals(factura.getCuilTitular())
                && !facturaDto.getCuilTitular().isEmpty())
            factura.setCuilTitular(facturaDto.getCuilTitular());

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

    public BigDecimal getMontoByQuincena(Long idAsistencial, Long idEfector, QuincenaEnum quincena, MesesEnum mes,
            int anio) {
        return facturaRepository.sumMontoByAsistencialEfectorQuincenaMesAnio(idAsistencial, idEfector, quincena, mes,
                anio);
    }

    public List<FacturaSummaryDto> getFacturasByAnioMesQuincena(int idEfector, int anio, MesesEnum mes,
            QuincenaEnum quincena) {
        List<Factura> facturas = facturaRepository.findByAnioMesQuincena(idEfector, anio, mes, quincena);
        return facturas.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    private FacturaSummaryDto convertToSummaryDto(Factura factura) {
        FacturaSummaryDto dto = new FacturaSummaryDto();
        dto.setId(factura.getId());
        dto.setAsistencial(convertToAsistencialDetailDto(factura.getAsistencial()));

        return dto;
    }

    private AsistencialDetailDto convertToAsistencialDetailDto(Person asistencial) {
        if (asistencial == null) {
            return null;
        }
        return new AsistencialDetailDto(
                asistencial.getId(),
                asistencial.getNombre(),
                asistencial.getApellido(),
                asistencial.getCuil());
    }

    public List<FacturaDetailDto> getByFiltros(Long idAsistencial, Long idEfector, int anio, MesesEnum mes,
            QuincenaEnum quincena) {
        List<Factura> facturas = facturaRepository.findByFiltros(idAsistencial, idEfector, anio, mes, quincena);
        return facturas.stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());
    }

    private FacturaDetailDto convertToDetailDto(Factura factura) {
        FacturaDetailDto dto = new FacturaDetailDto();
        dto.setId(factura.getId());
        dto.setNombreTitular(factura.getNombreTitular());
        dto.setApellidoTitular(factura.getApellidoTitular());
        dto.setDniTitular(factura.getDniTitular());
        dto.setCuilTitular(factura.getCuilTitular());
        dto.setContribuyente(factura.getContribuyente());
        dto.setTipo(factura.getTipo());
        dto.setPuntoVenta(factura.getPuntoVenta());
        dto.setNumeroFactura(factura.getNumeroFactura());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setMonto(factura.getMonto());
        return dto;
    }

    public boolean existeFacturaByFiltros(Long idAsistencial, Long idEfector, int anio, MesesEnum mes,
            QuincenaEnum quincena) {
        return facturaRepository.existsByAsistencialAndEfectorAndAnioMesQuincena(idAsistencial, idEfector, anio, mes,
                quincena);
    }

    public boolean existenDosFacturasByFiltros(Long idAsistencial, Long idEfector, int anio,
            MesesEnum mes, QuincenaEnum quincena) {

        // Contar facturas activas para el asistencial en el efector, mes, año y
        // quincena
        long cantidadFacturas = facturaRepository.countByAsistencialAndEfectorAndPeriodo(
                idAsistencial, idEfector, anio, mes, quincena);

        // Devolver true si hay exactamente 2 facturas
        return cantidadFacturas == 2;
    }


}

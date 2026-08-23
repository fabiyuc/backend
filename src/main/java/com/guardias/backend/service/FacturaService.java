package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.guardias.backend.enums.EstadoFacturacionEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.PeriodoCargaEnum;
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

        return new ResponseEntity(new Mensaje("valido en validation"), HttpStatus.OK);
    }

    /**
     * Determinar periodoCarga según fecha sistema y datos del RM
     */
    public PeriodoCargaEnum determinarPeriodoCarga(FacturaDto facturaDto) {
        // 1. Obtener registro mensual
        Long idRegistro = facturaDto.getIdRegistrosMensuales().get(0);
        RegistroMensual registro = registroMensualService.findById(idRegistro)
                .orElseThrow(() -> new RuntimeException("Registro mensual no encontrado"));

        LocalDate fechaSistema = LocalDate.now();
        // QuincenaEnum quincenaRegistro = registro.getQuincena();
        int mesRegistro = convertirMesANumero(registro.getMes());
        int anioRegistro = registro.getAnio();

        System.out.println("=== DEBUG PERIODO CARGA ===");
        System.out.println("Fecha sistema: " + fechaSistema);
        // System.out.println("Quincena registro: " + quincenaRegistro);
        System.out.println("Mes registro: " + registro.getMes() + " " + anioRegistro);

        // ETAPA 1: Determinar si está EN TÉRMINO
        boolean enTermino = estaEnTermino(fechaSistema, mesRegistro, anioRegistro);

        if (enTermino) {
            System.out.println("🕒 Periodo carga: EN_TERMINO");
            return PeriodoCargaEnum.EN_TERMINO;
        }

        /*
         * // ETAPA 2: Si está EN TÉRMINO, determinar quincena específica
         * if (quincenaRegistro == QuincenaEnum.PRIMERA) {
         * LocalDate limitePrimera = LocalDate.of(anioRegistro, mesRegistro, 21);
         * if (fechaSistema.isBefore(limitePrimera)) {
         * System.out.println("Periodo carga: PRIMERA (en término)");
         * return QuincenaEnum.PRIMERA;
         * }
         * } else if (quincenaRegistro == QuincenaEnum.SEGUNDA) {
         * LocalDate primerDiaMesSiguiente = LocalDate.of(anioRegistro, mesRegistro,
         * 1).plusMonths(1);
         * LocalDate limiteSegunda = primerDiaMesSiguiente.withDayOfMonth(6);
         * if (fechaSistema.isBefore(limiteSegunda)) {
         * System.out.println("Periodo carga: SEGUNDA (en término)");
         * return QuincenaEnum.SEGUNDA;
         * }
         * }
         */

        System.out.println("🕒 Periodo carga: FUERA_DE_TERMINO");
        return PeriodoCargaEnum.FUERA_DE_TERMINO;
    }

    private boolean estaEnTermino(LocalDate fechaSistema, int mesRegistro, int anioRegistro) {
        // Rango en término: desde primer día del mes registro hasta día 5 del mes
        // siguiente
        LocalDate inicioTermino = LocalDate.of(anioRegistro, mesRegistro, 1);
        LocalDate finTermino = LocalDate.of(anioRegistro, mesRegistro, 1)
                .plusMonths(1)
                .withDayOfMonth(5);

        System.out.println("Rango en término: " + inicioTermino + " a " + finTermino);

        return !fechaSistema.isBefore(inicioTermino) && !fechaSistema.isAfter(finTermino);
    }

    /**
     * Validar cantidad de facturas según periodoCarga
     */
    public ResponseEntity<?> validarCantidadFacturas(FacturaDto facturaDto, PeriodoCargaEnum periodoCarga) {
        try {
            Long idRegistro = facturaDto.getIdRegistrosMensuales().get(0);
            RegistroMensual registro = registroMensualService.findById(idRegistro)
                    .orElseThrow(() -> new RuntimeException("Registro mensual no encontrado"));

            int cantidadFacturas;

            if (periodoCarga == PeriodoCargaEnum.EN_TERMINO) {
                cantidadFacturas = facturaRepository.countFacturasPorPeriodo(
                        registro.getEfector().getId(),
                        registro.getAsistencial().getId(),
                        registro.getMes(),
                        registro.getAnio());
            } else {
                cantidadFacturas = facturaRepository.countFacturasParaRegistrosPendientes(
                        registro.getEfector().getId(),
                        registro.getAsistencial().getId(),
                        registro.getMes(),
                        registro.getAnio(),
                        EstadoFacturacionEnum.PENDIENTE);
            }

            System.out.println("=== DEBUG CANTIDAD FACTURAS ===");
            System.out.println("Periodo carga: " + periodoCarga);
            System.out.println("Cantidad facturas existentes: " + cantidadFacturas);

            if (cantidadFacturas >= 4) {
                return new ResponseEntity(
                        new Mensaje("Ya existen " + cantidadFacturas + " facturas para " +
                                registro.getMes() + " " + registro.getAnio() + " (" + periodoCarga + ")"),
                        HttpStatus.BAD_REQUEST);
            }

            // --- Alternativa sin límite (pendiente de confirmar con mandy, lunes) ---
            // Si mandy decide que no hay tope, se elimina el bloque if de arriba.

            return new ResponseEntity(new Mensaje("Validación cantidad OK"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Error validando cantidad: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validar montos según periodoCarga
     */
    public ResponseEntity<?> validarMontosFacturas(FacturaDto facturaDto, PeriodoCargaEnum periodoCarga) {
        try {
            Long idRegistro = facturaDto.getIdRegistrosMensuales().get(0);
            RegistroMensual registro = registroMensualService.findById(idRegistro)
                    .orElseThrow(() -> new RuntimeException("Registro mensual no encontrado"));

            BigDecimal montoFacturasExistentes;
            BigDecimal montoRegistro;

            if (periodoCarga == PeriodoCargaEnum.EN_TERMINO) {
                // Para EN_TERMINO: usar registros completos
                montoFacturasExistentes = facturaRepository.sumMontosFacturasPorPeriodo(
                        registro.getEfector().getId(),
                        registro.getAsistencial().getId(),
                        registro.getMes(),
                        registro.getAnio());

                montoRegistro = registro.getTotalHoras().getMontoTotal();
            } else {
                // Para FUERA_DE_TERMINO: usar registros pendientes
                montoFacturasExistentes = facturaRepository.sumMontosFacturasParaRegistrosPendientes(
                        registro.getEfector().getId(),
                        registro.getAsistencial().getId(),
                        registro.getMes(),
                        registro.getAnio(),
                        EstadoFacturacionEnum.PENDIENTE);

                montoRegistro = BigDecimal.ZERO;
                List<RegistroMensual> registrosPendientes = registroMensualService.findRegistrosPendientes(
                        registro.getEfector().getId(),
                        registro.getAsistencial().getId(),
                        registro.getMes(),
                        registro.getAnio());

                for (RegistroMensual rm : registrosPendientes) {
                    montoRegistro = montoRegistro.add(rm.getTotalHoras().getMontoTotal());
                }
            }

            // Validar que no exceda considerando margen de ±0.10
            BigDecimal sumaTotal = montoFacturasExistentes.add(facturaDto.getMonto());
            BigDecimal margenPermitido = new BigDecimal("0.10");

            // Calcular la diferencia absoluta
            BigDecimal diferencia = sumaTotal.subtract(montoRegistro).abs();

            // Calcular la diferencia absoluta para la validación
            BigDecimal diferenciaAbsoluta = diferencia.abs();

            System.out.println("=== DEBUG VALIDACIÓN MONTOS ===");
            System.out.println("Periodo carga: " + periodoCarga);
            System.out.println("Monto facturas existentes: " + montoFacturasExistentes);
            System.out.println("Monto nueva factura: " + facturaDto.getMonto());
            System.out.println("Monto registro(s): " + montoRegistro);
            System.out.println("Suma total: " + sumaTotal);
            System.out.println("Diferencia: " + diferencia);
            System.out.println("Diferencia absoluta: " + diferenciaAbsoluta);
            System.out.println("Margen permitido: " + margenPermitido);

            // Validar si la diferencia absoluta es mayor al margen permitido (0.10)
            if (diferenciaAbsoluta.compareTo(margenPermitido) > 0) {
                String mensajeError = String.format(
                        "Monto fuera del rango permitido para %s. Total registros: %s, Facturas existentes: %s, Nueva factura: %s, Suma total: %s, Diferencia: %s (Margen permitido: ±%s)",
                        periodoCarga, montoRegistro, montoFacturasExistentes, facturaDto.getMonto(), sumaTotal,
                        diferencia, margenPermitido);
                return new ResponseEntity(new Mensaje(mensajeError), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity(new Mensaje("Validación montos OK"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Error validando montos: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualizar estado de facturación según periodoCarga
     */
    public void actualizarEstadoFacturacion(Factura factura, PeriodoCargaEnum periodoCarga) {
        RegistroMensual registroBase = factura.getRegistrosMensuales().get(0);

        BigDecimal montoFacturasExistentes;
        BigDecimal montoRegistro;
        List<RegistroMensual> registrosAActualizar;

        if (periodoCarga == PeriodoCargaEnum.EN_TERMINO) {
            montoFacturasExistentes = facturaRepository.sumMontosFacturasPorPeriodo(
                    registroBase.getEfector().getId(),
                    registroBase.getAsistencial().getId(),
                    registroBase.getMes(),
                    registroBase.getAnio());

            montoRegistro = registroBase.getTotalHoras().getMontoTotal();

            registrosAActualizar = registroMensualService.findByEfectorAndAsistencialAndMesAndAnio(
                    registroBase.getEfector().getId(),
                    registroBase.getAsistencial().getId(),
                    registroBase.getMes(),
                    registroBase.getAnio());
        } else {
            montoFacturasExistentes = facturaRepository.sumMontosFacturasParaRegistrosPendientes(
                    registroBase.getEfector().getId(),
                    registroBase.getAsistencial().getId(),
                    registroBase.getMes(),
                    registroBase.getAnio(),
                    EstadoFacturacionEnum.PENDIENTE);

            registrosAActualizar = registroMensualService.findRegistrosPendientes(
                    registroBase.getEfector().getId(),
                    registroBase.getAsistencial().getId(),
                    registroBase.getMes(),
                    registroBase.getAnio());

            // Calcular monto total de registros pendientes
            montoRegistro = BigDecimal.ZERO;
            for (RegistroMensual rm : registrosAActualizar) {
                montoRegistro = montoRegistro.add(rm.getTotalHoras().getMontoTotal());
            }
        }

        // Determinar estado según lógica de negocio
        EstadoFacturacionEnum nuevoEstado;

        if (montoFacturasExistentes.compareTo(montoRegistro) < 0) {
            nuevoEstado = EstadoFacturacionEnum.PENDIENTE;// Faltan facturas
        } else {
            if (periodoCarga == PeriodoCargaEnum.FUERA_DE_TERMINO) {
                nuevoEstado = EstadoFacturacionEnum.REGULARIZADO;// Completado fuera de plazo
            } else {
                nuevoEstado = EstadoFacturacionEnum.COMPLETADO;// Completado en plazo
            }
        }

        // Actualizar registros
        for (RegistroMensual rm : registrosAActualizar) {
            rm.setEstadoFacturacion(nuevoEstado);

            System.out.println("=== DEBUG ACTUALIZACIÓN ESTADO ===");
            // System.out.println("Registro ID: " + rm.getId() + " - Quincena: " +
            // rm.getQuincena());
            System.out.println("Periodo carga: " + periodoCarga);
            System.out.println("Monto registros: " + montoRegistro);
            System.out.println("Monto facturas: " + montoFacturasExistentes);
            System.out.println("Nuevo estado: " + nuevoEstado);
            System.out.println("==================================");
        }

        registroMensualService.saveAll(registrosAActualizar);
    }

    public Factura createUpdate(Factura factura,
            FacturaDto facturaDto) {

        if (factura.getAsistencial() == null || !Objects
                .equals(factura.getAsistencial().getId(), facturaDto.getIdAsistencial()))
            factura.setAsistencial(asistencialService.findById(facturaDto.getIdAsistencial()).get());

        if (factura.getMonto() != facturaDto.getMonto() &&
                facturaDto.getMonto() != null)
            factura.setMonto(facturaDto.getMonto());

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

        factura.setActivo(true);

        return factura;
    }

    /**
     * Calcula fecha límite según quincena
     */
    /*
     * private LocalDate calcularFechaLimiteSegunQuincena(RegistroMensual registro)
     * {
     * int numeroMes = convertirMesANumero(registro.getMes());
     * int anio = registro.getAnio();
     * 
     * QuincenaEnum quincena = registro.getQuincena();
     * 
     * if (quincena == QuincenaEnum.PRIMERA) {
     * // Primera quincena: límite hasta el 20 del mismo mes
     * return LocalDate.of(anio, numeroMes, 20);
     * } else {
     * // Segunda quincena o completo: límite hasta el 5 del mes siguiente
     * LocalDate fechaBase = LocalDate.of(anio, numeroMes, 1);
     * return fechaBase.plusMonths(1).withDayOfMonth(5);
     * }
     * }
     */

    /**
     * Calcula la fecha límite (día 5 del mes siguiente al registro) para determinar
     * si una regularización de facturas ocurre dentro o fuera de término.
     */
    private LocalDate calcularFechaLimite(RegistroMensual registro) {
        int numeroMes = convertirMesANumero(registro.getMes());
        int anio = registro.getAnio();

        LocalDate fechaBase = LocalDate.of(anio, numeroMes, 1);
        return fechaBase.plusMonths(1).withDayOfMonth(5);
    }

    /**
     * Convierte MesesEnum a número
     */
    private int convertirMesANumero(MesesEnum mes) {

        return mes.getNumeroMes();
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

    /* public BigDecimal getMontoByQuincena(Long idAsistencial, Long idEfector, QuincenaEnum quincena, MesesEnum mes,
            int anio) {
        return facturaRepository.sumMontoByAsistencialEfectorQuincenaMesAnio(idAsistencial, idEfector, quincena, mes,
                anio);
    } */

    public BigDecimal getMonto(Long idAsistencial, Long idEfector, MesesEnum mes, int anio) {
        return facturaRepository.sumMontoByAsistencialEfectorMesAnio(idAsistencial, idEfector, mes, anio);
    }

    public BigDecimal getMontoFueraTermino(Long idAsistencial, Long idEfector, MesesEnum mes, int anio) {
        // Lista de estados que queremos buscar
        List<EstadoFacturacionEnum> estadosBuscados = Arrays.asList(
                EstadoFacturacionEnum.PENDIENTE,
                EstadoFacturacionEnum.REGULARIZADO);
        return facturaRepository.sumMontoByAsistencialEfectorMesAnioEstadoFacturacion(idAsistencial, idEfector, mes,
                anio, estadosBuscados);
    }

   /*  public List<FacturaSummaryDto> getFacturasByAnioMesQuincena(int idEfector, int anio, MesesEnum mes,
            QuincenaEnum quincena) {
        List<Factura> facturas = facturaRepository.findByAnioMesQuincena(idEfector, anio, mes, quincena);
        return facturas.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    } */

    public List<FacturaSummaryDto> getFacturasByAnioMes(int idEfector, int anio, MesesEnum mes) {
        List<Factura> facturas = facturaRepository.findByAnioMes(idEfector, anio, mes);
        return facturas.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    public List<FacturaDetailDto> getFacturasByAsistencial(int idEfector, int anio, MesesEnum mes,
            int idAsistencial) {
        List<Factura> facturas = facturaRepository.findByAsistencialYfiltros(idEfector, anio, mes, idAsistencial);
        return facturas.stream()
                .map(this::convertToDetailDto)
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

    public List<FacturaDetailDto> getByFiltros(Long idAsistencial, Long idEfector, int anio, MesesEnum mes) {
        List<Factura> facturas = facturaRepository.findByFiltros(idAsistencial, idEfector, anio, mes);
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

    public boolean existeFacturaByFiltros(Long idAsistencial, Long idEfector, int anio, MesesEnum mes) {
        return facturaRepository.existsByAsistencialAndEfectorAndAnioMes(idAsistencial, idEfector, anio, mes);
    }

   /*  public boolean existenDosFacturasByFiltros(Long idAsistencial, Long idEfector, int anio,
            MesesEnum mes, QuincenaEnum quincena) {

        // Contar facturas activas para el asistencial en el efector, mes, año y
        // quincena
        long cantidadFacturas = facturaRepository.countByAsistencialAndEfectorAndPeriodo(
                idAsistencial, idEfector, anio, mes, quincena);

        // Devolver true si hay exactamente 2 facturas
        return cantidadFacturas == 2;
    } */

    public boolean existeFacturaByFiltrosSinQuincena(Long idAsistencial, Long idEfector, int anio, MesesEnum mes) {
        return facturaRepository.existsByAsistencialAndEfectorAndAnioMes(idAsistencial, idEfector, anio, mes);
    }

    public boolean existenDosFacturasByFiltrosSinQuincena(Long idAsistencial, Long idEfector, int anio,
            MesesEnum mes) {

        // Contar facturas activas para el asistencial en el efector, mes, año y
        // quincena
        long cantidadFacturas = facturaRepository.countByAsistencialAndEfectorAndPeriodoSinQuincena(
                idAsistencial, idEfector, anio, mes);

        // Devolver true si hay exactamente 2 facturas
        return cantidadFacturas == 2;
    }

    public void actualizarEstadoFacturasDespuesDeEliminar(List<RegistroMensual> registrosAfectados) {

        // Obtener la fecha actual para determinar si estamos dentro o fuera de término
        LocalDate fechaActual = LocalDate.now();

        for (RegistroMensual registro : registrosAfectados) {
            BigDecimal montoTotalEsperado = registro.getTotalHoras().getMontoTotal();

            // Obtener el monto de las facturas ACTIVAS que quedan
            BigDecimal montoFacturasExistentes = facturaRepository.sumMontoFacturasExistentesPorPeriodo(
                    registro.getEfector().getId(),
                    registro.getAsistencial().getId(),
                    registro.getMes(),
                    //registro.getQuincena(),
                    registro.getAnio());

            // Verificar si quedan facturas y si completan el monto
            boolean quedanFacturas = montoFacturasExistentes.compareTo(BigDecimal.ZERO) > 0;
            boolean facturasCompletas = quedanFacturas && montoFacturasExistentes.compareTo(montoTotalEsperado) == 0;

            // Determinar si estamos dentro o fuera del plazo
            boolean esFueraDeTermino = determinarSiEsFueraDeTerminoParaRegistro(registro, fechaActual);

            // Determinar el nuevo estado según el enum
            EstadoFacturacionEnum nuevoEstado;

            if (!quedanFacturas) {
                // No hay facturas activas → PENDIENTE
                nuevoEstado = EstadoFacturacionEnum.PENDIENTE;
            } else if (facturasCompletas) {
                // Hay facturas y están completas → determinar si es a tiempo o fuera de tiempo
                nuevoEstado = esFueraDeTermino ? EstadoFacturacionEnum.REGULARIZADO : EstadoFacturacionEnum.COMPLETADO;
            } else {
                // Hay facturas pero no completan el monto → PENDIENTE
                nuevoEstado = EstadoFacturacionEnum.PENDIENTE;
            }

            registro.setEstadoFacturacion(nuevoEstado);

            System.out.println("=== DEBUG ELIMINACIÓN FACTURA ===");
            System.out.println("Registro ID: " + registro.getId());
            System.out.println("Monto esperado: " + montoTotalEsperado);
            System.out.println("Monto facturas existentes: " + montoFacturasExistentes);
            System.out.println("Quedan facturas: " + quedanFacturas);
            System.out.println("Facturas completas: " + facturasCompletas);
            System.out.println("Fecha actual: " + fechaActual);
            System.out.println("Es fuera de término: " + esFueraDeTermino);
            System.out.println("Nuevo estado: " + nuevoEstado);
            System.out.println("================================");
        }

        registroMensualService.saveAll(registrosAfectados);
    }

    /**
     * Determina si para un registro específico estamos fuera del término
     */
    private boolean determinarSiEsFueraDeTerminoParaRegistro(RegistroMensual registro, LocalDate fechaActual) {
        try {
            LocalDate fechaLimite = calcularFechaLimite(registro);
            return fechaActual.isAfter(fechaLimite);
        } catch (Exception e) {
            System.err.println(
                    "Error al determinar fecha límite para registro " + registro.getId() + ": " + e.getMessage());
            return false; // Por defecto, asumir dentro del término
        }
    }

}

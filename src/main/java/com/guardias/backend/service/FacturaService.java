package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.guardias.backend.enums.EstadoFacturacionEnum;
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

        return new ResponseEntity(new Mensaje("valido en validation"), HttpStatus.OK);
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

    public ResponseEntity<?> validarCompletitudAntesDeGuardar(FacturaDto facturaDto) {
        try {
            // 1. Obtener el ID del registro mensual activo
            Long idRegistroActivo = facturaDto.getIdRegistrosMensuales().get(0); // Primer y único registro activo
            RegistroMensual rmActivo = registroMensualService.findByIdAndActivoTrue(idRegistroActivo).get();

            // 2. Obtener monto total esperado del registro mensual
            BigDecimal montoTotalEsperado = rmActivo.getTotalHoras().getMontoTotal();
            BigDecimal montoFacturaDto = facturaDto.getMonto();

            // 3. Buscar facturas existentes con mismo efector, mes, quincena, año y
            // asistencial
            BigDecimal montoFacturasExistentes = facturaRepository.sumMontoFacturasExistentes(
                    rmActivo.getEfector().getId(),
                    rmActivo.getAsistencial().getId(),
                    rmActivo.getMes(),
                    rmActivo.getQuincena(),
                    rmActivo.getAnio());

            System.out.println("=== DEBUG VALIDACIÓN ===");
            System.out.println("Monto total esperado: " + montoTotalEsperado);
            System.out.println("Monto nueva factura: " + montoFacturaDto);
            System.out.println("Monto facturas existentes (BD): " + montoFacturasExistentes);
            System.out.println("Suma total: " + montoFacturasExistentes.add(montoFacturaDto));
            System.out.println("=========================");

            // 4. Calcular suma total
            BigDecimal sumaTotal = montoFacturasExistentes.add(montoFacturaDto);

            // 5. Validaciones según escenarios
            if (montoFacturasExistentes.compareTo(BigDecimal.ZERO) > 0) {
                // ESCENARIO 1: Existen facturas previas!!
                if (sumaTotal.compareTo(montoTotalEsperado) > 0) {
                    throw new RuntimeException("El monto total de facturas (" + sumaTotal + ") " +
                            "excede el monto esperado (" + montoTotalEsperado + ")");
                }

            } else {
                // ESCENARIO 2: No existen facturas previas!!
                if (montoFacturaDto.compareTo(montoTotalEsperado) > 0) {
                    throw new RuntimeException("El monto de la factura (" + montoFacturaDto + ") " +
                            "excede el monto esperado (" + montoTotalEsperado + ")");
                }
            }
            return new ResponseEntity(new Mensaje("Validación exitosa"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Error en validación: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Verifica que la fecha de emisión esté dentro del rango permitido para la quincena
     */
    public ResponseEntity<?> validarRangoFechasEmision(FacturaDto facturaDto) {
        try {
            // Obtener el registro mensual
            Long idRegistroActivo = facturaDto.getIdRegistrosMensuales().get(0);
            RegistroMensual rmActivo = registroMensualService.findByIdAndActivoTrue(idRegistroActivo)
                .orElseThrow(() -> new RuntimeException("Registro mensual no encontrado"));

            // Convertir fecha de emisión a LocalDate
            LocalDate fechaEmision = facturaDto.getFechaEmision();
            
            // Validar rango según quincena
            boolean fechaValida = validarRangoFechasPermitido(rmActivo, fechaEmision);
            
            if (!fechaValida) {
                String mensajeError = generarMensajeErrorRangoFechas(rmActivo.getQuincena(), rmActivo.getMes(), rmActivo.getAnio());
                return new ResponseEntity(new Mensaje(mensajeError), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity(new Mensaje("Fecha de emisión válida"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Error en validación de fecha: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Valida que la fecha de emisión esté dentro del rango permitido para la quincena
     */
    private boolean validarRangoFechasPermitido(RegistroMensual registro, LocalDate fechaEmision) {
        int numeroMes = convertirMesANumero(registro.getMes());
        int anio = registro.getAnio();
        
        QuincenaEnum quincena = registro.getQuincena();
        
        if (quincena == QuincenaEnum.PRIMERA) {
            // Primera quincena: emisión entre días 1-15 del mes
            LocalDate inicio = LocalDate.of(anio, numeroMes, 1);
            LocalDate fin = LocalDate.of(anio, numeroMes, 15);
            return (fechaEmision.isEqual(inicio) || fechaEmision.isAfter(inicio)) && 
                   (fechaEmision.isEqual(fin) || fechaEmision.isBefore(fin));
            
        } else if (quincena == QuincenaEnum.SEGUNDA) {
            // Segunda quincena: emisión entre días 16-fin de mes
            LocalDate inicio = LocalDate.of(anio, numeroMes, 16);
            LocalDate fin = LocalDate.of(anio, numeroMes, inicio.lengthOfMonth());
            return (fechaEmision.isEqual(inicio) || fechaEmision.isAfter(inicio)) && 
                   (fechaEmision.isEqual(fin) || fechaEmision.isBefore(fin));
            
        } else if (quincena == QuincenaEnum.COMPLETO) {
            // Completo: puede ser cualquier día del mes
            LocalDate inicio = LocalDate.of(anio, numeroMes, 1);
            LocalDate fin = LocalDate.of(anio, numeroMes, inicio.lengthOfMonth());
            return (fechaEmision.isEqual(inicio) || fechaEmision.isAfter(inicio)) && 
                   (fechaEmision.isEqual(fin) || fechaEmision.isBefore(fin));
            
        } else {
            throw new RuntimeException("Quincena no válida: " + quincena);
        }
    }

    /**
     * Genera mensaje de error específico según la quincena
     */
    private String generarMensajeErrorRangoFechas(QuincenaEnum quincena, MesesEnum mes, int anio) {
    int numeroMes = convertirMesANumero(mes);
    
    if (quincena == QuincenaEnum.PRIMERA) {
        return "La fecha de emisión para la PRIMERA quincena de " + mes + " debe estar entre el 1 y 15 de " + mes + " de " + anio;
    } else if (quincena == QuincenaEnum.SEGUNDA) {
        LocalDate fechaInicio = LocalDate.of(anio, numeroMes, 16);
        int ultimoDiaMes = fechaInicio.lengthOfMonth();
        return "La fecha de emisión para la SEGUNDA quincena de " + mes + " debe estar entre el 16 y " + ultimoDiaMes + " de " + mes + " de " + anio;
    } else if (quincena == QuincenaEnum.COMPLETO) {
        LocalDate primerDiaMes = LocalDate.of(anio, numeroMes, 1);
        int ultimoDiaMes = primerDiaMes.lengthOfMonth();
        return "La fecha de emisión para el mes COMPLETO de " + mes + " debe estar entre el 1 y " + ultimoDiaMes + " de " + mes + " de " + anio;
    } else {
        return "Quincena no válida";
    }
}

    /**
     * Determina si la factura se crea fuera de término
     */
    public boolean determinarSiEsFueraDeTermino(FacturaDto facturaDto) {
        try {
            Long idRegistroActivo = facturaDto.getIdRegistrosMensuales().get(0);
            RegistroMensual rmActivo = registroMensualService.findByIdAndActivoTrue(idRegistroActivo)
                .orElseThrow(() -> new RuntimeException("Registro mensual no encontrado"));

            LocalDate fechaLimite = calcularFechaLimiteSegunQuincena(rmActivo);
            LocalDate fechaActual = LocalDate.now();

            System.out.println("=== DEBUG FECHAS POR QUINCENA ===");
            System.out.println("Quincena: " + rmActivo.getQuincena());
            System.out.println("Fecha límite: " + fechaLimite);
            System.out.println("Fecha actual: " + fechaActual);
            System.out.println("Es fuera de término: " + fechaActual.isAfter(fechaLimite));
            System.out.println("================================");

            return fechaActual.isAfter(fechaLimite);

        } catch (Exception e) {
            System.err.println("Error al determinar fecha límite: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calcula fecha límite según quincena
     */
    private LocalDate calcularFechaLimiteSegunQuincena(RegistroMensual registro) {
        int numeroMes = convertirMesANumero(registro.getMes());
        int anio = registro.getAnio();
        
        QuincenaEnum quincena = registro.getQuincena();
        
        if (quincena == QuincenaEnum.PRIMERA) {
            // Primera quincena: límite hasta el 20 del mismo mes
            return LocalDate.of(anio, numeroMes, 20);
        } else {
            // Segunda quincena o completo: límite hasta el 10 del mes siguiente
            LocalDate fechaBase = LocalDate.of(anio, numeroMes, 1);
            return fechaBase.plusMonths(1).withDayOfMonth(10);
        }
    }

    /**
     * Convierte MesesEnum a número
     */
    private int convertirMesANumero(MesesEnum mes) {
        
        return mes.getNumeroMes(); 
    }
    
    public void actualizarEstadoFacturasDespuesDeGuardar(Factura factura, boolean esFueraDeTermino) {

    for (RegistroMensual registro : factura.getRegistrosMensuales()) {
        BigDecimal montoTotalEsperado = registro.getTotalHoras().getMontoTotal();
        
        BigDecimal montoFacturasExistentes = facturaRepository.sumMontoFacturasExistentes(
                registro.getEfector().getId(),
                registro.getAsistencial().getId(),
                registro.getMes(),
                registro.getQuincena(),
                registro.getAnio());

        boolean facturasCompletas = montoFacturasExistentes.compareTo(montoTotalEsperado) == 0;

        EstadoFacturacionEnum nuevoEstado;
        
        if (!facturasCompletas) {
            // Si no están completas, mantener PENDIENTE o el estado actual
            nuevoEstado = (registro.getEstadoFacturacion() == null) ? 
                EstadoFacturacionEnum.PENDIENTE : registro.getEstadoFacturacion();
        } else {
            // Si están completas, determinar si es COMPLETADO o REGULARIZADO
            nuevoEstado = esFueraDeTermino ? 
                EstadoFacturacionEnum.REGULARIZADO : EstadoFacturacionEnum.COMPLETADO;
        }

        registro.setEstadoFacturacion(nuevoEstado);

        System.out.println("=== DEBUG ESTADO FACTURACIÓN ===");
        System.out.println("Registro ID: " + registro.getId());
        System.out.println("Monto esperado: " + montoTotalEsperado);
        System.out.println("Monto facturas existentes: " + montoFacturasExistentes);
        System.out.println("Facturas completas: " + facturasCompletas);
        System.out.println("Es fuera de término: " + esFueraDeTermino);
        System.out.println("Nuevo estado: " + nuevoEstado);
        System.out.println("================================");
    }

    registroMensualService.saveAll(factura.getRegistrosMensuales());
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

    public void actualizarEstadoFacturasDespuesDeEliminar(List<RegistroMensual> registrosAfectados) {

        // Obtener la fecha actual para determinar si estamos dentro o fuera de término
        LocalDate fechaActual = LocalDate.now();

        for (RegistroMensual registro : registrosAfectados) {
            BigDecimal montoTotalEsperado = registro.getTotalHoras().getMontoTotal();

            // Obtener el monto de las facturas ACTIVAS que quedan
            BigDecimal montoFacturasExistentes = facturaRepository.sumMontoFacturasExistentes(
                    registro.getEfector().getId(),
                    registro.getAsistencial().getId(),
                    registro.getMes(),
                    registro.getQuincena(),
                    registro.getAnio());

            // Verificar si quedan facturas y si completan el monto
            boolean quedanFacturas = montoFacturasExistentes.compareTo(BigDecimal.ZERO) > 0;
            boolean facturasCompletas  = quedanFacturas && montoFacturasExistentes.compareTo(montoTotalEsperado) == 0;

            // Determinar si estamos dentro o fuera del plazo
            boolean esFueraDeTermino = determinarSiEsFueraDeTerminoParaRegistro(registro, fechaActual);

            // Determinar el nuevo estado según el enum
            EstadoFacturacionEnum nuevoEstado;

            if (!quedanFacturas) {
                // No hay facturas activas → PENDIENTE
                nuevoEstado = EstadoFacturacionEnum.PENDIENTE;
            } else if (facturasCompletas) {
                // Hay facturas y están completas → determinar si es a tiempo o fuera de tiempo
                nuevoEstado = esFueraDeTermino ? 
                EstadoFacturacionEnum.REGULARIZADO : EstadoFacturacionEnum.COMPLETADO;
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
            LocalDate fechaLimite = calcularFechaLimiteSegunQuincena(registro);
            return fechaActual.isAfter(fechaLimite);
        } catch (Exception e) {
            System.err.println("Error al determinar fecha límite para registro " + registro.getId() + ": " + e.getMessage());
            return false; // Por defecto, asumir dentro del término
        }
    }



}

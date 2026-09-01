package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.AsignacionHorasEfectorDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asignacionHorasEfector.DetalleAsignacionEfectorDto;
import com.guardias.backend.dto.asignacionHorasEfector.HorasDisponiblesEfectorDto;
import com.guardias.backend.dto.asignacionHorasEfector.ResumenAsignacionDto;
import com.guardias.backend.entity.AsignacionHorasEfector;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.repository.AsignacionHorasEfectorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AsignacionHorasEfectorService {

    @Autowired
    AsignacionHorasEfectorRepository asignacionHorasEfectorRepository;
    @Autowired
    LegajoService legajoService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    DistribucionHorariaService distribucionHorariaService;

    public ResponseEntity<?> validations(AsignacionHorasEfectorDto dto, Long id) {

        if (dto.getIdLegajo() == null)
            return new ResponseEntity<>(new Mensaje("El legajo es obligatorio"), HttpStatus.BAD_REQUEST);

        if (dto.getIdEfector() == null)
            return new ResponseEntity<>(new Mensaje("El efector es obligatorio"), HttpStatus.BAD_REQUEST);

        if (dto.getHorasAsignadas() == null || dto.getHorasAsignadas().compareTo(BigDecimal.ZERO) <= 0)
            return new ResponseEntity<>(new Mensaje("Las horas asignadas deben ser mayor a 0"),
                    HttpStatus.BAD_REQUEST);

        if (dto.getFechaInicio() == null)
            return new ResponseEntity<>(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        if (dto.getFechaFinalizacion() != null && dto.getFechaFinalizacion().isBefore(dto.getFechaInicio()))
            return new ResponseEntity<>(new Mensaje("La fecha de fin no puede ser anterior a la de inicio"),
                    HttpStatus.BAD_REQUEST);

        // --- Traer el legajo y su carga horaria total ---
        Optional<Legajo> legajoOpt = legajoService.findById(dto.getIdLegajo());
        if (legajoOpt.isEmpty())
            return new ResponseEntity<>(new Mensaje("El legajo no existe"), HttpStatus.BAD_REQUEST);
        Legajo legajo = legajoOpt.get();

        if (legajo.getRevista() == null || legajo.getRevista().getCargaHoraria() == null)
            return new ResponseEntity<>(new Mensaje("El legajo no tiene carga horaria definida"),
                    HttpStatus.BAD_REQUEST);
        BigDecimal cargaHorariaTotal = BigDecimal.valueOf(legajo.getRevista().getCargaHoraria().getCantidad());

        // --- El efector tiene que estar habilitado en legajo.efectores ---
        boolean efectorValido = legajo.getEfectores() != null && legajo.getEfectores().stream()
                .anyMatch(ef -> Objects.equals(ef.getId(), dto.getIdEfector()));

        if (!efectorValido)
            return new ResponseEntity<>(
                    new Mensaje("El efector no pertenece a la lista de efectores del legajo"),
                    HttpStatus.BAD_REQUEST);

        // --- ¿Ya existe una asignación de este mismo efector que pise este mes? ---
        List<AsignacionHorasEfector> solapadas = asignacionHorasEfectorRepository
                .findSolapadasMismoEfector(dto.getIdLegajo(), dto.getIdEfector(),
                        dto.getFechaInicio(), dto.getFechaFinalizacion())
                .stream()
                .filter(a -> !Objects.equals(a.getId(), id)) // excluye la propia fila si es edición
                .collect(Collectors.toList());

        if (!solapadas.isEmpty())
            return new ResponseEntity<>(
                    new Mensaje("Ya existe una asignación para este efector en ese período"),
                    HttpStatus.BAD_REQUEST);

        // --- Sumar lo asignado en TODOS los efectores del legajo para ese mismo
        // período ---
        List<AsignacionHorasEfector> activasEnRango = asignacionHorasEfectorRepository
                .findActivasEnRango(dto.getIdLegajo(), dto.getFechaInicio(), dto.getFechaFinalizacion())
                .stream()
                .filter(a -> !Objects.equals(a.getId(), id))
                .collect(Collectors.toList());

        BigDecimal sumaExistente = activasEnRango.stream()
                .map(AsignacionHorasEfector::getHorasAsignadas)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal nuevaSuma = sumaExistente.add(dto.getHorasAsignadas());

        // --- Regla dura: no puede superar el total. Puede ser menor (queda resto
        // pendiente) o igual. ---
        if (nuevaSuma.compareTo(cargaHorariaTotal) > 0) {
            BigDecimal disponible = cargaHorariaTotal.subtract(sumaExistente);
            return new ResponseEntity<>(
                    new Mensaje("Excede las horas totales del legajo (" + cargaHorariaTotal +
                            "hs). Disponible para ese período: " + disponible + "hs"),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Mensaje("valido"), HttpStatus.OK);
    }

    public AsignacionHorasEfector createUpdate(AsignacionHorasEfector asignacion, AsignacionHorasEfectorDto dto) {

        if (asignacion.getLegajo() == null ||
                !Objects.equals(asignacion.getLegajo().getId(), dto.getIdLegajo())) {
            asignacion.setLegajo(legajoService.findById(dto.getIdLegajo()).get());
        }

        if (asignacion.getEfector() == null ||
                !Objects.equals(asignacion.getEfector().getId(), dto.getIdEfector())) {
            asignacion.setEfector(efectorService.findById(dto.getIdEfector()));
        }

        asignacion.setHorasAsignadas(dto.getHorasAsignadas());
        asignacion.setFechaInicio(dto.getFechaInicio());
        asignacion.setFechaFinalizacion(dto.getFechaFinalizacion());
        asignacion.setActivo(true);

        return asignacion;
    }

    public void save(AsignacionHorasEfector asignacion) {
        asignacionHorasEfectorRepository.save(asignacion);
    }

    public Optional<AsignacionHorasEfector> findById(Long id) {
        return asignacionHorasEfectorRepository.findById(id);
    }

    public List<AsignacionHorasEfector> findByLegajoActivo(Long idLegajo) {
        return asignacionHorasEfectorRepository.findByLegajoIdAndActivoTrue(idLegajo);
    }

    public ResumenAsignacionDto resumenAsignacion(Long idLegajo, Integer anio, Integer mes) {

        Legajo legajo = legajoService.findById(idLegajo)
                .orElseThrow(() -> new IllegalArgumentException("El legajo no existe"));

        if (legajo.getRevista() == null || legajo.getRevista().getCargaHoraria() == null)
            throw new IllegalArgumentException("El legajo no tiene carga horaria definida");

        BigDecimal horasTotales = BigDecimal.valueOf(legajo.getRevista().getCargaHoraria().getCantidad());

        // Rango del mes consultado: primer y último día
        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        // findActivasEnRango ya filtra activo = true en el @Query
        List<AsignacionHorasEfector> asignacionesDelMes = asignacionHorasEfectorRepository
                .findActivasEnRango(idLegajo, inicioMes, finMes);

        BigDecimal horasAsignadas = asignacionesDelMes.stream()
                .map(AsignacionHorasEfector::getHorasAsignadas)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal horasSinAsignar = horasTotales.subtract(horasAsignadas);

        List<DetalleAsignacionEfectorDto> detalle = asignacionesDelMes.stream()
                .map(a -> new DetalleAsignacionEfectorDto(
                        a.getEfector().getId(),
                        a.getEfector().getNombre(),
                        a.getHorasAsignadas()))
                .collect(Collectors.toList());

        boolean completo = horasSinAsignar.compareTo(BigDecimal.ZERO) == 0;

        return new ResumenAsignacionDto(anio, mes, horasTotales, horasAsignadas, horasSinAsignar, completo, detalle);
    }

    public HorasDisponiblesEfectorDto horasDisponiblesEfector(Long idLegajo, Long idEfector,
            Integer anio, Integer mes) {

        Legajo legajo = legajoService.findById(idLegajo)
                .orElseThrow(() -> new IllegalArgumentException("El legajo no existe"));

        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        // Reusamos el mismo método que ya usa validations() para detectar solapamiento,
        // acá simplemente tomamos la única fila vigente de ese efector para ese mes
        AsignacionHorasEfector asignacion = asignacionHorasEfectorRepository
                .findSolapadasMismoEfector(idLegajo, idEfector, inicioMes, finMes)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay horas asignadas a este efector para ese período"));

        BigDecimal horasCargadas = distribucionHorariaService.sumarHorasCargadas(
                legajo.getPersona().getId(), idEfector, inicioMes, finMes);

        BigDecimal horasDisponibles = asignacion.getHorasAsignadas().subtract(horasCargadas);

        return new HorasDisponiblesEfectorDto(asignacion.getHorasAsignadas(), horasCargadas, horasDisponibles);
    }

    public boolean puedeModificar(Long id) {
        AsignacionHorasEfector asignacion = asignacionHorasEfectorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La asignación no existe"));

        BigDecimal horasCargadas = distribucionHorariaService.sumarHorasCargadas(
                asignacion.getLegajo().getPersona().getId(),
                asignacion.getEfector().getId(),
                asignacion.getFechaInicio(),
                asignacion.getFechaFinalizacion());

        return horasCargadas.compareTo(BigDecimal.ZERO) == 0;
    }

    public ResponseEntity<?> update(Long id, AsignacionHorasEfectorDto dto) {

        Optional<AsignacionHorasEfector> existenteOpt = asignacionHorasEfectorRepository.findById(id);
        if (existenteOpt.isEmpty())
            return new ResponseEntity<>(new Mensaje("No existe la asignación indicada"), HttpStatus.NOT_FOUND);

        AsignacionHorasEfector existente = existenteOpt.get();

        // 1. Regla dura: no se puede editar si ya hay distribuciones cargadas contra
        // esta cuota
        if (!puedeModificar(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No se puede editar: ya existen distribuciones horarias cargadas para este período"),
                    HttpStatus.BAD_REQUEST);
        }

        if (dto.getHorasAsignadas() == null || dto.getHorasAsignadas().compareTo(BigDecimal.ZERO) <= 0)
            return new ResponseEntity<>(new Mensaje("Las horas asignadas deben ser mayor a 0"), HttpStatus.BAD_REQUEST);

        Legajo legajo = existente.getLegajo();
        BigDecimal cargaHorariaTotal = BigDecimal.valueOf(legajo.getRevista().getCargaHoraria().getCantidad());

        // 2. Validar el nuevo valor contra el total del legajo en ese mes, EXCLUYENDO
        // la fila vieja
        // (porque la vamos a dar de baja igual)
        List<AsignacionHorasEfector> activasDelMes = asignacionHorasEfectorRepository
                .findActivasEnRango(legajo.getId(), existente.getFechaInicio(), existente.getFechaFinalizacion())
                .stream()
                .filter(a -> !Objects.equals(a.getId(), id))
                .collect(Collectors.toList());

        BigDecimal sumaSinEstaFila = activasDelMes.stream()
                .map(AsignacionHorasEfector::getHorasAsignadas)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal nuevaSuma = sumaSinEstaFila.add(dto.getHorasAsignadas());

        if (nuevaSuma.compareTo(cargaHorariaTotal) > 0) {
            BigDecimal disponible = cargaHorariaTotal.subtract(sumaSinEstaFila);
            return new ResponseEntity<>(
                    new Mensaje("Excede las horas totales del legajo. Disponible ese período: " + disponible + "hs"),
                    HttpStatus.BAD_REQUEST);
        }

        // 3. Historizar: dar de baja la vieja, crear una nueva con el valor actualizado
        existente.setActivo(false);
        asignacionHorasEfectorRepository.save(existente);

        AsignacionHorasEfector nueva = new AsignacionHorasEfector();
        nueva.setLegajo(existente.getLegajo());
        nueva.setEfector(existente.getEfector());
        nueva.setHorasAsignadas(dto.getHorasAsignadas());
        nueva.setFechaInicio(existente.getFechaInicio());
        nueva.setFechaFinalizacion(existente.getFechaFinalizacion());
        nueva.setActivo(true);

        asignacionHorasEfectorRepository.save(nueva);

        return new ResponseEntity<>(nueva, HttpStatus.OK);
    }

    public ResponseEntity<?> delete(Long id) {

        Optional<AsignacionHorasEfector> existenteOpt = asignacionHorasEfectorRepository.findById(id);
        if (existenteOpt.isEmpty())
            return new ResponseEntity<>(new Mensaje("No existe la asignación indicada"), HttpStatus.NOT_FOUND);

        if (!puedeModificar(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No se puede eliminar: ya existen distribuciones horarias cargadas para este período"),
                    HttpStatus.BAD_REQUEST);
        }

        AsignacionHorasEfector existente = existenteOpt.get();
        existente.setActivo(false);
        asignacionHorasEfectorRepository.save(existente);

        return new ResponseEntity<>(new Mensaje("Asignación eliminada"), HttpStatus.OK);
    }

}

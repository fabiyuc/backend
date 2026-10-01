package com.guardias.backend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.FechaLimiteDdjj.FechaLimiteDdjjBajaDto;
import com.guardias.backend.dto.FechaLimiteDdjj.FechaLimiteDdjjDto;
import com.guardias.backend.dto.FechaLimiteDdjj.FechaLimiteDdjjListDto;
import com.guardias.backend.dto.FechaLimiteDdjj.FechaLimiteVigenteDto;
import com.guardias.backend.entity.FechaLimiteDdjj;
import com.guardias.backend.entity.Person;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.EstadoFechaLimiteEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.FechaLimiteDdjjRepository;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.security.enums.RolNombre;
import com.guardias.backend.security.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FechaLimiteDdjjService {

    @Autowired
    FechaLimiteDdjjRepository fechaLimiteDdjjRepository;
    @Autowired
    TipoGuardiaService tipoGuardiaService;
    @Autowired
    UsuarioService usuarioService;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ============================================================
    // CONSULTAS
    // ============================================================

    /** Fechas límite vigentes, ordenadas por año y mes descendente. */
    public List<FechaLimiteDdjjListDto> listVigentes() {
        return fechaLimiteDdjjRepository.findByActivoTrue().stream()
                .sorted(Comparator.comparingInt(FechaLimiteDdjj::getAnio).reversed()
                        .thenComparing(f -> f.getMes().getNumeroMes(), Comparator.reverseOrder())
                        .thenComparing(f -> f.getTipoGuardia().getId()))
                .map(this::toListDto)
                .collect(Collectors.toList());
    }

    /** Histórico completo (todas las versiones), más reciente primero. */
    public List<FechaLimiteDdjjListDto> historialAll() {
        return fechaLimiteDdjjRepository.findAllByOrderByFechaHoraCreacionDesc().stream()
                .map(this::toListDto)
                .collect(Collectors.toList());
    }

    /** Histórico de un mes/año/tipo de guardia, más reciente primero. */
    public List<FechaLimiteDdjjListDto> historial(MesesEnum mes, int anio, Long idTipoGuardia) {
        return fechaLimiteDdjjRepository
                .findByMesAndAnioAndTipoGuardia_IdOrderByFechaHoraCreacionDesc(mes, anio, idTipoGuardia).stream()
                .map(this::toListDto)
                .collect(Collectors.toList());
    }

    public Optional<FechaLimiteDdjj> findVigente(MesesEnum mes, int anio, Long idTipoGuardia) {
        return fechaLimiteDdjjRepository.findFirstByMesAndAnioAndTipoGuardia_IdAndActivoTrue(mes, anio,
                idTipoGuardia);
    }

    public Optional<FechaLimiteDdjj> findById(Long id) {
        return fechaLimiteDdjjRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return fechaLimiteDdjjRepository.existsById(id);
    }

    /**
     * Fecha límite que aplica a un mes/año/tipo de guardia.
     * Si no hay una cargada, se usa la regla por defecto: lunes de la segunda
     * semana del mes siguiente.
     */
    public FechaLimiteVigenteDto obtenerFechaLimite(MesesEnum mes, int anio, Long idTipoGuardia) {
        Optional<FechaLimiteDdjj> vigente = findVigente(mes, anio, idTipoGuardia);

        if (vigente.isPresent()) {
            return new FechaLimiteVigenteDto(vigente.get().getFechaLimite(), true, vigente.get().getId());
        }
        return new FechaLimiteVigenteDto(calcularFechaLimitePorDefecto(mes, anio), false, null);
    }

    /** Igual que obtenerFechaLimite, pero indicando el tipo de guardia por nombre. */
    public FechaLimiteVigenteDto obtenerFechaLimite(MesesEnum mes, int anio, TipoGuardiaEnum tipoGuardia) {
        Optional<TipoGuardia> tipo = tipoGuardiaService.findByNombre(tipoGuardia);
        if (tipo.isEmpty())
            return new FechaLimiteVigenteDto(calcularFechaLimitePorDefecto(mes, anio), false, null);
        return obtenerFechaLimite(mes, anio, tipo.get().getId());
    }

    /** Lunes de la segunda semana del mes siguiente al de las guardias. */
    public static LocalDate calcularFechaLimitePorDefecto(MesesEnum mes, int anio) {
        return LocalDate.of(anio, mes.getNumeroMes(), 1)
                .plusMonths(1)
                .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY));
    }

    // ============================================================
    // ALTA / MODIFICACIÓN / BAJA
    // ============================================================

    public ResponseEntity<?> crear(FechaLimiteDdjjDto dto) {
        ResponseEntity<?> respuesta = validations(dto);
        if (respuesta.getStatusCode() != HttpStatus.OK)
            return respuesta;

        if (findVigente(dto.getMes(), dto.getAnio(), dto.getIdTipoGuardia()).isPresent())
            return new ResponseEntity<>(new Mensaje(
                    "Ya existe una fecha límite vigente para ese mes, año y tipo de guardia. Debe modificarla."),
                    HttpStatus.BAD_REQUEST);

        Usuario usuario = usuarioService.findById(dto.getIdUsuario()).get();
        FechaLimiteDdjj nueva = nuevaVersion(dto.getMes(), dto.getAnio(), dto.getIdTipoGuardia(),
                dto.getFechaLimite(), dto.getMotivo(), usuario);
        fechaLimiteDdjjRepository.save(nueva);

        return new ResponseEntity<>(toListDto(nueva), HttpStatus.OK);
    }

    /**
     * Modifica la fecha límite vigente: la versión actual queda REEMPLAZADA y se
     * crea una nueva VIGENTE. Mes, año y tipo de guardia no se pueden cambiar.
     */
    public ResponseEntity<?> modificar(Long id, FechaLimiteDdjjDto dto) {
        Optional<FechaLimiteDdjj> actualOpt = findById(id);
        if (actualOpt.isEmpty())
            return new ResponseEntity<>(new Mensaje("La fecha límite no existe"), HttpStatus.NOT_FOUND);

        FechaLimiteDdjj actual = actualOpt.get();
        if (!actual.isActivo())
            return new ResponseEntity<>(new Mensaje("Solo se puede modificar la fecha límite vigente"),
                    HttpStatus.BAD_REQUEST);

        // Se validan los datos nuevos sobre el mes/año/tipo del registro vigente
        dto.setMes(actual.getMes());
        dto.setAnio(actual.getAnio());
        dto.setIdTipoGuardia(actual.getTipoGuardia().getId());

        ResponseEntity<?> respuesta = validations(dto);
        if (respuesta.getStatusCode() != HttpStatus.OK)
            return respuesta;

        if (actual.getFechaLimite().equals(dto.getFechaLimite()))
            return new ResponseEntity<>(new Mensaje("La nueva fecha límite es igual a la vigente"),
                    HttpStatus.BAD_REQUEST);

        Usuario usuario = usuarioService.findById(dto.getIdUsuario()).get();
        LocalDateTime ahora = LocalDateTime.now();

        actual.setActivo(false);
        actual.setEstado(EstadoFechaLimiteEnum.REEMPLAZADA);
        actual.setUsuarioBaja(usuario);
        actual.setFechaHoraBaja(ahora);
        actual.setMotivoBaja(dto.getMotivo());
        fechaLimiteDdjjRepository.save(actual);

        FechaLimiteDdjj nueva = nuevaVersion(actual.getMes(), actual.getAnio(), actual.getTipoGuardia().getId(),
                dto.getFechaLimite(), dto.getMotivo(), usuario);
        nueva.setFechaHoraCreacion(ahora);
        fechaLimiteDdjjRepository.save(nueva);

        return new ResponseEntity<>(toListDto(nueva), HttpStatus.OK);
    }

    /** Baja lógica: la versión vigente queda ELIMINADA con el usuario y la fecha de baja. */
    public ResponseEntity<?> eliminar(Long id, FechaLimiteDdjjBajaDto bajaDto) {
        Optional<FechaLimiteDdjj> actualOpt = findById(id);
        if (actualOpt.isEmpty())
            return new ResponseEntity<>(new Mensaje("La fecha límite no existe"), HttpStatus.NOT_FOUND);

        FechaLimiteDdjj actual = actualOpt.get();
        if (!actual.isActivo())
            return new ResponseEntity<>(new Mensaje("La fecha límite ya no está vigente"), HttpStatus.BAD_REQUEST);

        ResponseEntity<?> respuestaUsuario = validarUsuario(bajaDto == null ? null : bajaDto.getIdUsuario());
        if (respuestaUsuario.getStatusCode() != HttpStatus.OK)
            return respuestaUsuario;

        actual.setActivo(false);
        actual.setEstado(EstadoFechaLimiteEnum.ELIMINADA);
        actual.setUsuarioBaja(usuarioService.findById(bajaDto.getIdUsuario()).get());
        actual.setFechaHoraBaja(LocalDateTime.now());
        actual.setMotivoBaja(bajaDto.getMotivo());
        fechaLimiteDdjjRepository.save(actual);

        return new ResponseEntity<>(new Mensaje("Fecha límite eliminada correctamente"), HttpStatus.OK);
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    public ResponseEntity<?> validations(FechaLimiteDdjjDto dto) {
        if (dto.getMes() == null)
            return new ResponseEntity<>(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);
        if (dto.getAnio() == null || dto.getAnio() < 2000)
            return new ResponseEntity<>(new Mensaje("El año es obligatorio"), HttpStatus.BAD_REQUEST);
        if (dto.getIdTipoGuardia() == null)
            return new ResponseEntity<>(new Mensaje("El tipo de guardia es obligatorio"), HttpStatus.BAD_REQUEST);
        if (!tipoGuardiaService.activo(dto.getIdTipoGuardia()))
            return new ResponseEntity<>(new Mensaje("El tipo de guardia no existe o no está activo"),
                    HttpStatus.BAD_REQUEST);
        if (dto.getFechaLimite() == null)
            return new ResponseEntity<>(new Mensaje("La fecha límite es obligatoria"), HttpStatus.BAD_REQUEST);

        // La fecha límite debe caer en el mes siguiente al de las guardias
        YearMonth mesSiguiente = YearMonth.of(dto.getAnio(), dto.getMes().getNumeroMes()).plusMonths(1);
        if (!YearMonth.from(dto.getFechaLimite()).equals(mesSiguiente))
            return new ResponseEntity<>(new Mensaje("La fecha límite debe estar entre el "
                    + mesSiguiente.atDay(1).format(FORMATO_FECHA) + " y el "
                    + mesSiguiente.atEndOfMonth().format(FORMATO_FECHA)), HttpStatus.BAD_REQUEST);

        return validarUsuario(dto.getIdUsuario());
    }

    /** El usuario debe existir y tener rol DPH o SUPERUSER. */
    private ResponseEntity<?> validarUsuario(Long idUsuario) {
        if (idUsuario == null)
            return new ResponseEntity<>(new Mensaje("Indicar el id del usuario"), HttpStatus.BAD_REQUEST);

        Optional<Usuario> usuario = usuarioService.findById(idUsuario);
        if (usuario.isEmpty())
            return new ResponseEntity<>(new Mensaje("El usuario no existe"), HttpStatus.BAD_REQUEST);

        boolean autorizado = usuario.get().getRoles().stream()
                .anyMatch(rol -> rol.getRolNombre() == RolNombre.ROLE_DPH
                        || rol.getRolNombre() == RolNombre.ROLE_SUPERUSER);
        if (!autorizado)
            return new ResponseEntity<>(new Mensaje("El usuario no tiene permisos para gestionar fechas límite"),
                    HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(new Mensaje("ok"), HttpStatus.OK);
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private FechaLimiteDdjj nuevaVersion(MesesEnum mes, int anio, Long idTipoGuardia, LocalDate fechaLimite,
            String motivo, Usuario usuario) {
        FechaLimiteDdjj nueva = new FechaLimiteDdjj();
        nueva.setMes(mes);
        nueva.setAnio(anio);
        nueva.setTipoGuardia(tipoGuardiaService.findById(idTipoGuardia).get());
        nueva.setFechaLimite(fechaLimite);
        nueva.setMotivo(motivo);
        nueva.setEstado(EstadoFechaLimiteEnum.VIGENTE);
        nueva.setActivo(true);
        nueva.setUsuarioCreacion(usuario);
        nueva.setFechaHoraCreacion(LocalDateTime.now());
        return nueva;
    }

    private FechaLimiteDdjjListDto toListDto(FechaLimiteDdjj f) {
        FechaLimiteDdjjListDto dto = new FechaLimiteDdjjListDto();
        dto.setId(f.getId());
        dto.setMes(f.getMes());
        dto.setAnio(f.getAnio());
        dto.setIdTipoGuardia(f.getTipoGuardia().getId());
        dto.setTipoGuardia(f.getTipoGuardia().getNombre().name());
        dto.setFechaLimite(f.getFechaLimite());
        dto.setMotivo(f.getMotivo());
        dto.setEstado(f.getEstado());
        dto.setActivo(f.isActivo());

        if (f.getUsuarioCreacion() != null) {
            dto.setIdUsuarioCreacion(f.getUsuarioCreacion().getId());
            dto.setUsuarioCreacion(nombreUsuario(f.getUsuarioCreacion()));
        }
        dto.setFechaHoraCreacion(f.getFechaHoraCreacion());

        if (f.getUsuarioBaja() != null) {
            dto.setIdUsuarioBaja(f.getUsuarioBaja().getId());
            dto.setUsuarioBaja(nombreUsuario(f.getUsuarioBaja()));
        }
        dto.setFechaHoraBaja(f.getFechaHoraBaja());
        dto.setMotivoBaja(f.getMotivoBaja());
        return dto;
    }

    /** "Apellido, Nombre" de la persona asociada, o el nombre de usuario si no tiene. */
    private String nombreUsuario(Usuario usuario) {
        Person persona = usuario.getPerson();
        if (persona != null && persona.getApellido() != null)
            return persona.getApellido() + ", " + persona.getNombre();
        return usuario.getNombreUsuario();
    }
}

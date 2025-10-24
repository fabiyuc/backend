package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.DdjjDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ddjj.DdjjListDto;
import com.guardias.backend.dto.ddjj.EstadoDdjjDto;
import com.guardias.backend.dto.registroMensual.RegistroMensualListDto;
import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.ObservacionDdjj;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.CondicionDdjjEnum;
import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.EstadoFacturacionEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.CronogramaTentativoRepository;
import com.guardias.backend.repository.DdjjRepository;
import com.guardias.backend.repository.ObservacionDdjjRepository;
import com.guardias.backend.repository.RegistroMensualRepository;
import com.guardias.backend.repository.TipoGuardiaRepository;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.security.repository.UsuarioRepository;

import jakarta.validation.ValidationException;

@Service
@Transactional
public class DdjjService {
    @Autowired
    DdjjRepository ddjjRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    CronogramaTentativoRepository cronogramaTentativoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RegistroMensualRepository registroMensualRepository;
    @Autowired
    TipoGuardiaRepository tipoGuardiaRepository;
    @Autowired
    ObservacionDdjjRepository observacionDdjjRepository;
    @Autowired
    TipoGuardiaService tipoGuardiaService;
    @Autowired
    CronogramaDefinitivoService cronogramaDefinitivoService;
    @Autowired
    RegistroActividadService registroActividadService;

    public boolean existsById(Long id) {
        return ddjjRepository.existsById(id);
    }

    public Optional<Ddjj> findById(Long id) {
        return ddjjRepository.findById(id);
    }

    public List<Ddjj> findAll() {
        return ddjjRepository.findAll();
    }

    public List<Ddjj> findByActivoTrue() {
        return ddjjRepository.findByActivoTrue();
    }

    public boolean activo(Long id) {
        return ddjjRepository.existsById(id) && ddjjRepository.findById(id).get().isActivo();
    }

    public boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return ddjjRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsByAnio(int anio) {
        return ddjjRepository.existsByAnio(anio);
    }

    public List<Ddjj> findByByAnioAndMes(int anio, MesesEnum mes) {
        return ddjjRepository.findByAnioAndMes(anio, mes);
    }

    public List<Ddjj> findByEfectorIdAndMesAndAnio(Long efectorId, MesesEnum mes, int anio) {
        return ddjjRepository.findByEfectorIdAndMesAndAnio(efectorId, mes, anio);
    }

    public List<Ddjj> findByByAnio(int anio) {
        return ddjjRepository.findByAnio(anio);
    }

    public void save(Ddjj ddjj) {
        ddjjRepository.save(ddjj);
    }

    public void deleteById(Long id) {
        ddjjRepository.deleteById(id);
    }

    public ResponseEntity<?> validations(DdjjDto ddjjDto) {

        if (ddjjDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getSubtotal().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto del subtotal incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getTotal().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto del total incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getIdEfector() < 1)
            return new ResponseEntity(new Mensaje("El id del efector es incorrecto"), HttpStatus.BAD_REQUEST);

        /* ver si es valida esta comprobacion */
        if (ddjjDto.getIdRegistrosMensuales() == null)
            return new ResponseEntity(new Mensaje("La lista de registros mensuales no puede ser vacia"),
                    HttpStatus.BAD_REQUEST);

        if (ddjjDto.getEnPosesionDirector() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar la posesion en director"),
                    HttpStatus.BAD_REQUEST);

        if (!efectorService.activoById(ddjjDto.getIdEfector())) {
            throw new IllegalArgumentException("El efector no existe");
        }

        if (ddjjDto.getIdTipoGuardia() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar el tipo de guardia"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Ddjj createUpdate(Ddjj ddjj, DdjjDto ddjjDto) {

        // ===== 0. VALIDACIÓN INMEDIATA DE LOS IDs =====
        validateRegistrosMensuales(ddjjDto.getIdRegistrosMensuales());

        // ===== 1. DETERMINAR QUINCENA (NUEVO) =====
        QuincenaEnum quincena = determinarQuincenaParaDdjj(ddjjDto);
        ddjj.setQuincena(quincena);

        // ===== 2. MAPEO DE CAMPOS BÁSICOS =====
        mapBasicFields(ddjj, ddjjDto);

        // ===== 3. MANEJO DE DIRECTORES =====
        processDirectores(ddjj, ddjjDto);

        // ===== 4. CARGA EFICIENTE DE REGISTROS MENSUALES =====
        processRegistrosMensuales(ddjj, ddjjDto);

        // ===== 5. GUARDADO FINAL =====
        return ddjjRepository.save(ddjj);

    }

    // solo para CONTRAFACTURA sino retorna NULL
    private QuincenaEnum determinarQuincenaParaDdjj(DdjjDto ddjjDto) {

        Optional<TipoGuardia> tipoGuardia = tipoGuardiaRepository.findById(ddjjDto.getIdTipoGuardia());
        List<RegistroMensual> registros = registroMensualRepository.findAllById(ddjjDto.getIdRegistrosMensuales());

        if (!registros.isEmpty() && tipoGuardia.isPresent()
                && tipoGuardia.get().getNombre() == TipoGuardiaEnum.CONTRAFACTURA) {
            return obtenerQuincenaDeRegistrosMensuales(registros);
        }
        // Para otros tipos de guardia, retorna null
        return null;
    }

    private QuincenaEnum obtenerQuincenaDeRegistrosMensuales(List<RegistroMensual> registrosMensuales) {

        // Verificar si todos los registros tienen la misma quincena
        boolean quincenaConsistente = true;
        QuincenaEnum quincenaComun = registrosMensuales.get(0).getQuincena();
        for (RegistroMensual registro : registrosMensuales) {
            if (!Objects.equals(quincenaComun, registro.getQuincena())) {
                quincenaConsistente = false;
                break;
            }
        }

        // Si tienen misma quincena, evaluar condiciones temporales
        if (quincenaConsistente) {
            // Obtener mes y anio de registro mensual
            MesesEnum mesComun = registrosMensuales.get(0).getMes();
            int anioComun = registrosMensuales.get(0).getAnio();
            // Obtener fecha actual del sistema
            LocalDate fechaActual = LocalDate.now();
            int mesSistema = fechaActual.getMonthValue();
            int diaSistema = fechaActual.getDayOfMonth();
            int anioSistema = fechaActual.getYear();

            // Convertir mesComun (MesesEnum) a número de mes (1-12)
            int mesRegistro = mesComun.ordinal() + 1; // Asumiendo MesesEnum es ENERO(0), FEBRERO(1), ..., DICIEMBRE(11)

            // Determinar si el mes del registro es igual o anterior al del sistema
            boolean mismoAnioMes = (anioComun == anioSistema && mesRegistro == mesSistema);
            boolean mesAnterior = (anioComun < anioSistema) || (anioComun == anioSistema && mesRegistro < mesSistema);

            // Determino quincena para registros con misma quincena
            if (quincenaComun == QuincenaEnum.PRIMERA && mismoAnioMes && diaSistema <= 20) {
                return QuincenaEnum.PRIMERA;
            } else if (quincenaComun == QuincenaEnum.SEGUNDA && mesAnterior && diaSistema <= 10) {
                return QuincenaEnum.SEGUNDA;
            }
        }
        // devuelve fuera de termino para registros con diferente quincena
        return QuincenaEnum.FUERA_DE_TERMINO;
    }

    private void validateRegistrosMensuales(List<Long> idsRegistros) {
        if (idsRegistros == null || idsRegistros.isEmpty()) {
            throw new IllegalArgumentException("La lista de registros mensuales no puede estar vacía");
        }

        // erifica existencia en una sola query
        List<Long> idsExistentes = registroMensualRepository.findExistingIds(idsRegistros);

        if (idsExistentes.size() != idsRegistros.size()) {
            List<Long> idsFaltantes = new ArrayList<>(idsRegistros);
            idsFaltantes.removeAll(idsExistentes);
            throw new IllegalArgumentException("Los siguientes IDs de registros no existen: " + idsFaltantes);
        }
    }

    private void mapBasicFields(Ddjj ddjj, DdjjDto ddjjDto) {

        if (ddjjDto.getMes() != null && !ddjjDto.getMes().equals(ddjj.getMes()))
            ddjj.setMes(ddjjDto.getMes());

        if (ddjjDto.getAnio() != ddjj.getAnio())
            ddjj.setAnio(ddjjDto.getAnio());

        if (ddjjDto.getSubtotal() != ddjj.getSubtotal())
            ddjj.setSubtotal(ddjjDto.getSubtotal());

        if (ddjjDto.getTotal() != ddjj.getTotal())
            ddjj.setTotal(ddjjDto.getTotal());

        if (ddjjDto.getIdEfector() != null
                && (ddjj.getEfector() == null || !Objects.equals(ddjj.getEfector().getId(), ddjjDto.getIdEfector()))) {
            ddjj.setEfector(efectorService.findById(ddjjDto.getIdEfector()));
        }

        if (ddjjDto.getIdObservacionesDdjj() != null) {
            // 1. Primero guarda la DDJJ si es nueva (sin los registros)
            if (ddjj.getId() == null) {
                ddjj = ddjjRepository.save(ddjj);
            }

            // 2. Manejo de registros existentes (para actualización)
            if (ddjj.getObservacionesDdjj() != null) {
                // Rompe la relación con registros que ya no están en la lista nueva
                List<ObservacionDdjj> toRemove = new ArrayList<>();
                for (ObservacionDdjj od : ddjj.getObservacionesDdjj()) {
                    if (!ddjjDto.getIdObservacionesDdjj().contains(od.getId())) {
                        od.setDdjj(null);
                        toRemove.add(od);
                    }
                }
                ddjj.getObservacionesDdjj().removeAll(toRemove);
            } else {
                ddjj.setObservacionesDdjj(new ArrayList<>());
            }

            // 3. Agrega las nuevas observaciones
            for (Long id : ddjjDto.getIdObservacionesDdjj()) {
                boolean exists = ddjj.getObservacionesDdjj().stream()
                        .anyMatch(od -> od.getId().equals(id));

                if (!exists) {
                    Optional<ObservacionDdjj> odOpt = observacionDdjjRepository.findById(id);
                    if (odOpt.isPresent()) {
                        ObservacionDdjj od = odOpt.get();
                        od.setDdjj(ddjj); // Establece la relación inversa
                        ddjj.getObservacionesDdjj().add(od);
                    }
                }
            }
        }

        if (ddjjDto.getEstadoDdjjDirector() != null
                && !ddjjDto.getEstadoDdjjDirector().equals(ddjj.getEstadoDdjjDirector()))
            ddjj.setEstadoDdjjDirector(ddjjDto.getEstadoDdjjDirector());

        if (ddjjDto.getEstadoDdjjDirectorDPH() != null
                && !ddjjDto.getEstadoDdjjDirectorDPH().equals(ddjj.getEstadoDdjjDirectorDPH()))
            ddjj.setEstadoDdjjDirectorDPH(ddjjDto.getEstadoDdjjDirectorDPH());

        ddjj.setEnPosesionDirector(ddjjDto.getEnPosesionDirector());
        ddjj.setEnPosesionDirectorDPH(ddjjDto.getEnPosesionDirectorDPH());

        ddjj.setMotivoDirector(ddjjDto.getMotivoDirector());
        ddjj.setMotivoDirectorDPH(ddjjDto.getMotivoDirectorDPH());

        if (ddjj.getTipoGuardia() == null ||
                (ddjjDto.getIdTipoGuardia() != null &&
                        !Objects.equals(ddjj.getTipoGuardia().getId(),
                                ddjjDto.getIdTipoGuardia()))) {
            ddjj
                    .setTipoGuardia(tipoGuardiaService.findById(ddjjDto.getIdTipoGuardia()).get());
        }

        if (ddjjDto.getIdCronogramasDefinitivos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (ddjj.getCronogramasDefinitivos() != null) {
                for (CronogramaDefinitivo cronograma : ddjj.getCronogramasDefinitivos()) {
                    for (Long id : ddjjDto.getIdCronogramasDefinitivos()) {
                        if (!cronograma.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                ddjj.setCronogramasDefinitivos(new ArrayList<CronogramaDefinitivo>());
            }

            List<Long> idsToAdd = idList.isEmpty() ? ddjjDto.getIdCronogramasDefinitivos() : idList;

            for (Long id : idsToAdd) {
                ddjj.getCronogramasDefinitivos().add(cronogramaDefinitivoService.findById(id).get());
                cronogramaDefinitivoService.findById(id).get().getDdjjs().add(ddjj);
            }
        }

        ddjj.setActivo(true);
    }

    private void processRegistrosMensuales(Ddjj ddjj, DdjjDto ddjjDto) {
        
        // 1. Carga de los registros mensuales
        List<RegistroMensual> todosRegistros = registroMensualRepository.findAllById(ddjjDto.getIdRegistrosMensuales());

        // 2. VERIFICAR TIPO DE GUARDIA
        Optional<TipoGuardia> tipoGuardia = tipoGuardiaRepository.findById(ddjjDto.getIdTipoGuardia());
        boolean esContrafactura = tipoGuardia.isPresent() && tipoGuardia.get().getNombre() == TipoGuardiaEnum.CONTRAFACTURA;

        List<RegistroMensual> registrosFiltrados;
        CondicionDdjjEnum condicion = null;

        // ===== LÓGICA SOLO PARA CONTRAFACTURA =====
        if (esContrafactura) {
        
            condicion = determinarTipoDdjjDesdeRegistros(todosRegistros);
            ddjj.setCondicionDdjj(condicion);
            registrosFiltrados = filtrarRegistrosPorTipoDdjj(todosRegistros, condicion);
        } else {
            // ===== PARA OTROS TIPOS DE GUARDIA =====
            // NO determinar condición, NO filtrar registros
            ddjj.setCondicionDdjj(null);
            registrosFiltrados = todosRegistros; // Usar TODOS los registros sin filtrar
        }

        // 4. Validar que haya al menos un registro válido
        if (registrosFiltrados.isEmpty()) {
            throw new IllegalArgumentException("No hay registros mensuales válidos para crear la DDJJ. ");
        }

        System.out.println("=== DEBUG CREACIÓN DDJJ ===");
        System.out.println("Tipo Guardia: " + (esContrafactura ? "CONTRAFACTURA" : "OTRO"));
        System.out.println("Condición DDJJ: " + condicion);
        System.out.println("Total registros solicitados: " + todosRegistros.size());
        System.out.println("Registros usados: " + registrosFiltrados.size());
        System.out.println("Estados encontrados en registros:");
        todosRegistros
                .forEach(rm -> System.out.println(" - Registro " + rm.getId() + ": " + rm.getEstadoFacturacion()));
        System.out.println("===========================");

        // 5. si ddjj nueva - Caso CREACIÓN - Inicializa y establece relaciones
        if (ddjj.getId() == null) {
            ddjj.setRegistrosMensuales(new ArrayList<>());

            // Establece relaciones bidireccionales
            for (RegistroMensual rm : registrosFiltrados) {
                // Agrega la DDJJ al registro mensual
                if (!rm.getDdjjs().contains(ddjj)) {
                    rm.getDdjjs().add(ddjj);
                }
                // Agrega el registro mensual a la DDJJ
                if (!ddjj.getRegistrosMensuales().contains(rm)) {
                    ddjj.getRegistrosMensuales().add(rm);
                }
            }

            // GUARDA LA DDJJ PRIMERO para tener ID
            ddjj = ddjjRepository.save(ddjj);

            // GUARDA LOS REGISTROS MODIFICADOS
            registroMensualRepository.saveAll(registrosFiltrados);
            return;
        }

        // 6. si Caso EDICIÓN
        Set<Long> nuevosIds = registrosFiltrados.stream()
                .map(RegistroMensual::getId)
                .collect(Collectors.toSet());

        // a) Elimina relaciones con registros que ya no están
        Iterator<RegistroMensual> iterator = ddjj.getRegistrosMensuales().iterator();
        List<RegistroMensual> registrosToUpdate = new ArrayList<>();

        while (iterator.hasNext()) {
            RegistroMensual rmExistente = iterator.next();
            if (!nuevosIds.contains(rmExistente.getId())) {
                rmExistente.getDdjjs().remove(ddjj);
                registrosToUpdate.add(rmExistente);
                iterator.remove();
            }
        }

        // b) Agrega nuevas relaciones
        for (RegistroMensual rm : registrosFiltrados) {
            boolean yaExiste = ddjj.getRegistrosMensuales().stream()
                    .anyMatch(existente -> existente.getId().equals(rm.getId()));

            if (!yaExiste) {
                if (!rm.getDdjjs().contains(ddjj)) {
                    rm.getDdjjs().add(ddjj);
                    registrosToUpdate.add(rm);
                }
                ddjj.getRegistrosMensuales().add(rm);
            }
        }

        // GUARDA LOS CAMBIOS EN LOS REGISTROS
        if (!registrosToUpdate.isEmpty()) {
            registroMensualRepository.saveAll(registrosToUpdate);
        }
    }

    /**
     * Determina la condicion de la DDJJ
     */
    private CondicionDdjjEnum determinarTipoDdjjDesdeRegistros(List<RegistroMensual> registros) {
        // Contar registros por estado
        long completados = registros.stream()
                .filter(rm -> rm.getEstadoFacturacion() == EstadoFacturacionEnum.COMPLETADO)
                .count();

        long regularizados = registros.stream()
                .filter(rm -> rm.getEstadoFacturacion() == EstadoFacturacionEnum.REGULARIZADO)
                .count();

        long pendientes = registros.stream()
                .filter(rm -> rm.getEstadoFacturacion() == EstadoFacturacionEnum.PENDIENTE ||
                        rm.getEstadoFacturacion() == null)
                .count();

        System.out.println("=== DEBUG DETERMINACIÓN TIPO DDJJ ===");
        System.out.println("COMPLETADO: " + completados);
        System.out.println("REGULARIZADO: " + regularizados);
        System.out.println("PENDIENTE: " + pendientes);
        System.out.println("Total registros: " + registros.size());


        // Determinar condicion de DDJJ
        if (completados == registros.size()) {
            // TODOS COMPLETADOS → OFICIAL
            System.out.println("Condicion determinada: OFICIAL (todos los RM con estado facturacion COMPLETADO)");
            return CondicionDdjjEnum.OFICIAL;
        } else if (regularizados == registros.size()) {
            // TODOS REGULARIZADOS → FUERA_DE_TERMINO
            System.out.println("Condicion determinada: FUERA_DE_TERMINO (todos los RM con estado facturacion REGULARIZADO)");
            return CondicionDdjjEnum.FUERA_DE_TERMINO;
        } else 
            // Al menos 1 RM debe tener estado PENDIENTE y  al menos 1 debe tener estado COMPLETADO
            if (completados > 0 && pendientes > 0) {
                System.out.println("Condicion determinada: PARCIAL (mezcla COMPLETADO + PENDIENTE)");
                return CondicionDdjjEnum.PARCIAL;
            } else {
                throw new IllegalArgumentException("No hay RM con estado facturacion COMPLETADO para poder crear la ddjj");
            }
        }

    /**
     * Filtra registros según el condicion de DDJJ
     */
    private List<RegistroMensual> filtrarRegistrosPorTipoDdjj(List<RegistroMensual> registros,
            CondicionDdjjEnum tipoDdjj) {
        switch (tipoDdjj) {
            case OFICIAL:
                // Solo registros COMPLETADO (excluye PENDIENTE y REGULARIZADO)
                return registros.stream()
                        .filter(rm -> rm.getEstadoFacturacion() == EstadoFacturacionEnum.COMPLETADO)
                        .collect(Collectors.toList());

            case FUERA_DE_TERMINO:
                // Solo registros REGULARIZADO (excluye PENDIENTE y COMPLETADO)
                return registros.stream()
                        .filter(rm -> rm.getEstadoFacturacion() == EstadoFacturacionEnum.REGULARIZADO)
                        .collect(Collectors.toList());

            case PARCIAL:
            // Para PARCIAL: solo registros COMPLETADO
            return registros.stream()
                    .filter(rm -> rm.getEstadoFacturacion() == EstadoFacturacionEnum.COMPLETADO || 
                                 rm.getEstadoFacturacion() == null)
                    .collect(Collectors.toList());


            default:
                throw new IllegalArgumentException("Tipo de DDJJ no soportado: " + tipoDdjj);
        }
    }

    private void processDirectores(Ddjj ddjj, DdjjDto ddjjDto) {
        if (ddjjDto.getIdDirector() != null) {
            Usuario director = usuarioRepository.findById(ddjjDto.getIdDirector())
                    .orElseThrow(() -> new IllegalArgumentException("Director no encontrado"));
            ddjj.setDirector(director);
        }

        if (ddjjDto.getIdDirectorDPH() != null) {
            Usuario directorDPH = usuarioRepository.findById(ddjjDto.getIdDirectorDPH())
                    .orElseThrow(() -> new IllegalArgumentException("Director DPH no encontrado"));
            ddjj.setDirectorDPH(directorDPH);
        }
    }

    public boolean cambiarEstado(EstadoDdjjDto estadoDdjjDto) {
        // Busca la DDJJ por ID y que esté activa
        Optional<Ddjj> ddjjOptional = ddjjRepository.findByIdAndActivoTrue(estadoDdjjDto.getIdDdjj());

        if (!ddjjOptional.isPresent()) {
            throw new IllegalArgumentException("No se encontró la DDJJ con ID: " + estadoDdjjDto.getIdDdjj());
        }

        Ddjj ddjj = ddjjOptional.get();

        // Actualiza los campos según el DTO recibido
        if (estadoDdjjDto.getIdDirector() != null) {
            Optional<Usuario> directorOptional = usuarioRepository.findById(estadoDdjjDto.getIdDirector());
            if (directorOptional.isPresent()) {
                ddjj.setDirector(directorOptional.get());
            } else {
                throw new ValidationException(
                        "No se encontró el usuario director con ID: " + estadoDdjjDto.getIdDirector());
            }
        }

        if (estadoDdjjDto.getIdDirectorDPH() != null) {
            Optional<Usuario> directorDPHOptional = usuarioRepository.findById(estadoDdjjDto.getIdDirectorDPH());
            if (directorDPHOptional.isPresent()) {
                ddjj.setDirectorDPH(directorDPHOptional.get());
            } else {
                throw new ValidationException(
                        "No se encontró el usuario director DPH con ID: " + estadoDdjjDto.getIdDirectorDPH());
            }
        }

        if (estadoDdjjDto.getEstadoDdjjDirector() != null) {
            ddjj.setEstadoDdjjDirector(estadoDdjjDto.getEstadoDdjjDirector());
        }

        if (estadoDdjjDto.getEstadoDdjjDirectorDPH() != null) {
            ddjj.setEstadoDdjjDirectorDPH(estadoDdjjDto.getEstadoDdjjDirectorDPH());
        }

        if (estadoDdjjDto.getEnPosesionDirector() != null) {
            ddjj.setEnPosesionDirector(estadoDdjjDto.getEnPosesionDirector());
        }

        if (estadoDdjjDto.getEnPosesionDirectorDPH() != null) {
            ddjj.setEnPosesionDirectorDPH(estadoDdjjDto.getEnPosesionDirectorDPH());
        }

        if (estadoDdjjDto.getMotivoDirector() != null) {
            ddjj.setMotivoDirector(estadoDdjjDto.getMotivoDirector());
        }

        if (estadoDdjjDto.getMotivoDirectorDPH() != null) {
            ddjj.setMotivoDirectorDPH(estadoDdjjDto.getMotivoDirectorDPH());
        }

        try {
            // Guardar los cambios
            ddjjRepository.save(ddjj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Ddjj> findByEfectorAndEstadoPendienteDph(Long idEfector) {
        return ddjjRepository.findByEfectorIdAndEstadoDdjjDirectorDPHAndActivoTrue(idEfector, EstadoDdjjEnum.PENDIENTE);
    }

    public List<Ddjj> findByEfectorAndEstadoPendiente(Long idEfector) {
        return ddjjRepository.findByEfectorIdAndEstadoDdjjDirectorAndActivoTrue(idEfector, EstadoDdjjEnum.PENDIENTE);
    }

    public List<Ddjj> findByEfectorAndEstadoAprobado(Long idDirector, Long idEfector) {
        return ddjjRepository.findByEfectorIdAndDirectorIdAndEstadoDdjjDirectorAndActivoTrue(
                idEfector,
                idDirector,
                EstadoDdjjEnum.APROBADO);
    }

    public List<Ddjj> findByEfectorAndEstadoAprobadoDph(Long idEfector) {
        return ddjjRepository.findByEfectorIdAndEstadoDdjjDirectorDPHAndActivoTrue(idEfector, EstadoDdjjEnum.APROBADO);
    }

    public boolean existsByAnioMesEfectorAndTipoGuardia(int anio, MesesEnum mes, Long idEfector, Long idTipoGuardia) {

        System.out.println("=== INICIO Servicio (con nuevo atributo) ===");

        Optional<TipoGuardia> tipoGuardiaOptional = tipoGuardiaRepository.findById(idTipoGuardia);

        if (tipoGuardiaOptional.isPresent()) {
            TipoGuardiaEnum tipo = tipoGuardiaOptional.get().getNombre();
            System.out.println("Tipo de guardia: " + tipo);

            if (tipo == TipoGuardiaEnum.CARGO || tipo == TipoGuardiaEnum.AGRUPACION) {
                // Nueva consulta usando el atributo directo
                System.out.println("Buscando cualquier DDJJ activa con tipo CARGO o AGRUPACION");
                boolean result = ddjjRepository.existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndActivoTrue(
                        anio, mes, idEfector, 1L);
                System.out.println("Resultado: " + result);
                return result;
            } else {
                // Consulta específica por tipo de guardia
                System.out.println("Buscando DDJJ con tipo específico: " + tipo);
                boolean result = ddjjRepository.existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndActivoTrue(
                        anio, mes, idEfector, idTipoGuardia);
                System.out.println("Resultado: " + result);
                return result;
            }
        }

        System.out.println("TipoGuardia no encontrado");
        return false;
    }

    public boolean existsByAnioMesEfectorAndQuincenaCf(
            int anio, MesesEnum mes, Long idEfector, QuincenaEnum quincena) {

        System.out.println("=== INICIO existsByAnioMesEfectorAndQuincenaCf ===");
        System.out.println("Parámetros:");
        System.out.println(" - anio: " + anio);
        System.out.println(" - mes: " + mes);
        System.out.println(" - idEfector: " + idEfector);
        System.out.println(" - quincena: " + quincena);

        // Buscar el ID de CONTRAFACTURA
        Optional<TipoGuardia> tipoGuardiaCf = tipoGuardiaRepository.findByNombre(TipoGuardiaEnum.CONTRAFACTURA);

        if (!tipoGuardiaCf.isPresent()) {
            System.out.println("ERROR: TipoGuardia CONTRAFACTURA no encontrado");
            return false;
        }

        Long idTipoGuardiaCf = tipoGuardiaCf.get().getId();
        System.out.println("ID de CONTRAFACTURA: " + idTipoGuardiaCf);

        // Consulta específica para CONTRAFACTURA con quincena
        boolean result = ddjjRepository.existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndQuincenaAndActivoTrue(
                anio, mes, idEfector, idTipoGuardiaCf, quincena);

        System.out.println("Resultado de la consulta: " + result);
        System.out.println("=== FIN existsByAnioMesEfectorAndQuincenaCf ===");

        return result;
    }

    public boolean existsByAnioMesAndEfectorCf(
            int anio, MesesEnum mes, Long idEfector) {

        System.out.println("=== INICIO existsByAnioMesAndEfectorCf ===");
        System.out.println("Parámetros:");
        System.out.println(" - anio: " + anio);
        System.out.println(" - mes: " + mes);
        System.out.println(" - idEfector: " + idEfector);

        // Buscar el ID de CONTRAFACTURA
        Optional<TipoGuardia> tipoGuardiaCf = tipoGuardiaRepository.findByNombre(TipoGuardiaEnum.CONTRAFACTURA);

        if (!tipoGuardiaCf.isPresent()) {
            System.out.println("ERROR: TipoGuardia CONTRAFACTURA no encontrado");
            return false;
        }

        Long idTipoGuardiaCf = tipoGuardiaCf.get().getId();
        System.out.println("ID de CONTRAFACTURA: " + idTipoGuardiaCf);

        // Consulta específica para CONTRAFACTURA
        boolean result = ddjjRepository.existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndActivoTrue(
                anio, mes, idEfector, idTipoGuardiaCf);

        System.out.println("Resultado de la consulta: " + result);
        System.out.println("=== FIN existsByAnioMesAndEfectorCf ===");

        return result;
    }

    public boolean existsByAnioMesAndEfectorCfFueraTermino(
            int anio, MesesEnum mes, Long idEfector) {

        System.out.println("=== INICIO existsByAnioMesAndEfectorCf ===");
        System.out.println("Parámetros:");
        System.out.println(" - anio: " + anio);
        System.out.println(" - mes: " + mes);
        System.out.println(" - idEfector: " + idEfector);

        // Buscar el ID de CONTRAFACTURA
        Optional<TipoGuardia> tipoGuardiaCf = tipoGuardiaRepository.findByNombre(TipoGuardiaEnum.CONTRAFACTURA);

        if (!tipoGuardiaCf.isPresent()) {
            System.out.println("ERROR: TipoGuardia CONTRAFACTURA no encontrado");
            return false;
        }

        Long idTipoGuardiaCf = tipoGuardiaCf.get().getId();
        System.out.println("ID de CONTRAFACTURA: " + idTipoGuardiaCf);

        // Consulta específica para CONTRAFACTURA
        boolean result = ddjjRepository.existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndCondicionDdjjAndActivoTrue(
                anio, mes, idEfector, idTipoGuardiaCf, CondicionDdjjEnum.FUERA_DE_TERMINO);

        System.out.println("Resultado de la consulta: " + result);
        System.out.println("=== FIN existsByAnioMesAndEfectorCf ===");

        return result;
    }

    public boolean existsCompleteSetOfDdjj(MesesEnum mes, int anio, Long idEfector) {

        EstadoDdjjEnum estadoRequerido = EstadoDdjjEnum.APROBADO;

        // Verifico si existen las 3 ddjj requeridas
        boolean hasCargo = ddjjRepository.countActiveByMesAnioEfectorAndTipoGuardia(
                mes, anio, idEfector, TipoGuardiaEnum.CARGO, estadoRequerido) > 0;

        boolean hasExtra = ddjjRepository.countActiveByMesAnioEfectorAndTipoGuardia(
                mes, anio, idEfector, TipoGuardiaEnum.EXTRA, estadoRequerido) > 0;

        boolean hasContrafactura = ddjjRepository.countActiveByMesAnioEfectorAndTipoGuardia(
                mes, anio, idEfector, TipoGuardiaEnum.CONTRAFACTURA, estadoRequerido) > 0;

        return hasCargo && hasExtra && hasContrafactura;
    }

    public boolean puedeGenerarCronogramaDefinitivo(Long idEfector, MesesEnum mes, int anio) {

        // 2. Obtener todas las DDJJ del efector para ese período
        List<Ddjj> ddjjs = ddjjRepository.findByEfectorAndMesAndAnio(idEfector, mes, anio);

        if (ddjjs.isEmpty()) {
            return false; // No hay DDJJ creadas (pero podría no ser obligatorio)
        }

        // 3. Verificar aprobación de todas las DDJJ existentes
        for (Ddjj ddjj : ddjjs) {
            if (ddjj.getEstadoDdjjDirector() != EstadoDdjjEnum.APROBADO) {
                return false; // Hay al menos una DDJJ no aprobada
            }
        }

        return true; // Cumple todas las condiciones
    }

    public int actualizarDdjjAPendiente(Long idEfector, int mes, int anio) {
        // 1. Primero obtenemos los IDs de las DDJJ que cumplen los requisitos
        List<Long> idsDdjjValidas = registroActividadService.obtenerIdsDdjjAprobadas(idEfector, mes, anio);

        // 2. Si no hay DDJJ válidas, retornamos 0
        if (idsDdjjValidas.isEmpty()) {
            return 0;
        }

        // 3. Actualizamos el estado de las DDJJ encontradas
        return ddjjRepository.updateEstadoDdjjDirectorDPHByIds(
                idsDdjjValidas,
                EstadoDdjjEnum.PENDIENTE);
    }

    public void actualizarEstadoAPendiente(List<Long> idsDdjj) {
        if (idsDdjj == null || idsDdjj.isEmpty()) {
            throw new IllegalArgumentException("La lista de IDs de DDJJ no puede estar vacía");
        }

        // Verificar que todas las DDJJ existen
        List<Ddjj> ddjjs = ddjjRepository.findAllById(idsDdjj);
        if (ddjjs.size() != idsDdjj.size()) {
            throw new IllegalArgumentException("Algunas DDJJ no existen");
        }

        // Actualizar estado
        ddjjs.forEach(ddjj -> {
            ddjj.setEstadoDdjjDirectorDPH(EstadoDdjjEnum.PENDIENTE);
            // Opcional: Registrar quién hizo el cambio
            // ddjj.setUltimaActualizacion(LocalDateTime.now());
        });

        ddjjRepository.saveAll(ddjjs);
    }

    public List<DdjjListDto> findCargoyAgrupServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio) {

        List<TipoGuardiaEnum> tipos = Arrays.asList(TipoGuardiaEnum.CARGO, TipoGuardiaEnum.AGRUPACION);
        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicioAndTipoGuardia(anio, mes, idEfector,
                idServicio, tipos);
        return ddjjs.stream()
                .filter(Ddjj::isActivo) // Filtrar ddjj activas
                .map(ddjj -> {
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo) // Filtrar registrosMensuales activos
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo()
                                                && (actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CARGO
                                                        || actividad.getTipoGuardia()
                                                                .getNombre() == TipoGuardiaEnum.AGRUPACION)
                                                && actividad.getServicio().getId().equals(idServicio))
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty()) // Excluir los que se quedaron sin
                                                                                // actividades
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty()) // Excluir los que se quedaron sin registros
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findCargoyAgrup(int anio, MesesEnum mes, Long idEfector) {

        List<TipoGuardiaEnum> tipos = Arrays.asList(TipoGuardiaEnum.CARGO, TipoGuardiaEnum.AGRUPACION);
        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfectorAndTipoGuardia(anio, mes, idEfector, tipos);
        return ddjjs.stream()
                .filter(Ddjj::isActivo) // Filtrar ddjj activas
                .map(ddjj -> {
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo) // Filtrar registrosMensuales activos
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo()
                                                && (actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CARGO
                                                        || actividad.getTipoGuardia()
                                                                .getNombre() == TipoGuardiaEnum.AGRUPACION))
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty()) // Excluir los que se quedaron sin
                                                                                // actividades
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty()) // Excluir los que se quedaron sin registros
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findExtraServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio) {

        List<TipoGuardiaEnum> tipos = Arrays.asList(TipoGuardiaEnum.EXTRA);
        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicioAndTipoGuardia(anio, mes, idEfector,
                idServicio, tipos);
        return ddjjs.stream()
                .filter(Ddjj::isActivo) // Filtrar ddjj activas
                .map(ddjj -> {
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo) // Filtrar registrosMensuales activos
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo()
                                                && (actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.EXTRA)
                                                && actividad.getServicio().getId().equals(idServicio))
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty()) // Excluir los que se quedaron sin
                                                                                // actividades
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty()) // Excluir los que se quedaron sin registros
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findExtra(int anio, MesesEnum mes, Long idEfector) {

        List<TipoGuardiaEnum> tipos = Arrays.asList(TipoGuardiaEnum.EXTRA);
        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfectorAndTipoGuardia(anio, mes, idEfector, tipos);
        return ddjjs.stream()
                .filter(Ddjj::isActivo) // Filtrar ddjj activas
                .map(ddjj -> {
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo) // Filtrar registrosMensuales activos
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo()
                                                && (actividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.EXTRA))
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty()) // Excluir los que se quedaron sin
                                                                                // actividades
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty()) // Excluir los que se quedaron sin registros
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findCfServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio,
            QuincenaEnum quincena) {

        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicioAndTipoGuardiaAndQuincena(anio, mes,
                idEfector, idServicio, TipoGuardiaEnum.CONTRAFACTURA, quincena);

        if (ddjjs.isEmpty()) {
            return Collections.emptyList();
        }

        return ddjjs.stream()
                .map(ddjj -> {

                    // Filtrar solo registros mensuales activos
                    List<RegistroMensual> registrosActivos = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(rm -> {

                                // Filtrar solo regActiv activas
                                List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad().stream()
                                        .filter(actividad -> actividad.isActivo())
                                        .collect(Collectors.toList());

                                rm.setRegistroActividad(actividadesFiltradas);
                                return rm;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty()) // Solo registros con actividades
                            .collect(Collectors.toList());

                    ddjj.setRegistrosMensuales(registrosActivos);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty()) // Solo DTOs con registros
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findCf(int anio, MesesEnum mes, Long idEfector, QuincenaEnum quincena) {

        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfectorAndTipoGuardiaAndQuincena(
                anio, mes, idEfector, TipoGuardiaEnum.CONTRAFACTURA, quincena);

        if (ddjjs.isEmpty()) {
            return Collections.emptyList();
        }

        List<DdjjListDto> result = ddjjs.stream()
                .map(ddjj -> {

                    // Filtrar solo registros mensuales activos
                    List<RegistroMensual> registrosActivos = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(rm -> {

                                // Filtrar solo regActiv activas
                                List<RegistroActividad> actividadesActivas = rm.getRegistroActividad().stream()
                                        .filter(actividad -> {
                                            boolean activo = actividad.isActivo();
                                            boolean guardiaCompleta = actividad.getEsGuardiaIncompleta() == null ||
                                                    !actividad.getEsGuardiaIncompleta();

                                            return activo && guardiaCompleta;
                                        })
                                        .collect(Collectors.toList());

                                rm.setRegistroActividad(actividadesActivas);
                                return rm;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty()) // Solo registros con actividades
                            .collect(Collectors.toList());

                    ddjj.setRegistrosMensuales(registrosActivos);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(dto -> !dto.getRegistrosMensuales().isEmpty()) // Solo DTOs con registros
                .collect(Collectors.toList());

        return result;
    }

    public List<DdjjListDto> findCfFueraTermino(int anio, MesesEnum mes, Long idEfector,
            CondicionDdjjEnum condicionDdjj) {

        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfectorAndTipoGuardiaAndCondicionDdjj(
                anio, mes, idEfector, TipoGuardiaEnum.CONTRAFACTURA, condicionDdjj);

        if (ddjjs.isEmpty()) {
            return Collections.emptyList();
        }

        List<DdjjListDto> result = ddjjs.stream()
                .map(ddjj -> {

                    // Filtrar solo registros mensuales activos
                    List<RegistroMensual> registrosActivos = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(rm -> {

                                // Filtrar solo regActiv activas
                                List<RegistroActividad> actividadesActivas = rm.getRegistroActividad().stream()
                                        .filter(actividad -> {
                                            boolean activo = actividad.isActivo();
                                            boolean guardiaCompleta = actividad.getEsGuardiaIncompleta() == null ||
                                                    !actividad.getEsGuardiaIncompleta();

                                            return activo && guardiaCompleta;
                                        })
                                        .collect(Collectors.toList());

                                rm.setRegistroActividad(actividadesActivas);
                                return rm;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty()) // Solo registros con actividades
                            .collect(Collectors.toList());

                    ddjj.setRegistrosMensuales(registrosActivos);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(dto -> !dto.getRegistrosMensuales().isEmpty()) // Solo DTOs con registros
                .collect(Collectors.toList());

        return result;
    }

    private DdjjListDto convertirADdjjListDto(Ddjj ddjj) {
        System.out.println("Convertiendo DDJJ ID: " + ddjj.getId() + " a DTO");

        DdjjListDto dto = new DdjjListDto();
        dto.setId(ddjj.getId());
        dto.setMes(ddjj.getMes());
        dto.setAnio(ddjj.getAnio());

        // Convertir registros mensuales usando el servicio de RegistroMensual
        List<RegistroMensualListDto> registrosMensualesDto = ddjj.getRegistrosMensuales().stream()
                .map(rm -> {
                    System.out.println("  Convirtiendo RegistroMensual ID: " + rm.getId() + " a DTO");

                    // Primero filtramos las actividades
                    List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad().stream()
                            .filter(actividad -> {
                                boolean activo = actividad.isActivo();
                                System.out.println("    Actividad ID: " + actividad.getId() + " - activo: " + activo);
                                return activo;
                            })
                            .collect(Collectors.toList());

                    System.out.println(
                            "    Actividades después de filtrar en conversión: " + actividadesFiltradas.size());

                    // Llamamos al método del servicio pasando el registro mensual y las actividades
                    // filtradas
                    return registroMensualService.convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                })
                .collect(Collectors.toList());

        dto.setRegistrosMensuales(registrosMensualesDto);

        // Director y Director DPH
        if (ddjj.getDirector() != null) {
            dto.setIdDirector(ddjj.getDirector().getId());
            System.out.println("  Director ID: " + ddjj.getDirector().getId());
        }
        if (ddjj.getDirectorDPH() != null) {
            dto.setIdDirectorDPH(ddjj.getDirectorDPH().getId());
            System.out.println("  Director DPH ID: " + ddjj.getDirectorDPH().getId());
        }

        dto.setEstadoDdjjDirector(ddjj.getEstadoDdjjDirector());
        dto.setEstadoDdjjDirectorDPH(ddjj.getEstadoDdjjDirectorDPH());
        dto.setEnPosesionDirector(ddjj.getEnPosesionDirector());
        dto.setEnPosesionDirectorDPH(ddjj.getEnPosesionDirectorDPH());
        dto.setMotivoDirector(ddjj.getMotivoDirector());
        dto.setMotivoDirectorDPH(ddjj.getMotivoDirectorDPH());

        if (ddjj.getTipoGuardia() != null) {
            dto.setIdTipoGuardia(ddjj.getTipoGuardia().getId());
            System.out.println("  TipoGuardia ID: " + ddjj.getTipoGuardia().getId());
        }

        System.out.println("  Total registros mensuales en DTO: " + dto.getRegistrosMensuales().size());
        return dto;
    }

}
package com.guardias.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroMensualDto;
import com.guardias.backend.dto.adicional.AdicionalListDto;
import com.guardias.backend.dto.asistencial.AsistencialListForRmensualDto;
import com.guardias.backend.dto.categoria.CategoriaListDto;
import com.guardias.backend.dto.factura.FacturaDetailDto;
import com.guardias.backend.dto.legajo.LegajoListDto;
import com.guardias.backend.dto.novedadPersonal.NovedadPersonalListDto;
import com.guardias.backend.dto.registroActividad.RegActivListDto;
import com.guardias.backend.dto.registroMensual.RegistroMensualListDto;
import com.guardias.backend.dto.revista.RevistaListDto;
import com.guardias.backend.dto.servicio.ServicioSummaryDto;
import com.guardias.backend.dto.sumaHoras.SumaHorasListDto;
import com.guardias.backend.dto.tipoGuardia.TipoGuardiaListDto;
import com.guardias.backend.dto.tipoLicencia.TipoLicenciaListDto;
import com.guardias.backend.dto.tipoRevista.TipoRevistaListDto;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.Factura;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.enums.EstadoFacturacionEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.DdjjRepository;
import com.guardias.backend.repository.RegistroMensualRepository;

@Service
@Transactional
public class RegistroMensualService {

        @Autowired
        RegistroMensualRepository registroMensualRepository;
        @Autowired
        DdjjRepository ddjjRepository;
        @Autowired
        AsistencialService asistencialService;
        @Autowired
        EfectorService efectorService;
        @Autowired
        SumaHorasService sumaHorasService;

        public Optional<List<RegistroMensual>> findByActivoTrue() {
                return registroMensualRepository.findByActivoTrue();
        }

        public List<RegistroMensual> findAll() {
                return registroMensualRepository.findAll();
        }

        public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCargoReagrupacion(int anio, MesesEnum mes,
                        Long idEfector) {

                List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes,
                                idEfector);

                // Filtro adicional para dejar solo las actividades de tipo CARGO o AGRUPACION
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(registroMensual -> {
                                        List<RegistroActividad> actividadesFiltradas = registroMensual
                                                        .getRegistroActividad().stream()
                                                        .filter(actividad -> actividad.isActivo() && // Filtrar
                                                                                                     // actividades
                                                                                                     // activas
                                                                        (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CARGO
                                                                                        ||
                                                                                        actividad.getTipoGuardia()
                                                                                                        .getNombre() == TipoGuardiaEnum.AGRUPACION))
                                                        .collect(Collectors.toList());
                                        registroMensual.setRegistroActividad(actividadesFiltradas);
                                        return registroMensual;
                                })
                                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir
                                                                                                              // registros
                                                                                                              // con
                                                                                                              // lista
                                                                                                              // vacía
                                .collect(Collectors.toList());
        }

        public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaExtra(int anio, MesesEnum mes,
                        Long idEfector) {
                List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes,
                                idEfector);

                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(registroMensual -> {
                                        List<RegistroActividad> actividadesFiltradas = registroMensual
                                                        .getRegistroActividad().stream()
                                                        .filter(actividad -> actividad.isActivo() && // Filtrar
                                                                                                     // actividades
                                                                                                     // activas
                                                                        actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.EXTRA)
                                                        .collect(Collectors.toList());
                                        registroMensual.setRegistroActividad(actividadesFiltradas);
                                        return registroMensual;
                                })
                                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir
                                                                                                              // registros
                                                                                                              // con
                                                                                                              // lista
                                                                                                              // vacía
                                .collect(Collectors.toList());
        }

        public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCF(int anio, MesesEnum mes,
                        Long idEfector) {
                List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes,
                                idEfector);

                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(registroMensual -> {
                                        List<RegistroActividad> actividadesFiltradas = registroMensual
                                                        .getRegistroActividad().stream()
                                                        .filter(actividad -> actividad.isActivo() && // Filtrar
                                                                                                     // actividades
                                                                                                     // activas
                                                                        actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CONTRAFACTURA)
                                                        .collect(Collectors.toList());
                                        registroMensual.setRegistroActividad(actividadesFiltradas);
                                        return registroMensual;
                                })
                                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir
                                                                                                              // registros
                                                                                                              // con
                                                                                                              // lista
                                                                                                              // vacía
                                .collect(Collectors.toList());
        }

        public Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId,
                        Long efectorId, MesesEnum mes, int anio) {
                return registroMensualRepository.findByAsistencialIdAndEfectorIdAndMesAndAnio(asistencialId, efectorId,
                                mes, anio);
        }

        public Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnioAndQuincena(Long asistencialId,
                        Long efectorId, MesesEnum mes, int anio, QuincenaEnum quincena) {
                return registroMensualRepository.findByAsistencialIdAndEfectorIdAndMesAndAnioAndQuincena(asistencialId,
                                efectorId, mes, anio, quincena);
        }

        public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCargoReagrupacionAndServicio(
                        int anio, MesesEnum mes, Long idEfector, Long idServicio) {

                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfectorAndServicio(anio, mes, idEfector, idServicio);

                // Filtro adicional para dejar solo las actividades de tipo CARGO o AGRUPACION Y
                // del servicio específico
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(registroMensual -> {
                                        List<RegistroActividad> actividadesFiltradas = registroMensual
                                                        .getRegistroActividad().stream()
                                                        .filter(actividad -> actividad.isActivo() && // Filtrar
                                                                                                     // actividades
                                                                                                     // activas
                                                                        (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CARGO
                                                                                        ||
                                                                                        actividad.getTipoGuardia()
                                                                                                        .getNombre() == TipoGuardiaEnum.AGRUPACION)
                                                                        &&
                                                                        actividad.getServicio().getId()
                                                                                        .equals(idServicio)) // Filtrar
                                                                                                             // por
                                                                                                             // servicio
                                                                                                             // específico
                                                        .collect(Collectors.toList());
                                        registroMensual.setRegistroActividad(actividadesFiltradas);
                                        return registroMensual;
                                })
                                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir
                                                                                                              // registros
                                                                                                              // con
                                                                                                              // lista
                                                                                                              // vacía
                                .collect(Collectors.toList());
        }

        public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaExtraAndServicio(int anio, MesesEnum mes,
                        Long idEfector, Long idServicio) {

                // Utilizamos directamente la consulta personalizada del repositorio
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfectorAndServicio(anio, mes, idEfector, idServicio);

                // Filtro adicional para dejar solo las actividades de tipo EXTRA Y del servicio
                // específico Y del servicio específico
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(registroMensual -> {
                                        List<RegistroActividad> actividadesFiltradas = registroMensual
                                                        .getRegistroActividad().stream()
                                                        .filter(actividad -> actividad.isActivo() && // Filtrar
                                                                                                     // actividades
                                                                                                     // activas
                                                                        actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.EXTRA
                                                                        &&
                                                                        actividad.getServicio().getId()
                                                                                        .equals(idServicio)) // Filtrar
                                                                                                             // por
                                                                                                             // servicio
                                                                                                             // específico
                                                        .collect(Collectors.toList());
                                        registroMensual.setRegistroActividad(actividadesFiltradas);
                                        return registroMensual;
                                })
                                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir
                                                                                                              // registros
                                                                                                              // con
                                                                                                              // lista
                                                                                                              // vacía
                                .collect(Collectors.toList());
        }

        public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCFAndServicio(int anio, MesesEnum mes,
                        Long idEfector, Long idServicio) {

                // Utilizamos directamente la consulta personalizada del repositorio
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfectorAndServicio(anio, mes, idEfector, idServicio);

                // Filtro adicional para dejar solo las actividades de tipo CONTRAFACTURA Y del
                // servicio específico
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(registroMensual -> {
                                        List<RegistroActividad> actividadesFiltradas = registroMensual
                                                        .getRegistroActividad().stream()
                                                        .filter(actividad -> actividad.isActivo() && // Filtrar
                                                                                                     // actividades
                                                                                                     // activas
                                                                        actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CONTRAFACTURA
                                                                        &&
                                                                        actividad.getServicio().getId()
                                                                                        .equals(idServicio)) // Filtrar
                                                                                                             // por
                                                                                                             // servicio
                                                                                                             // específico
                                                        .collect(Collectors.toList());
                                        registroMensual.setRegistroActividad(actividadesFiltradas);
                                        return registroMensual;
                                })
                                .filter(registroMensual -> !registroMensual.getRegistroActividad().isEmpty()) // Excluir
                                                                                                              // registros
                                                                                                              // con
                                                                                                              // lista
                                                                                                              // vacía
                                .collect(Collectors.toList());
        }

        // public Optional<Long> idByIdAsistencialAndMes(Long idAsistencial, Long
        // idEfector, MesesEnum mes, int anio) {
        // return registroMensualRepository.idByIdAsistencialAndMes(idAsistencial,
        // idEfector, mes, anio);
        // }

        public Optional<RegistroMensual> findById(Long id) {
                return registroMensualRepository.findById(id);
        }

        public Optional<RegistroMensual> findByIdAndActivoTrue(Long id) {
                return registroMensualRepository.findByIdAndActivoTrue(id);
        }

        boolean existsByAnioAndMes(int anio, MesesEnum mes) {
                return registroMensualRepository.existsByAnioAndMes(anio, mes);
        }

        public boolean existsByAsistencialId(Long idAsistencial) {
                return registroMensualRepository.existsByAsistencialId(idAsistencial);
        }

        public boolean existsById(Long id) {
                return registroMensualRepository.existsById(id);
        }

        public boolean activo(Long id) {
                return (registroMensualRepository.existsById(id)
                                && registroMensualRepository.findById(id).get().isActivo());
        }

        public void save(RegistroMensual registroMensual) {
                registroMensualRepository.save(registroMensual);
        }

        public void saveAll(List<RegistroMensual> registros) {
                registroMensualRepository.saveAll(registros);
        }

        public void deleteById(Long id) {
                registroMensualRepository.deleteById(id);
        }

        public ResponseEntity<?> validations(RegistroMensualDto registroMensualDto) {

                if (registroMensualDto.getMes() == null)
                        return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

                if (registroMensualDto.getAnio() < 1991)
                        return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

                if (registroMensualDto.getIdAsistencial() < 1)
                        return new ResponseEntity(new Mensaje("El id de la persona es incorrecto"),
                                        HttpStatus.BAD_REQUEST);

                return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
        }

        public RegistroMensual createUpdate(RegistroMensual registroMensual,
                        RegistroMensualDto registroMensualDto) {

                if (registroMensualDto.getMes() != null
                                && !registroMensualDto.getMes().equals(registroMensual.getMes()))
                        registroMensual.setMes(registroMensualDto.getMes());

                if (registroMensualDto.getAnio() != registroMensual.getAnio())
                        registroMensual.setAnio(registroMensualDto.getAnio());

                // if (registroMensualDto.getIdAsistencial() !=
                // registroMensual.getIdAsistencial())
                // registroMensual.setIdAsistencial(registroMensualDto.getIdAsistencial());

                if (registroMensualDto.getIdAsistencial() != null && (registroMensual.getAsistencial() == null
                                || !Objects.equals(registroMensual.getAsistencial().getId(),
                                                registroMensualDto.getIdAsistencial()))) {
                        registroMensual.setAsistencial(
                                        asistencialService.findById(registroMensualDto.getIdAsistencial()).get());
                }

                if (registroMensualDto.getIdEfector() != null && (registroMensual.getEfector() == null
                                || !Objects.equals(registroMensual.getEfector().getId(),
                                                registroMensualDto.getIdEfector()))) {
                        registroMensual.setEfector(efectorService.findById(registroMensualDto.getIdEfector()));
                }

                // Validar si idDdjjs no es null
                if (registroMensualDto.getIdDdjjs() != null) {
                        // Si no es null, procesar las ddjj
                        for (Long idDdjj : registroMensualDto.getIdDdjjs()) {
                                // Lógica para procesar cada idDdjj
                                Ddjj ddjj = ddjjRepository.findById(idDdjj).orElse(null);
                                if (ddjj != null && !registroMensual.getDdjjs().contains(ddjj)) {
                                        registroMensual.getDdjjs().add(ddjj);
                                        ddjj.getRegistrosMensuales().add(registroMensual);
                                }
                        }
                }

                registroMensual.setActivo(true);
                return registroMensual;
        }

        /* Crea un nuevo RegistroMensual con valores iniciales (horas/montos en 0) */
        public RegistroMensual createRegistroMensual(Long idAsistencial, Long idEfector, MesesEnum mesEnum, int anio,
                        QuincenaEnum quincena) {

                /* Inicializa un nuevo RegistroMensual con mes/año, asistencial,efector */
                RegistroMensual registroMensual = new RegistroMensual();
                registroMensual.setMes(mesEnum);
                registroMensual.setAnio(anio);
                registroMensual.setQuincena(quincena); // Puede ser null para tipos no CONTRAFACTURA
                registroMensual.setAsistencial(asistencialService.findById(idAsistencial).get());
                registroMensual.setEfector(efectorService.findById(idEfector));
                registroMensual.setActivo(true);
                if (quincena != null) {
                        registroMensual.setEstadoFacturacion(EstadoFacturacionEnum.PENDIENTE);
                }

                // Creo SumaHoras vacio
                SumaHoras horas = new SumaHoras();
                horas.setHorasLav(0L);
                horas.setHorasSdf(0L);
                registroMensual.setTotalHoras(horas);

                try {
                        save(registroMensual);
                        return registroMensual;
                } catch (Exception e) {
                        System.out.println("error al crear registro mensual  registroMensualService Ln196 -- "
                                        + e.getMessage());
                        return null;
                }
        }

        /*
         * Busca o crea un registro mensual para el asistencial/efector/mes/año y
         * acumula horas/montos
         */
        public RegistroActividad setRegistroMensual(RegistroActividad registroActividad) {

                // 1. Determinar si aplica quincena (solo para CONTRAFACTURA)
                boolean aplicaQuincena = registroActividad.getTipoGuardia() != null
                                && registroActividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CONTRAFACTURA;

                // 2. Identificación del registro
                Long idAsistencial = registroActividad.getAsistencial().getId();
                Long idEfector = registroActividad.getEfector().getId();
                MesesEnum mesEnum = MesesEnum.fromNumeroMes(registroActividad.getFechaIngreso().getMonthValue());
                int anio = registroActividad.getFechaIngreso().getYear();
                QuincenaEnum quincena = null;

                // 3. Determinar quincena solo si aplica
                if (aplicaQuincena) {
                        quincena = determinarQuincena(registroActividad);
                }

                // 4. Búsqueda del registro mensual existente
                Optional<RegistroMensual> registroExistente;

                if (aplicaQuincena) {
                        // Buscar RM por quincena para CONTRAFACTURA
                        registroExistente = findByAsistencialIdAndEfectorIdAndMesAndAnioAndQuincena(
                                        idAsistencial, idEfector, mesEnum, anio, quincena);
                } else {
                        // Buscar RM sin quincena para otros tipos de guardia
                        registroExistente = findByAsistencialIdAndEfectorIdAndMesAndAnio(
                                        idAsistencial, idEfector, mesEnum, anio);
                }

                RegistroMensual registroMensual;

                if (registroExistente.isPresent()) {
                        registroMensual = registroExistente.get();
                        System.out.println("##### ID Registro Mensual existente: " + registroMensual.getId());
                } else {
                        // Creación de nuevo registro con sumaHoras integrado
                        System.out.println("DEBUG - Creando nuevo registro mensual");

                        if (aplicaQuincena) {
                                registroMensual = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio,
                                                quincena);
                        } else {
                                registroMensual = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio, null);
                        }

                        // Crear y asignar SumaHoras
                        SumaHoras nuevasHoras = new SumaHoras();
                        nuevasHoras.setActivo(true);
                        sumaHorasService.save(nuevasHoras); // Persistir primero

                        registroMensual.setTotalHoras(nuevasHoras);
                        save(registroMensual); // Persistir el registro mensual

                        System.out.println("##### Nuevo ID Registro Mensual: " + registroMensual.getId() +
                                        " | ID SumaHoras: " + nuevasHoras.getId());
                }

                // 3. Acumular horas al registro mensual
                if (!Boolean.TRUE.equals(registroActividad.getEsGuardiaIncompleta())) {
                        SumaHoras horasMensuales = registroMensual.getTotalHoras();
                        SumaHoras horasGuardia = registroActividad.getHorasRealizadas();

                        System.out.println("DEBUG - Antes de acumular: SDF=" + horasMensuales.getHorasSdf() +
                                        " | LAV=" + horasMensuales.getHorasLav());

                        sumaHorasService.sumarHorasMensuales(horasMensuales, horasGuardia);
                        sumaHorasService.save(horasMensuales);

                        System.out.println("DEBUG - Después de acumular: SDF=" + horasMensuales.getHorasSdf() +
                                        " | LAV=" + horasMensuales.getHorasLav());
                }

                // 4. Vincular registro de actividad al mensual (sin modificar sus horas)
                registroActividad.setRegistroMensual(registroMensual);
                return registroActividad;
        }

        public RegistroActividad setRegistroMensualSinHoras(RegistroActividad registroActividad) {
                // Misma lógica de búsqueda/creación que setRegistroMensual pero SIN acumular horas
                
                // 1. Determinar si aplica quincena
                boolean aplicaQuincena = registroActividad.getTipoGuardia() != null
                                && registroActividad.getTipoGuardia().getNombre() == TipoGuardiaEnum.CONTRAFACTURA;

                // 2. Identificación del registro
                Long idAsistencial = registroActividad.getAsistencial().getId();
                Long idEfector = registroActividad.getEfector().getId();
                MesesEnum mesEnum = MesesEnum.fromNumeroMes(registroActividad.getFechaIngreso().getMonthValue());
                int anio = registroActividad.getFechaIngreso().getYear();
                QuincenaEnum quincena = null;

                // 3. Determinar quincena solo si aplica
                if (aplicaQuincena) {
                        quincena = determinarQuincena(registroActividad);
                }

                // 4. Búsqueda/creación del registro mensual (igual que antes)
                Optional<RegistroMensual> registroExistente;

                if (aplicaQuincena) {
                        registroExistente = findByAsistencialIdAndEfectorIdAndMesAndAnioAndQuincena(
                                        idAsistencial, idEfector, mesEnum, anio, quincena);
                } else {
                        registroExistente = findByAsistencialIdAndEfectorIdAndMesAndAnio(
                                        idAsistencial, idEfector, mesEnum, anio);
                }

                RegistroMensual registroMensual;

                if (registroExistente.isPresent()) {
                        registroMensual = registroExistente.get();
                } else {
                        // Creación de nuevo registro
                        if (aplicaQuincena) {
                                registroMensual = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio,
                                                quincena);
                        } else {
                                registroMensual = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio, null);
                        }

                        // Crear SumaHoras vacío para estructura consistente
                        SumaHoras nuevasHoras = new SumaHoras();
                        nuevasHoras.setActivo(true);
                        sumaHorasService.save(nuevasHoras);
                        registroMensual.setTotalHoras(nuevasHoras);
                        save(registroMensual);
                }

                // 5. Vincular registro de actividad al mensual SIN acumular horas
                registroActividad.setRegistroMensual(registroMensual);
                return registroActividad;
        }

        // Método auxiliar para determinar la quincena
        private QuincenaEnum determinarQuincena(RegistroActividad registroActividad) {

                int dia = registroActividad.getFechaIngreso().getDayOfMonth();
                return (dia <= 15) ? QuincenaEnum.PRIMERA : QuincenaEnum.SEGUNDA;
        }

        public List<RegistroMensualListDto> findByTipoGuardiaCargoReagrupacionAndServicio(
                        int anio, MesesEnum mes, Long idEfector, Long idServicio) {

                System.out.println("Iniciando consulta para año: {}, mes: {}, efector: {}, servicio: {}" + anio + mes
                                + idEfector + idServicio);
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfectorAndServicio(anio, mes, idEfector, idServicio);

                System.out.println("Registros encontrados en BD: {} " + registrosMensuales.size());
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(rm -> {
                                        // Filtrar actividades (CARGO/AGRUPACION + servicio)
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> actividad.isActivo()
                                                                        && (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CARGO
                                                                                        || actividad.getTipoGuardia()
                                                                                                        .getNombre() == TipoGuardiaEnum.AGRUPACION)
                                                                        && actividad.getServicio().getId()
                                                                                        .equals(idServicio))
                                                        .collect(Collectors.toList());

                                        // Convertir a DTO
                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                .filter(dto -> !dto.getRegistroActividad().isEmpty()) // Excluir DTOs sin actividades
                                .collect(Collectors.toList());
        }

        public List<RegistroMensualListDto> findByTipoGuardiaCargoReagrupacion(
                        int anio, MesesEnum mes, Long idEfector) {

                System.out.println(
                                "Parámetros recibidos - anio: " + anio + ", mes: " + mes + ", idEfector: " + idEfector);

                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfector(anio, mes, idEfector);

                System.out.println("Registros mensuales encontrados: " + registrosMensuales.size());

                return registrosMensuales.stream()
                                .filter(rm -> {
                                        System.out.println("Registro ID: " + rm.getId() + ", activo: " + rm.isActivo());
                                        return rm.isActivo();
                                })
                                .map(rm -> {
                                        System.out.println("Procesando registro ID: " + rm.getId());

                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> {
                                                                boolean cumpleCondicion = actividad.isActivo()
                                                                                && (actividad.getTipoGuardia()
                                                                                                .getNombre() == TipoGuardiaEnum.CARGO
                                                                                                || actividad.getTipoGuardia()
                                                                                                                .getNombre() == TipoGuardiaEnum.AGRUPACION);
                                                                System.out.println("Actividad ID: " + actividad.getId()
                                                                                +
                                                                                ", tipo: "
                                                                                + actividad.getTipoGuardia().getNombre()
                                                                                +
                                                                                ", activa: " + actividad.isActivo() +
                                                                                ", cumple: " + cumpleCondicion);
                                                                return cumpleCondicion;
                                                        })
                                                        .collect(Collectors.toList());

                                        System.out.println("Actividades filtradas: " + actividadesFiltradas.size());

                                        RegistroMensualListDto dto = convertirARegistroMensualCompletoDTO(rm,
                                                        actividadesFiltradas);

                                        System.out.println("DTO creado con actividades: "
                                                        + dto.getRegistroActividad().size());

                                        return dto;
                                })
                                .filter(dto -> {
                                        System.out.println("Filtrando DTO con actividades: "
                                                        + dto.getRegistroActividad().size());
                                        return !dto.getRegistroActividad().isEmpty();
                                })
                                .collect(Collectors.toList());
        }

        public List<RegistroMensualListDto> findByTipoGuardiaExtraAndServicio(
                        int anio, MesesEnum mes, Long idEfector, Long idServicio) {

                System.out.println("Iniciando consulta para año: {}, mes: {}, efector: {}, servicio: {}" + anio + mes
                                + idEfector + idServicio);
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfectorAndServicio(anio, mes, idEfector, idServicio);

                System.out.println("Registros encontrados en BD: {} " + registrosMensuales.size());
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(rm -> {
                                        // Filtrar actividades (EXTRA + servicio)
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> actividad.isActivo()
                                                                        && (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.EXTRA)
                                                                        && actividad.getServicio().getId()
                                                                                        .equals(idServicio))
                                                        .collect(Collectors.toList());

                                        // Convertir a DTO
                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                .filter(dto -> !dto.getRegistroActividad().isEmpty()) // Excluir DTOs sin actividades
                                .collect(Collectors.toList());
        }

        public List<RegistroMensualListDto> findByTipoGuardiaExtra(
                        int anio, MesesEnum mes, Long idEfector) {

                System.out.println("Iniciando consulta para año: {}, mes: {}, efector: {}" + anio + mes + idEfector);
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfector(anio, mes, idEfector);

                System.out.println("Registros encontrados en BD: {} " + registrosMensuales.size());
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(rm -> {
                                        // Filtrar actividades (EXTRA + servicio)
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> actividad.isActivo()
                                                                        && (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.EXTRA))
                                                        .collect(Collectors.toList());

                                        // Convertir a DTO
                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                .filter(dto -> !dto.getRegistroActividad().isEmpty()) // Excluir DTOs sin actividades
                                .collect(Collectors.toList());
        }

        public List<RegistroMensualListDto> findByTipoGuardiaCfAndServicio(
                        int anio, MesesEnum mes, Long idEfector, Long idServicio, QuincenaEnum quincena) {

                System.out.println("Iniciando consulta para año: {}, mes: {}, efector: {}, servicio: {}, quincena: {}"
                                + anio + mes
                                + idEfector + idServicio + quincena);
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfectorServicioAndQuincena(anio, mes, idEfector, idServicio, quincena);

                System.out.println("Registros encontrados en BD: {} " + registrosMensuales.size());
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(rm -> {
                                        // Filtrar actividades (CF + servicio)
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> actividad.isActivo()
                                                                        && (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CONTRAFACTURA)
                                                                        && actividad.getServicio().getId()
                                                                                        .equals(idServicio))
                                                        .collect(Collectors.toList());

                                        // Convertir a DTO
                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                .filter(dto -> !dto.getRegistroActividad().isEmpty()) // Excluir DTOs sin actividades
                                .collect(Collectors.toList());
        }

        public List<RegistroMensualListDto> findByTipoGuardiaCf(
                        int anio, MesesEnum mes, Long idEfector, QuincenaEnum quincena) {

                System.out.println("Iniciando consulta para año: " + anio + ", mes: " + mes + ", efector: " + idEfector
                                + ", quincena: " + quincena);
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findByAnioMesEfectorAndQuincena(anio, mes, idEfector, quincena);

                System.out.println("Registros encontrados en BD: {} " + registrosMensuales.size());
                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(rm -> {
                                        // Filtrar actividades (CF)
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> actividad.isActivo()
                                                                        && (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CONTRAFACTURA))
                                                        .collect(Collectors.toList());

                                        // Convertir a DTO
                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                .filter(dto -> !dto.getRegistroActividad().isEmpty()) // Excluir DTOs sin actividades
                                .collect(Collectors.toList());
        }

        public RegistroMensualListDto convertirARegistroMensualCompletoDTO(
                        RegistroMensual rm, List<RegistroActividad> actividadesFiltradas) {

                RegistroMensualListDto dto = new RegistroMensualListDto();
                dto.setId(rm.getId());
                dto.setMes(rm.getMes());
                dto.setAnio(rm.getAnio());
                dto.setQuincena(rm.getQuincena());
                dto.setEstadoFacturacion(rm.getEstadoFacturacion());

                // Asistencial
                if (rm.getAsistencial() != null) {
                        AsistencialListForRmensualDto asistencialDTO = new AsistencialListForRmensualDto();
                        asistencialDTO.setId(rm.getAsistencial().getId());
                        asistencialDTO.setApellido(rm.getAsistencial().getApellido());
                        asistencialDTO.setNombre(rm.getAsistencial().getNombre());
                        asistencialDTO.setDni(rm.getAsistencial().getDni());
                        asistencialDTO.setCuil(rm.getAsistencial().getCuil());

                        // Legajos
                        asistencialDTO.setLegajos(rm.getAsistencial().getLegajos().stream()
                                        .map(legajo -> {
                                                LegajoListDto legajoDTO = new LegajoListDto();
                                                if (legajo.getRevista() != null) {
                                                        RevistaListDto revistaDTO = new RevistaListDto();

                                                        // Manejo seguro de TipoRevista
                                                        if (legajo.getRevista().getTipoRevista() != null) {
                                                                revistaDTO.setTipoRevista(new TipoRevistaListDto(
                                                                                legajo.getRevista().getTipoRevista()
                                                                                                .getNombre()));
                                                        }

                                                        // Manejo seguro de Categoria
                                                        if (legajo.getRevista().getCategoria() != null) {
                                                                revistaDTO.setCategoria(new CategoriaListDto(
                                                                                legajo.getRevista().getCategoria()
                                                                                                .getNombre()));
                                                        }

                                                        // Manejo seguro de Adicional (¡esta era la línea que fallaba!)
                                                        if (legajo.getRevista().getAdicional() != null) {
                                                                revistaDTO.setAdicional(new AdicionalListDto(
                                                                                legajo.getRevista().getAdicional()
                                                                                                .getNombre()));
                                                        }

                                                        legajoDTO.setRevista(revistaDTO);
                                                }
                                                return legajoDTO;
                                        })
                                        .collect(Collectors.toList()));

                        // Novedades
                        asistencialDTO.setNovedadesPersonales(rm.getAsistencial().getNovedadesPersonales().stream()
                                        .map(novedad -> new NovedadPersonalListDto(
                                                        novedad.getId(),
                                                        novedad.getFechaInicio(),
                                                        novedad.getFechaFinal(),
                                                        novedad.getHoraInicio(),
                                                        novedad.getHoraFinal(),
                                                        new TipoLicenciaListDto(novedad.getTipoLicencia().getId(),
                                                                        novedad.getTipoLicencia().getNombre())))
                                        .collect(Collectors.toList()));

                        dto.setAsistencial(asistencialDTO);
                }

                // RegistroActividad (ya filtradas)
                List<RegActivListDto> actividadesDto = actividadesFiltradas.stream()
                                .map(actividad -> new RegActivListDto(
                                                actividad.getId(),
                                                actividad.getFechaIngreso(),
                                                actividad.getFechaEgreso(),
                                                actividad.getHoraIngreso(),
                                                actividad.getHoraEgreso(),
                                                new TipoGuardiaListDto(actividad.getTipoGuardia().getId(),
                                                                actividad.getTipoGuardia().getNombre().name()),

                                                new ServicioSummaryDto(actividad.getServicio().getId(),
                                                                actividad.getServicio().getDescripcion()),
                                                actividad.getHorasRealizadas() != null ? new SumaHorasListDto(
                                                                actividad.getHorasRealizadas().getId(),
                                                                actividad.getHorasRealizadas().getHorasLav(),
                                                                actividad.getHorasRealizadas().getHorasSdf(),
                                                                actividad.getHorasRealizadas().getMontoLav(),
                                                                actividad.getHorasRealizadas().getMontoSdf(),
                                                                actividad.getHorasRealizadas().getMontoTotal()) : null))
                                .collect(Collectors.toList());
                dto.setRegistroActividad(actividadesDto);

                // Calcular nuevo totalHoras basado en las actividades filtradas
                SumaHorasListDto nuevoTotalHoras = calcularTotalHorasDesdeActividades(actividadesFiltradas,
                                rm.getTotalHoras());
                dto.setTotalHoras(nuevoTotalHoras);

                if (rm.getDdjjs() != null) {
                        List<Long> ddjjIds = rm.getDdjjs().stream()
                                        .map(Ddjj::getId)
                                        .collect(Collectors.toList());
                        dto.setIdDdjjs(ddjjIds);
                }

                // Facturas
                if (rm.getFacturas() != null) {
                        List<FacturaDetailDto> facturasDto = rm.getFacturas().stream()
                                        .filter(Factura::isActivo) // Solo facturas activas
                                        .map(factura -> new FacturaDetailDto(
                                                        factura.getId(),
                                                        factura.getNombreTitular(),
                                                        factura.getApellidoTitular(),
                                                        factura.getDniTitular(),
                                                        factura.getCuilTitular(),
                                                        factura.getContribuyente(),
                                                        factura.getTipo(),
                                                        factura.getPuntoVenta(),
                                                        factura.getNumeroFactura(),
                                                        factura.getFechaEmision(),
                                                        factura.getMonto()))
                                        .collect(Collectors.toList());
                        dto.setFacturas(facturasDto);
                } else {
                        dto.setFacturas(new ArrayList<>()); // Lista vacía si no hay facturas
                }

                return dto;
        }

        private SumaHorasListDto calcularTotalHorasDesdeActividades(List<RegistroActividad> actividadesFiltradas,
                        SumaHoras totalHorasOriginal) {
                float totalHorasLav = 0;
                float totalHorasSdf = 0;
                BigDecimal totalMontoLav = BigDecimal.ZERO;
                BigDecimal totalMontoSdf = BigDecimal.ZERO;
                BigDecimal totalMontoTotal = BigDecimal.ZERO;

                for (RegistroActividad actividad : actividadesFiltradas) {
                        if (actividad.getHorasRealizadas() != null) {
                                SumaHoras horas = actividad.getHorasRealizadas();
                                totalHorasLav += horas.getHorasLav();
                                totalHorasSdf += horas.getHorasSdf();
                                totalMontoLav = totalMontoLav.add(
                                                horas.getMontoLav() != null ? horas.getMontoLav() : BigDecimal.ZERO);
                                totalMontoSdf = totalMontoSdf.add(
                                                horas.getMontoSdf() != null ? horas.getMontoSdf() : BigDecimal.ZERO);
                                totalMontoTotal = totalMontoTotal
                                                .add(horas.getMontoTotal() != null ? horas.getMontoTotal()
                                                                : BigDecimal.ZERO);
                        }
                }

                return new SumaHorasListDto(
                                totalHorasOriginal != null ? totalHorasOriginal.getId() : null,
                                totalHorasLav,
                                totalHorasSdf,
                                totalMontoLav,
                                totalMontoSdf,
                                totalMontoTotal);
        }

        public List<RegistroMensualListDto> mapToDtoList(List<RegistroMensual> registros, Long idTipoGuardia) {
                if (registros == null || registros.isEmpty()) {
                        return Collections.emptyList();
                }

                return registros.stream()
                                .map(rm -> {
                                        // Filtra actividades por tipoGuardia si está presente
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> idTipoGuardia == null || (actividad
                                                                        .getTipoGuardia() != null
                                                                        && actividad.getTipoGuardia().getId()
                                                                                        .equals(idTipoGuardia)))
                                                        .collect(Collectors.toList());

                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                .collect(Collectors.toList());
        }

        public BigDecimal getMontoTotalByQuincena(Long idAsistencial, Long idEfector, QuincenaEnum quincena,
                        MesesEnum mes, int anio) {
                return registroMensualRepository.findMontoTotalHorasByFiltros(idAsistencial, idEfector, quincena, mes,
                                anio);
        }

        public BigDecimal getMontoTotal(Long idAsistencial, Long idEfector, MesesEnum mes, int anio) {
                return registroMensualRepository.findMontoTotalHorasByFiltrosSinQuincena(idAsistencial, idEfector, mes,
                                anio);
        }

        public List<RegistroMensualListDto> findRegistrosIncompletos(Long efectorId, MesesEnum mes, int anio,
                        QuincenaEnum quincena) {

                List<RegistroMensual> registrosMensuales = registroMensualRepository.findRegistrosIncompletos(efectorId,
                                mes, anio, quincena, EstadoFacturacionEnum.PENDIENTE);

                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(rm -> {
                                        // Filtrar actividades (CF)
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> actividad.isActivo()
                                                                        && (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CONTRAFACTURA))
                                                        .collect(Collectors.toList());

                                        // Convertir a DTO
                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                .filter(dto -> !dto.getRegistroActividad().isEmpty()) // Excluir DTOs sin actividades
                                .collect(Collectors.toList());
        }

        public List<RegistroMensualListDto> findRegistrosFueraDeTermino(Long efectorId, MesesEnum mes, int anio) {

                // Lista de estados que queremos buscar
                List<EstadoFacturacionEnum> estadosBuscados = Arrays.asList(
                                EstadoFacturacionEnum.PENDIENTE,
                                EstadoFacturacionEnum.REGULARIZADO);

                // Buscar registros con los estados especificados
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findRegistrosFueraDeTermino(efectorId, mes, anio, estadosBuscados);

                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(rm -> {
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> actividad.isActivo()
                                                                        && (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CONTRAFACTURA))
                                                        .collect(Collectors.toList());

                                        // Convertir a DTO usando tu método existente
                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                // Si quieres excluir DTOs sin actividades, mantén esta línea:
                                .filter(dto -> !dto.getRegistroActividad().isEmpty())
                                .collect(Collectors.toList());
        }

        public List<RegistroMensualListDto> findRegistrosFueraDeTerminoPorServicio(Long efectorId, MesesEnum mes,
                        int anio, long idServicio) {

                // Lista de estados que queremos buscar
                List<EstadoFacturacionEnum> estadosBuscados = Arrays.asList(
                                EstadoFacturacionEnum.PENDIENTE,
                                EstadoFacturacionEnum.REGULARIZADO);

                // Buscar registros con los estados especificados
                List<RegistroMensual> registrosMensuales = registroMensualRepository
                                .findRegistrosFueraDeTermino(efectorId, mes, anio, estadosBuscados);

                return registrosMensuales.stream()
                                .filter(RegistroMensual::isActivo)
                                .map(rm -> {
                                        List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                                                        .stream()
                                                        .filter(actividad -> actividad.isActivo()
                                                                        && (actividad.getTipoGuardia()
                                                                                        .getNombre() == TipoGuardiaEnum.CONTRAFACTURA)
                                                                        && actividad.getServicio().getId()
                                                                                        .equals(idServicio))
                                                        .collect(Collectors.toList());

                                        // Convertir a DTO usando tu método existente
                                        return convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                                })
                                // Si quieres excluir DTOs sin actividades, mantén esta línea:
                                .filter(dto -> !dto.getRegistroActividad().isEmpty())
                                .collect(Collectors.toList());
        }

        public boolean existenRegistrosFueraDeTermino(Long efectorId, MesesEnum mes,
                        int anio) {

                // Lista de estados que queremos buscar
                List<EstadoFacturacionEnum> estadosBuscados = Arrays.asList(
                                EstadoFacturacionEnum.PENDIENTE,
                                EstadoFacturacionEnum.REGULARIZADO);

                List<RegistroMensual> lista = registroMensualRepository
                                .findRegistrosFueraDeTermino(efectorId, mes, anio, estadosBuscados);

                return !lista.isEmpty();

        }

        public boolean existenCompletos(Long efectorId, MesesEnum mes, int anio, QuincenaEnum quincena) {

                List<RegistroMensual> lista = registroMensualRepository
                                .findRegistrosCompletos(efectorId, mes, anio, quincena,
                                                EstadoFacturacionEnum.COMPLETADO);

                return !lista.isEmpty();

        }

        /**
     * Suma los montos totales de TODOS los registros mensuales por mes y año
     */
    public BigDecimal sumMontosRegistrosMensuales(Long efectorId, Long asistencialId, MesesEnum mes, int anio) {
        List<RegistroMensual> registros = registroMensualRepository.findByEfectorAndAsistencialAndMesAndAnio(
                efectorId, asistencialId, mes, anio);
        
        return registros.stream()
                .map(rm -> rm.getTotalHoras().getMontoTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Busca TODOS los registros mensuales por efector, asistencial, mes y año
     */
    public List<RegistroMensual> findByEfectorAndAsistencialAndMesAndAnio(
            Long efectorId, Long asistencialId, MesesEnum mes, int anio) {
        return registroMensualRepository.findByEfectorAndAsistencialAndMesAndAnio(
                efectorId, asistencialId, mes, anio);
    }

    /**
     * Busca registros por quincena específica
     */
    public List<RegistroMensual> findByEfectorAndAsistencialAndMesAndAnioAndQuincena(
            Long efectorId, Long asistencialId, MesesEnum mes, int anio, QuincenaEnum quincena) {
        return registroMensualRepository.findByEfectorAndAsistencialAndMesAndAnioAndQuincena(
                efectorId, asistencialId, mes, anio, quincena);
    }

    /**
     * Busca registros pendientes
     */
    public List<RegistroMensual> findRegistrosPendientes(Long efectorId, Long asistencialId, MesesEnum mes, int anio) {
        return registroMensualRepository.findRegistrosPendientes(efectorId, asistencialId, mes, anio, EstadoFacturacionEnum.PENDIENTE);
    }


   

}

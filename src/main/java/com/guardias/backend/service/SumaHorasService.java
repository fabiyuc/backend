package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.SumaHorasDto;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.enums.TipoDiaEnum;
import com.guardias.backend.repository.SumaHorasRepository;

@Service
@Transactional
public class SumaHorasService {
    @Autowired
    SumaHorasRepository sumaHorasRepository;
    @Autowired
    FeriadoService feriadoService;
    @Autowired
    ValorGmiService valorGmiService;

    public List<SumaHoras> findAll() {
        return sumaHorasRepository.findAll();
    }

    public Optional<List<SumaHoras>> findByActivoTrue() {
        return sumaHorasRepository.findByActivoTrue();
    }

    public boolean existsById(Long id) {
        return sumaHorasRepository.existsById(id);
    }

    public Optional<SumaHoras> findById(Long id) {
        return sumaHorasRepository.findById(id);
    }

    public boolean existByRegistroMensual(Long idRegistroMensual) {
        return sumaHorasRepository.existByRegistroMensual(idRegistroMensual);
    }

    public List<SumaHoras> findByRegistroMensual(Long idRegistroMensual, Long idAsistencial) {
        return sumaHorasRepository.findByRegistroMensual(idRegistroMensual, idAsistencial);
    }

    public boolean activo(Long id) {
        return sumaHorasRepository.existsById(id) && sumaHorasRepository.findById(id).get().isActivo();
    }

    public void save(SumaHoras sumaHoras) {
        sumaHorasRepository.save(sumaHoras);
    }

    public void deleteById(Long id) {
        sumaHorasRepository.deleteById(id);
    }

    public SumaHoras createUpdate(SumaHoras sumaHoras, SumaHorasDto sumaHorasDto) {

        // debe recibir todos los datos desde el registro diario
        return sumaHoras;
    }

    public SumaHoras calcularHoras(RegistroActividad registroActividad) {
        // Combina fecha y hora para obtener LocalDateTime
        LocalDateTime fechaHoraIngreso = LocalDateTime.of(registroActividad.getFechaIngreso(),
                registroActividad.getHoraIngreso());
        LocalDateTime fechaHoraEgreso = LocalDateTime.of(registroActividad.getFechaEgreso(),
                registroActividad.getHoraEgreso());
        Duration duration = Duration.between(fechaHoraIngreso, fechaHoraEgreso);
        int horasTotales = (int) duration.toHours();
        int minutosRestantes = (int) duration.toMinutesPart();

        DayOfWeek diaDeLaSemana = registroActividad.getFechaIngreso().getDayOfWeek();
        String dia = diaDeLaSemana.getDisplayName(TextStyle.FULL, Locale.of("es", "ES"));
        boolean isFeriado = feriadoService.existsByFecha(registroActividad.getFechaIngreso());
        ValorGmi valorGmi = valorGmiService.findByActivoTrue();

        SumaHoras horas = new SumaHoras();

        if (isFeriado) {
            horas.setTipoDia(TipoDiaEnum.valueOf("FERIADO"));
        } else if (dia.toUpperCase() == "SABADO" || dia.toUpperCase() == "DOMINGO") {
            horas.setTipoDia(TipoDiaEnum.valueOf("FINDE"));
            horas.setMontoHora(valorGmi.getMonto());// multiplicado por algun valor????

        } else {
            horas.setTipoDia(TipoDiaEnum.valueOf("NORMAL"));
            horas.setMontoHora(valorGmi.getMonto());
        }
        // COMO hago si es BONO???????????????????????????????????????????

        horas.setCantidadHoras(horasTotales);
        // Faltan sumar los minutos!!!!!!!!!!!!!!!!!!!!!!!! int minutosRestantes = (int)
        // duration.toMinutesPart();

        BigDecimal horasTotalesBigDecimal = BigDecimal.valueOf(horasTotales);
        horas.setTotalAPagar(horasTotalesBigDecimal.multiply(horas.getMontoHora()));
        save(horas);

        return horas;
    }

}

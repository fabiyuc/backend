package com.guardias.backend.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.repository.SumaHorasRepository;

@Service
@Transactional
public class SumaHorasService {
    @Autowired
    SumaHorasRepository sumaHorasRepository;
    @Autowired
    FeriadoService feriadoService;

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

    public float redondearHoras(float totalHours, float remainingMinutes) {

        float roundedHours;

        if (remainingMinutes < 16) {
            roundedHours = totalHours;
        } else if (remainingMinutes <= 45) {
            roundedHours = totalHours + 0.5f;
        } else {
            roundedHours = totalHours + 1;
        }

        return roundedHours;
    }

    public SumaHoras calcularHoras(LocalDate fechaIngreso, LocalDate fechaEgreso, LocalTime horaIngreso,
            LocalTime horaEgreso) {

        // 6 sabado 7 domingo
        int diaDeLaSemana = fechaIngreso.getDayOfWeek().getValue();
        LocalDateTime dateTimeIngreso = LocalDateTime.of(fechaIngreso, horaIngreso);
        LocalDateTime dateTimeEgreso = LocalDateTime.of(fechaEgreso, horaEgreso);
        Duration duration = Duration.between(dateTimeIngreso, dateTimeEgreso);

        float totalHours = duration.toHours();
        float remainingMinutes = duration.toMinutes() % 60;

        float roundedHours = redondearHoras(totalHours, remainingMinutes);
        SumaHoras totalHoras = new SumaHoras();

        if (feriadoService.existsByFecha(fechaIngreso) || diaDeLaSemana > 5) {
            // sabado domingo o feriado
            totalHoras.setHorasSdf(roundedHours);
        } else {
            // dia normal
            totalHoras.setHorasLav(roundedHours);
        }
        return totalHoras;
    }

    public SumaHoras sumarHorasMensuales(SumaHoras horas, SumaHoras horasASumar) {
        SumaHoras totalHoras = new SumaHoras();
        // DEBO tomar horas y sumarle las que vienen de horasaSumar

        totalHoras.getHorasLav();
        totalHoras.getHorasSdf();
        totalHoras.getBonoLav();
        totalHoras.getBonoSdf();
        totalHoras.getMontoHorasLav();
        totalHoras.getMontoHorasSdf();
        totalHoras.getMontoBonoLav();
        totalHoras.getMontoBonoSdf();
        totalHoras.getMontoTotal();

        return totalHoras;
    }
}

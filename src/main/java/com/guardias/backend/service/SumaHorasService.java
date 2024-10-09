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
            //Menos de 16 minutos: No se redondea, las horas permanecen iguales
            roundedHours = totalHours;
        } else if (remainingMinutes <= 45) {
            //Entre 16 y 45 minutos: Se redondea a media hora adicional
            roundedHours = totalHours + 0.5f;
        } else {
            //Más de 45 minutos: Se redondea a una hora adicional
            roundedHours = totalHours + 1;
        }

        return roundedHours;
    }

    // SOLO CALCULA HORAS no calcula los montos!!!!
    public SumaHoras calcularHoras(LocalDate fechaIngreso, LocalDate fechaEgreso, LocalTime horaIngreso,
            LocalTime horaEgreso) {

        // obtiene el día de la semana como un número, 6 sabado 7 domingo
        int diaDeLaSemana = fechaIngreso.getDayOfWeek().getValue();
        
        //combino la fecha y hora para obtener un instante completo en el tiempo (LocalDateTime)
        LocalDateTime dateTimeIngreso = LocalDateTime.of(fechaIngreso, horaIngreso);
        LocalDateTime dateTimeEgreso = LocalDateTime.of(fechaEgreso, horaEgreso);

        //calcula el tiempo transcurrido entre las dos marcas de tiempo
        Duration duration = Duration.between(dateTimeIngreso, dateTimeEgreso);

        //obtengo el total de horas de esa duración
        float totalHours = duration.toHours();

        //calcula los minutos sobrantes
        float remainingMinutes = duration.toMinutes() % 60;

        //que accion desencadena un minimo de 4hs????
        if (totalHours < 4) {
            System.out.println("error!!!!! son pocas horas");
        }

        //ajusta las horas trabajadas basándose en los minutos sobrantes
        float roundedHours = redondearHoras(totalHours, remainingMinutes);

        SumaHoras totalHoras = new SumaHoras();

        if (feriadoService.existsByFecha(fechaIngreso) || diaDeLaSemana > 5) {
            //Si el día es un sábado, domingo o feriado
            totalHoras.setHorasSdf(roundedHours);
        } else {
            // Si el día es un día laboral
            totalHoras.setHorasLav(roundedHours);
        }
        return totalHoras;
    }

    public SumaHoras sumarHorasMensuales(SumaHoras horas, SumaHoras horasASumar) {
        SumaHoras totalHoras = new SumaHoras();

        totalHoras.setHorasLav(totalHoras.getHorasLav() + horasASumar.getHorasLav());
        totalHoras.setHorasSdf(totalHoras.getHorasSdf() + horasASumar.getHorasSdf());
        totalHoras.setBonoLav(totalHoras.getBonoLav() + horasASumar.getBonoLav());
        totalHoras.setBonoSdf(totalHoras.getBonoSdf() + horasASumar.getBonoSdf());

        if (totalHoras.getMontoHorasLav() != null) {
            totalHoras.setMontoHorasLav(totalHoras.getMontoHorasLav().add(horasASumar.getMontoHorasLav()));
        } else {
            totalHoras.setMontoHorasLav(horasASumar.getMontoHorasLav());
        }
        if (totalHoras.getMontoHorasSdf() != null) {
            totalHoras.setMontoHorasSdf(totalHoras.getMontoHorasSdf().add(horasASumar.getMontoHorasSdf()));
        } else {
            totalHoras.setMontoHorasSdf(horasASumar.getMontoHorasSdf());
        }

        if (totalHoras.getMontoBonoLav() != null) {
            totalHoras.setMontoBonoLav(totalHoras.getMontoBonoLav().add(horasASumar.getMontoBonoLav()));
        } else {
            totalHoras.setMontoBonoLav(horasASumar.getMontoBonoLav());
        }

        if (totalHoras.getMontoBonoSdf() != null) {
            totalHoras.setMontoBonoSdf(totalHoras.getMontoBonoSdf().add(horasASumar.getMontoBonoSdf()));
        } else {
            totalHoras.setMontoBonoSdf(horasASumar.getMontoBonoSdf());
        }

        if (totalHoras.getMontoTotal() != null) {
            totalHoras.setMontoTotal(totalHoras.getMontoTotal().add(horasASumar.getMontoTotal()));
        } else {
            totalHoras.setMontoTotal(horasASumar.getMontoBonoSdf());
        }

        return totalHoras;
    }
}

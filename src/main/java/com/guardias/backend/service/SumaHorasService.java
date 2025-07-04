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

    /*Ajusta las horas trabajadas basándose en minutos sobrantes */
    public float redondearHoras(float totalHours, float remainingMinutes) {

        if (remainingMinutes <= 30) {
           return (float) Math.floor(totalHours); // Redondear hacia abajo
        } else {
            return (float) Math.ceil(totalHours); // Redondear hacia arriba
        }
        /* float roundedHours;

        // <16 minutos: No se redondea, las horas permanecen iguales
        if (remainingMinutes < 16) {
            roundedHours = totalHours;
        } else if (remainingMinutes <= 45) {
            //Entre 16 y 45 minutos: Se suma media hora adicional
            roundedHours = totalHours + 0.5f;
        } else {
            // >45 minutos: suma una hora adicional
            roundedHours = totalHours + 1;
        }

        return roundedHours; */
    }

    /*Calcula horas LAV(laborables) o SDF(sábados/domingos/feriados) */
    public SumaHoras calcularHoras(LocalDate fechaIngreso, LocalDate fechaEgreso, LocalTime horaIngreso, LocalTime horaEgreso) {

        // obtiene el día de la semana como un número, 6 sabado 7 domingo
        int diaDeLaSemana = fechaIngreso.getDayOfWeek().getValue();
        
        /* combina fecha y hora para obtener un instante completo en el tiempo (LocalDateTime)*/
        LocalDateTime dateTimeIngreso = LocalDateTime.of(fechaIngreso, horaIngreso);
        LocalDateTime dateTimeEgreso = LocalDateTime.of(fechaEgreso, horaEgreso);

        /*calcula duración entre las dos marcas de tiempo*/
        Duration duration = Duration.between(dateTimeIngreso, dateTimeEgreso);

        //obtengo el total de horas de esa duración
        float totalHours = duration.toHours();

        //calcula los minutos sobrantes
        float remainingMinutes = duration.toMinutes() % 60;

        //que accion desencadena un minimo de 4hs????
        if (totalHours < 4) {
            System.out.println("error!!!!! son pocas horas");
        }

        /*Redondea las horas según minutos sobrantes*/
        float roundedHours = redondearHoras(totalHours, remainingMinutes);

        SumaHoras totalHoras = new SumaHoras();

        /*Asigna horas a horasLav o horasSdf segun el dia */
        if (feriadoService.existsByFecha(fechaIngreso) || diaDeLaSemana > 5) {
            //Si el día es un sábado, domingo o feriado
            totalHoras.setHorasSdf(roundedHours);
        } else {
            // Si el día es un día laboral
            totalHoras.setHorasLav(roundedHours);
        }

        return totalHoras;
    }

    /*Suma los valores de horasASumar al objeto horas */
    public void sumarHorasMensuales(SumaHoras horas, SumaHoras horasASumar) {
        horas.setHorasLav(horas.getHorasLav() + horasASumar.getHorasLav());
        horas.setHorasSdf(horas.getHorasSdf() + horasASumar.getHorasSdf());
    
        if (horas.getMontoLav() != null) {
            horas.setMontoLav(horas.getMontoLav().add(horasASumar.getMontoLav()));
        } else {
            horas.setMontoLav(horasASumar.getMontoLav());
        }
        if (horas.getMontoSdf() != null) {
            horas.setMontoSdf(horas.getMontoSdf().add(horasASumar.getMontoSdf()));
        } else {
            horas.setMontoSdf(horasASumar.getMontoSdf());
        }
    
        if (horas.getMontoTotal() != null) {
            horas.setMontoTotal(horas.getMontoTotal().add(horasASumar.getMontoTotal()));
        } else {
            horas.setMontoTotal(horasASumar.getMontoTotal());
        }
    
        horas.setActivo(true);  
    }

}

package com.guardias.backend.scheduling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.guardias.backend.service.PendientesSemanalService;

@Component
public class ScheduledTasks {
    @Autowired
    PendientesSemanalService pendientesSemanalService;

    // @Scheduled(cron = "42 31 12 15 5")
    // @Scheduled(cron = "SEGUNDOS MINUTOS HORAS DIA MES")

    // @Scheduled(cron = "0 0 0 * * SAT")
    // @Scheduled(cron = "SEGUNDOS MINUTOS HORAS * DIA_DE_LA_SEMANA")
    // para poner dias de la semana, el anterior debe ser un asterisco

    // Tarea que se ejecuta todos los sabados a las 0 hs
    // para crear el pendiente semanal
    @Scheduled(cron = "0 0 0 * * SAT")
    public void executeWeeklyTask() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedNow = now.format(formatter);
        System.out.println("Tarea ejecutada los Sabados a las 0hs - Fecha y hora actual: " + formattedNow);
        pendientesSemanalService.createPendienteSemanal();

    }

    // Tarea que se ejecuta todos los días a las 11 am
    // para avisar los que no registraron la salida
    @Scheduled(cron = "0 0 11 * * *")
    public void executeDailyTask() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedNow = now.format(formatter);
        System.out.println("Tarea ejecutada todos los dias a las 11:00am - Fecha y hora actual: " + formattedNow);

    }

    // Tarea que se ejecuta una vez por segundo para probar el trigger
    @Scheduled(fixedRate = 10000)
    public void executeTaskEverySecond() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        String formattedNow = now.format(formatter);
        System.out.println("Tarea ejecutada cada segundo - Fecha y hora actual: " +
                formattedNow);
        pendientesSemanalService.createPendienteSemanal();
    }
}

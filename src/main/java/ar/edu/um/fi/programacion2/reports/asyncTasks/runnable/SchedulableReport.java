package ar.edu.um.fi.programacion2.reports.asyncTasks.runnable;

import ar.edu.um.fi.programacion2.reports.asyncTasks.dto.RespuestaReporteRequestDTO;
import ar.edu.um.fi.programacion2.reports.asyncTasks.exceptions.ExpiredReportException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class SchedulableReport implements Runnable {

    private Duration intervalo;

    private Instant fechaInicio;

    private Instant fechaFin;

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    private ScheduledFuture<?> future;

    private final RestTemplate restTemplate = new RestTemplate();

    private final HttpHeaders headers = new HttpHeaders();

    private Logger log = LoggerFactory.getLogger(SchedulableReport.class);

    public SchedulableReport(Duration intervalo, Instant fechaInicio, Instant fechaFin) {
        this.intervalo = intervalo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public SchedulableReport() {}

    @Override
    public void run() {
        // Comprobar que este reporte no fue cancelado

        // Comprobar que la fechaFin aun no ha llegado
        if (Instant.now().minus(3, ChronoUnit.HOURS).isAfter(fechaFin)) {
            log.warn(
                "Reporte recurrente on {} has expired : ReporteRecurr( fechaInicio={}, fechaFin={}, intervalo={} )",
                Thread.currentThread().getName(),
                fechaInicio,
                fechaFin,
                intervalo
            );
            future.cancel(true);
            return;
        }

        // Obtener las ventas del ultimo intervalo de tiempo

        try {
            // Tareas realizadas para construir el reporte historico...
            RespuestaReporteRequestDTO requestDTO = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(
                    new File("src/main/java/ar/edu/um/fi/programacion2/reports/asyncTasks/templates/respuesta_reporte.json"),
                    RespuestaReporteRequestDTO.class
                );

            // Construir request que se enviara al servicio principal
            headers.add(
                "Authorization",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0aXRvZm9vZCIsImF1dGgiOiIiLCJleHAiOjE5ODg4MjgwNTR9.4_P9ZyGuNCp8bwdKWsC22MJn4NGlpjJDcvvdg-UEwHhdyAylJ03qnGE6DJh2xdWYeQcnvMkjFgnzRK5sfn9sJQ"
            );
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(
                new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestDTO),
                headers
            );

            // Reportar al servicio principal
            String responseDTO = restTemplate.postForObject("http://10.101.102.1:8080/api/reporte/datos", request, String.class);

            // Loguear resultado
            log.info("{} - Reporte recurrente submitted successfully : {} ", Thread.currentThread().getName(), responseDTO.toString());
        } catch (RestClientException e) {
            log.error("{} - Error in REST client : {}", Thread.currentThread().getName(), e.getMessage());
        } catch (IOException e) {
            log.error("{} - Error reading JSON template : {}", Thread.currentThread().getName(), e.getMessage());
        }
    }
}

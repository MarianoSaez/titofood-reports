package ar.edu.um.fi.programacion2.reports.asyncTasks.runnable;

import ar.edu.um.fi.programacion2.reports.asyncTasks.dto.RespuestaReporteRequestDTO;
import ar.edu.um.fi.programacion2.reports.asyncTasks.dto.VentaForReportDTO;
import ar.edu.um.fi.programacion2.reports.asyncTasks.exceptions.ExpiredReportException;
import ar.edu.um.fi.programacion2.reports.config.ApplicationProperties;
import ar.edu.um.fi.programacion2.reports.domain.Reporte;
import ar.edu.um.fi.programacion2.reports.service.ReporteService;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class SchedulableReport implements Runnable {

    private Long id;

    private Duration intervalo;

    private Instant fechaInicio;

    private Instant fechaFin;

    private String token;

    private String uuid;

    private ReporteService reporteService;

    private ScheduledFuture<?> future;

    private final RestTemplate restTemplate = new RestTemplate();

    private final HttpHeaders headers = new HttpHeaders();

    private Logger log = LoggerFactory.getLogger(SchedulableReport.class);

    public SchedulableReport(
        Long id,
        Duration intervalo,
        Instant fechaInicio,
        Instant fechaFin,
        ReporteService reporteService,
        String token,
        String uuid
    ) {
        this.id = id;
        this.intervalo = intervalo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.reporteService = reporteService;
        this.token = token;
        this.uuid = uuid;
    }

    public SchedulableReport() {}

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    @Override
    public void run() {
        // Comprobar que este reporte no fue cancelado
        Optional<ReporteDTO> reporte = reporteService.findOne(id);
        if (reporte.isEmpty()) {
            log.warn("Reporte recurrente {} does not exist, cancelling thread on this report", id);
            future.cancel(true);
            return;
        }

        if (reporte.get().getCancelado()) {
            log.warn(
                "Reporte recurrente {} on {} has been cancelled : ReporteRecurr( fechaInicio={}, fechaFin={}, intervalo={} )",
                id,
                Thread.currentThread().getName(),
                fechaInicio,
                fechaFin,
                intervalo
            );
            future.cancel(true);
            return;
        }

        // Comprobar que la fechaFin aun no ha llegado
        if (Instant.now().minus(3, ChronoUnit.HOURS).isAfter(fechaFin)) {
            log.warn(
                "Reporte recurrente {} on {} has expired : ReporteRecurr( fechaInicio={}, fechaFin={}, intervalo={} )",
                id,
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
            // Obtener las ventas que cumplen el parametro del reporte
            // http://localhost:8080/api/ventas/2023-01-13T13:00:40/2023-01-13T23:59:40
            ResponseEntity<VentaForReportDTO[]> ventasResponse = restTemplate.getForEntity(
                "http://localhost:8080/api/ventas/" + LocalDateTime.now().minus(intervalo) + "/" + LocalDateTime.now(),
                VentaForReportDTO[].class
            );
            List<VentaForReportDTO> ventas = List.of(ventasResponse.getBody());
            log.info("Franchise service has responded with {} ventas made in the las interval of {}", ventas.size(), intervalo);

            // Tareas realizadas para construir el reporte historico...
            RespuestaReporteRequestDTO requestDTO = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(
                    new File("src/main/java/ar/edu/um/fi/programacion2/reports/asyncTasks/templates/respuesta_reporte.json"),
                    RespuestaReporteRequestDTO.class
                );

            // Agregar ventas a los datos
            requestDTO.setDatos(ventas);

            // Construir request que se enviara al servicio principal
            headers.add("Authorization", "Bearer " + token);
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

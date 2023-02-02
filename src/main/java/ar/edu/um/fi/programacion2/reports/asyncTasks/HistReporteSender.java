package ar.edu.um.fi.programacion2.reports.asyncTasks;

import ar.edu.um.fi.programacion2.reports.asyncTasks.dto.RespuestaReporteRequestDTO;
import ar.edu.um.fi.programacion2.reports.asyncTasks.dto.VentaForReportDTO;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import ar.edu.um.fi.programacion2.reports.service.dto.VentaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("histReporteSender")
public class HistReporteSender implements ReporteSender {

    private final Logger log = LoggerFactory.getLogger(HistReporteSender.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final HttpHeaders headers = new HttpHeaders();

    @Async
    public void sendReport(ReporteDTO report) {
        RespuestaReporteRequestDTO requestDTO;

        try {
            // Obtener las ventas que cumplen el parametro del reporte
            ResponseEntity<VentaForReportDTO[]> ventasResponse = restTemplate.getForEntity(
                "http://localhost:8080/api/ventas",
                VentaForReportDTO[].class
            );
            List<VentaForReportDTO> ventas = List.of(ventasResponse.getBody());

            // Construir y cargar el reporte a enviar
            requestDTO =
                new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .readValue(
                        new File("src/main/java/ar/edu/um/fi/programacion2/reports/asyncTasks/templates/respuesta_reporte.json"),
                        RespuestaReporteRequestDTO.class
                    );

            // Agregar historial de ventas al reporte
            requestDTO.setDatos(ventas);

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
            log.info("Report submitted successfully : {} ", responseDTO.toString());
        } catch (RestClientException e) {
            log.error("Error in REST client : {}", e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("Error parsing to JSON : {}", e.getMessage());
        } catch (IOException e) {
            log.error("Error reading JSON template : {}", e.getMessage());
        }
    }
}

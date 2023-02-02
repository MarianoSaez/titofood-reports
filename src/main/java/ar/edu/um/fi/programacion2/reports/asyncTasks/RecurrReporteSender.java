package ar.edu.um.fi.programacion2.reports.asyncTasks;

import ar.edu.um.fi.programacion2.reports.asyncTasks.runnable.SchedulableReport;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("recurrReporteSender")
public class RecurrReporteSender implements ReporteSender {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final Logger log = LoggerFactory.getLogger(RecurrReporteSender.class);

    public void sendReport(ReporteDTO report) {
        // Obtener fecha de inicio y fecha fin
        Instant fechaInicio = report.getFechaInicio().toInstant();
        Instant fechaFin = report.getFechaFin().toInstant();

        // Obtener intervalo de reporte
        Duration intervalo = Duration.parse(report.getIntervalo());

        // Crear hilo para reporte y dotarlo de los atributos necesarios para que se detenga de forma autonoma
        SchedulableReport schedulableReport = new SchedulableReport(intervalo, fechaInicio, fechaFin);
        ScheduledFuture<?> future = threadPoolTaskScheduler.scheduleAtFixedRate(schedulableReport, fechaInicio, intervalo);
        schedulableReport.setFuture(future);

        // Log a consola con informacion de la tarea
        log.info("Reporte recurrente scheduled from {} to {} every {} : {}", fechaInicio, fechaFin, intervalo, report);
    }
}

package ar.edu.um.fi.programacion2.reports.asyncTasks;

import ar.edu.um.fi.programacion2.reports.service.ReporteService;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelReporte {

    private final Logger log = LoggerFactory.getLogger(CancelReporte.class);

    @Autowired
    private ReporteService reporteService;

    public void cancel(ReporteDTO report) {
        Optional<ReporteDTO> reporteDTO = reporteService.findOneByForeignId(report.getReporteCanceladoId());
        if (reporteDTO.isPresent()) {
            ReporteDTO cancelledReport = reporteDTO.get();
            cancelledReport.setCancelado(true);
            reporteService.save(cancelledReport);
            log.info("Reporte recurrente has been cancelled successfully : {}", cancelledReport);
        } else {
            log.error("Requested report is not present : {}", report);
        }
    }
}

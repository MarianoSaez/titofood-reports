package ar.edu.um.fi.programacion2.reports.asyncTasks;

import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;

public interface ReporteSender {
    void sendReport(ReporteDTO report);
}

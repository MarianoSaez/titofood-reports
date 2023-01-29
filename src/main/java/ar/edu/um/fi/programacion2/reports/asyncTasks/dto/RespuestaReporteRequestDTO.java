package ar.edu.um.fi.programacion2.reports.asyncTasks.dto;

import ar.edu.um.fi.programacion2.reports.service.dto.VentaDTO;
import java.util.List;

public class RespuestaReporteRequestDTO {

    private String accion;
    private List<VentaForReportDTO> datos;

    public RespuestaReporteRequestDTO() {}

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public List<VentaForReportDTO> getDatos() {
        return datos;
    }

    public void setDatos(List<VentaForReportDTO> datos) {
        this.datos = datos;
    }
    //    @Override
    //    public String toString() {
    //        return "RespuestaReporteRequestDTO{" +
    //            "accion='" + accion + '\'' +
    //            ", datos=" + datos +
    //            '}';
    //    }
}

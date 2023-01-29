package ar.edu.um.fi.programacion2.reports.asyncTasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RespuestaReporteResponseDTO {

    private String accion;
    private String estado;
    //    @JsonIgnore
    private DatoReportadoEstadoDTO datoReportadoEstadoDTO;

    public RespuestaReporteResponseDTO() {}

    @Override
    public String toString() {
        return (
            "RespuestaReporteResponseDTO{" +
            "accion='" +
            accion +
            '\'' +
            ", estado='" +
            estado +
            '\'' +
            ", datoReportadoEstadoDTO=" +
            datoReportadoEstadoDTO +
            '}'
        );
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public DatoReportadoEstadoDTO getDatoReportadoEstadoDTO() {
        return datoReportadoEstadoDTO;
    }

    public void setDatoReportadoEstadoDTO(DatoReportadoEstadoDTO datoReportadoEstadoDTO) {
        this.datoReportadoEstadoDTO = datoReportadoEstadoDTO;
    }
}

package ar.edu.um.fi.programacion2.reports.asyncTasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class DatoReportadoEstadoDTO {

    private String accion;
    private String estado;
    private String errorMotivo;
    //    @JsonIgnore
    private List<String> erroneos;

    public DatoReportadoEstadoDTO() {}

    @Override
    public String toString() {
        return (
            "DatoReportadoEstadoDTO{" +
            "accion='" +
            accion +
            '\'' +
            ", estado='" +
            estado +
            '\'' +
            ", errorMotivo='" +
            errorMotivo +
            '\'' +
            ", erroneos=" +
            erroneos +
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

    public String getErrorMotivo() {
        return errorMotivo;
    }

    public void setErrorMotivo(String errorMotivo) {
        this.errorMotivo = errorMotivo;
    }

    public List<String> getErroneos() {
        return erroneos;
    }

    public void setErroneos(List<String> erroneos) {
        this.erroneos = erroneos;
    }
}

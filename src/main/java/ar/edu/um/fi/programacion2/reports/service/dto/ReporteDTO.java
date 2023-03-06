package ar.edu.um.fi.programacion2.reports.service.dto;

import ar.edu.um.fi.programacion2.reports.domain.enumeration.TipoReporte;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ar.edu.um.fi.programacion2.reports.domain.Reporte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReporteDTO implements Serializable {

    private Long id;

    private TipoReporte tipo;

    private ZonedDateTime fechaInicio;

    private ZonedDateTime fechaFin;

    private String intervalo;

    private Long foreignId;

    private Boolean cancelado;

    private Long reporteCanceladoId;

    private Set<VentaDTO> ventas = new HashSet<>();

    @Override
    public String toString() {
        return (
            "ReporteDTO{" +
            "id=" +
            id +
            ", tipo=" +
            tipo +
            ", fechaInicio=" +
            fechaInicio +
            ", fechaFin=" +
            fechaFin +
            ", intervalo='" +
            intervalo +
            '\'' +
            ", foreignId=" +
            foreignId +
            ", cancelado=" +
            cancelado +
            ", reporteCanceladoId=" +
            reporteCanceladoId +
            ", ventas=" +
            ventas +
            '}'
        );
    }

    public Long getReporteCanceladoId() {
        return reporteCanceladoId;
    }

    public void setReporteCanceladoId(Long reporteCanceladoId) {
        this.reporteCanceladoId = reporteCanceladoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoReporte getTipo() {
        return tipo;
    }

    public void setTipo(TipoReporte tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(ZonedDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ZonedDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(ZonedDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }

    public Long getForeignId() {
        return foreignId;
    }

    public void setForeignId(Long foreignId) {
        this.foreignId = foreignId;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Set<VentaDTO> getVentas() {
        return ventas;
    }

    public void setVentas(Set<VentaDTO> ventas) {
        this.ventas = ventas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReporteDTO)) {
            return false;
        }

        ReporteDTO reporteDTO = (ReporteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reporteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}

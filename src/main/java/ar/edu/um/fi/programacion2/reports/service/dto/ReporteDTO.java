package ar.edu.um.fi.programacion2.reports.service.dto;

import ar.edu.um.fi.programacion2.reports.domain.enumeration.TipoReporte;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonIgnore
    private Long id;

    private TipoReporte tipo;

    private ZonedDateTime fechaInicio;

    private ZonedDateTime fechaFin;

    private String intervalo;

    @JsonProperty("id")
    private Long foreignId;

    private Set<VentaDTO> ventas = new HashSet<>();

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

    // prettier-ignore
    @Override
    public String toString() {
        return "ReporteDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", intervalo='" + getIntervalo() + "'" +
            ", foreignId=" + getForeignId() +
            ", ventas=" + getVentas() +
            "}";
    }
}

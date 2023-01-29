package ar.edu.um.fi.programacion2.reports.asyncTasks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class VentaForReportDTO {

    private Long id;
    private Long foreignId;
    private Float precio;
    private Instant fecha;

    public VentaForReportDTO() {}

    @JsonProperty("ventaId")
    public Long getId() {
        return id; // Enviar este atributo causa errores en el servicio principal
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("menu")
    public Long getForeignId() {
        return foreignId;
    }

    @JsonProperty("foreignId")
    public void setForeignId(Long foreignId) {
        this.foreignId = foreignId;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }
    //    @Override
    //    public String toString() {
    //        return "VentaForReportDTO{" +
    //            "id=" + id +
    //            ", foreignId=" + foreignId +
    //            ", precio=" + precio +
    //            ", fecha=" + fecha +
    //            '}';
    //    }
}

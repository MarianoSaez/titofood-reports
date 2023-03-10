package ar.edu.um.fi.programacion2.reports.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private Instant fecha;

    @Column(name = "precio")
    private Float precio;

    @Column(name = "foreign_id")
    private Double foreignId;

    @ManyToMany(mappedBy = "ventas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ventas" }, allowSetters = true)
    private Set<Reporte> reportes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Venta fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Float getPrecio() {
        return this.precio;
    }

    public Venta precio(Float precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Double getForeignId() {
        return this.foreignId;
    }

    public Venta foreignId(Double foreignId) {
        this.setForeignId(foreignId);
        return this;
    }

    public void setForeignId(Double foreignId) {
        this.foreignId = foreignId;
    }

    public Set<Reporte> getReportes() {
        return this.reportes;
    }

    public void setReportes(Set<Reporte> reportes) {
        if (this.reportes != null) {
            this.reportes.forEach(i -> i.removeVenta(this));
        }
        if (reportes != null) {
            reportes.forEach(i -> i.addVenta(this));
        }
        this.reportes = reportes;
    }

    public Venta reportes(Set<Reporte> reportes) {
        this.setReportes(reportes);
        return this;
    }

    public Venta addReporte(Reporte reporte) {
        this.reportes.add(reporte);
        reporte.getVentas().add(this);
        return this;
    }

    public Venta removeReporte(Reporte reporte) {
        this.reportes.remove(reporte);
        reporte.getVentas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", precio=" + getPrecio() +
            ", foreignId=" + getForeignId() +
            "}";
    }
}

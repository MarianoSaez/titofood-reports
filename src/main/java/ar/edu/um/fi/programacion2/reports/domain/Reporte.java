package ar.edu.um.fi.programacion2.reports.domain;

import ar.edu.um.fi.programacion2.reports.domain.enumeration.TipoReporte;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reporte.
 */
@Entity
@Table(name = "reporte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoReporte tipo;

    @Column(name = "fecha_inicio")
    private ZonedDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private ZonedDateTime fechaFin;

    @Column(name = "intervalo")
    private String intervalo;

    @ManyToMany
    @JoinTable(
        name = "rel_reporte__venta",
        joinColumns = @JoinColumn(name = "reporte_id"),
        inverseJoinColumns = @JoinColumn(name = "venta_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reportes" }, allowSetters = true)
    private Set<Venta> ventas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reporte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoReporte getTipo() {
        return this.tipo;
    }

    public Reporte tipo(TipoReporte tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoReporte tipo) {
        this.tipo = tipo;
    }

    public ZonedDateTime getFechaInicio() {
        return this.fechaInicio;
    }

    public Reporte fechaInicio(ZonedDateTime fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(ZonedDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ZonedDateTime getFechaFin() {
        return this.fechaFin;
    }

    public Reporte fechaFin(ZonedDateTime fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(ZonedDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIntervalo() {
        return this.intervalo;
    }

    public Reporte intervalo(String intervalo) {
        this.setIntervalo(intervalo);
        return this;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }

    public Set<Venta> getVentas() {
        return this.ventas;
    }

    public void setVentas(Set<Venta> ventas) {
        this.ventas = ventas;
    }

    public Reporte ventas(Set<Venta> ventas) {
        this.setVentas(ventas);
        return this;
    }

    public Reporte addVenta(Venta venta) {
        this.ventas.add(venta);
        venta.getReportes().add(this);
        return this;
    }

    public Reporte removeVenta(Venta venta) {
        this.ventas.remove(venta);
        venta.getReportes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reporte)) {
            return false;
        }
        return id != null && id.equals(((Reporte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reporte{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", intervalo='" + getIntervalo() + "'" +
            "}";
    }
}

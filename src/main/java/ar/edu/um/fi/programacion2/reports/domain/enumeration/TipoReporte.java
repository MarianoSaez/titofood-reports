package ar.edu.um.fi.programacion2.reports.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The TipoReporte enumeration.
 */
public enum TipoReporte {
    @JsonProperty("recurrente")
    RECURR("recurrente"),

    @JsonProperty("historico")
    HIST("historico"),

    @JsonProperty("cancelar")
    CANCELAR("cancelar");

    private final String value;

    TipoReporte(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

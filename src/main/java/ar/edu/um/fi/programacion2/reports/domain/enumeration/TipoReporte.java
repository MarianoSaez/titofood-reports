package ar.edu.um.fi.programacion2.reports.domain.enumeration;

/**
 * The TipoReporte enumeration.
 */
public enum TipoReporte {
    RECURR("recurrente"),
    HIST("historico");

    private final String value;

    TipoReporte(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

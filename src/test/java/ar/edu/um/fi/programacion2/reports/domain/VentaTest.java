package ar.edu.um.fi.programacion2.reports.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.fi.programacion2.reports.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venta.class);
        Venta venta1 = new Venta();
        venta1.setId(1L);
        Venta venta2 = new Venta();
        venta2.setId(venta1.getId());
        assertThat(venta1).isEqualTo(venta2);
        venta2.setId(2L);
        assertThat(venta1).isNotEqualTo(venta2);
        venta1.setId(null);
        assertThat(venta1).isNotEqualTo(venta2);
    }
}

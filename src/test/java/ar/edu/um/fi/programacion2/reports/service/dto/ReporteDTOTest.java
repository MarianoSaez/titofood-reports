package ar.edu.um.fi.programacion2.reports.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.fi.programacion2.reports.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReporteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReporteDTO.class);
        ReporteDTO reporteDTO1 = new ReporteDTO();
        reporteDTO1.setId(1L);
        ReporteDTO reporteDTO2 = new ReporteDTO();
        assertThat(reporteDTO1).isNotEqualTo(reporteDTO2);
        reporteDTO2.setId(reporteDTO1.getId());
        assertThat(reporteDTO1).isEqualTo(reporteDTO2);
        reporteDTO2.setId(2L);
        assertThat(reporteDTO1).isNotEqualTo(reporteDTO2);
        reporteDTO1.setId(null);
        assertThat(reporteDTO1).isNotEqualTo(reporteDTO2);
    }
}

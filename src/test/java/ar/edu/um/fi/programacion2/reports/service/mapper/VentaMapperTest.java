package ar.edu.um.fi.programacion2.reports.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VentaMapperTest {

    private VentaMapper ventaMapper;

    @BeforeEach
    public void setUp() {
        ventaMapper = new VentaMapperImpl();
    }
}

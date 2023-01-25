package ar.edu.um.fi.programacion2.reports.service.mapper;

import ar.edu.um.fi.programacion2.reports.domain.Venta;
import ar.edu.um.fi.programacion2.reports.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Venta} and its DTO {@link VentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentaMapper extends EntityMapper<VentaDTO, Venta> {}

package ar.edu.um.fi.programacion2.reports.service.mapper;

import ar.edu.um.fi.programacion2.reports.domain.Reporte;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reporte} and its DTO {@link ReporteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReporteMapper extends EntityMapper<ReporteDTO, Reporte> {}

package ar.edu.um.fi.programacion2.reports.service.mapper;

import ar.edu.um.fi.programacion2.reports.domain.Reporte;
import ar.edu.um.fi.programacion2.reports.domain.Venta;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import ar.edu.um.fi.programacion2.reports.service.dto.VentaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reporte} and its DTO {@link ReporteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReporteMapper extends EntityMapper<ReporteDTO, Reporte> {
    @Mapping(target = "ventas", source = "ventas", qualifiedByName = "ventaIdSet")
    ReporteDTO toDto(Reporte s);

    @Mapping(target = "removeVenta", ignore = true)
    Reporte toEntity(ReporteDTO reporteDTO);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);

    @Named("ventaIdSet")
    default Set<VentaDTO> toDtoVentaIdSet(Set<Venta> venta) {
        return venta.stream().map(this::toDtoVentaId).collect(Collectors.toSet());
    }
}

package ar.edu.um.fi.programacion2.reports.repository;

import ar.edu.um.fi.programacion2.reports.domain.Reporte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ReporteRepositoryWithBagRelationships {
    Optional<Reporte> fetchBagRelationships(Optional<Reporte> reporte);

    List<Reporte> fetchBagRelationships(List<Reporte> reportes);

    Page<Reporte> fetchBagRelationships(Page<Reporte> reportes);
}

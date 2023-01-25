package ar.edu.um.fi.programacion2.reports.repository;

import ar.edu.um.fi.programacion2.reports.domain.Reporte;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ReporteRepositoryWithBagRelationshipsImpl implements ReporteRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reporte> fetchBagRelationships(Optional<Reporte> reporte) {
        return reporte.map(this::fetchVentas);
    }

    @Override
    public Page<Reporte> fetchBagRelationships(Page<Reporte> reportes) {
        return new PageImpl<>(fetchBagRelationships(reportes.getContent()), reportes.getPageable(), reportes.getTotalElements());
    }

    @Override
    public List<Reporte> fetchBagRelationships(List<Reporte> reportes) {
        return Optional.of(reportes).map(this::fetchVentas).orElse(Collections.emptyList());
    }

    Reporte fetchVentas(Reporte result) {
        return entityManager
            .createQuery("select reporte from Reporte reporte left join fetch reporte.ventas where reporte is :reporte", Reporte.class)
            .setParameter("reporte", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Reporte> fetchVentas(List<Reporte> reportes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, reportes.size()).forEach(index -> order.put(reportes.get(index).getId(), index));
        List<Reporte> result = entityManager
            .createQuery(
                "select distinct reporte from Reporte reporte left join fetch reporte.ventas where reporte in :reportes",
                Reporte.class
            )
            .setParameter("reportes", reportes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

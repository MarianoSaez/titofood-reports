package ar.edu.um.fi.programacion2.reports.repository;

import ar.edu.um.fi.programacion2.reports.domain.Reporte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reporte entity.
 *
 * When extending this class, extend ReporteRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ReporteRepository extends ReporteRepositoryWithBagRelationships, JpaRepository<Reporte, Long> {
    Optional<Reporte> findByForeignId(Long foreignId);

    default Optional<Reporte> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Reporte> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Reporte> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}

package ar.edu.um.fi.programacion2.reports.repository;

import ar.edu.um.fi.programacion2.reports.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}

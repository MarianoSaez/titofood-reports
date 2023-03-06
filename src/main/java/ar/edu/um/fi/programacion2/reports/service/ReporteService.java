package ar.edu.um.fi.programacion2.reports.service;

import ar.edu.um.fi.programacion2.reports.domain.Reporte;
import ar.edu.um.fi.programacion2.reports.repository.ReporteRepository;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import ar.edu.um.fi.programacion2.reports.service.mapper.ReporteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reporte}.
 */
@Service
@Transactional
public class ReporteService {

    private final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;

    private final ReporteMapper reporteMapper;

    public ReporteService(ReporteRepository reporteRepository, ReporteMapper reporteMapper) {
        this.reporteRepository = reporteRepository;
        this.reporteMapper = reporteMapper;
    }

    /**
     * Get a reporte by its foreignId
     *
     * @param foreignId of the entity
     * @return the entity matching the foreignId provided
     */
    public Optional<ReporteDTO> findOneByForeignId(Long foreignId) {
        Optional<Reporte> r = reporteRepository.findByForeignId(foreignId);

        ReporteDTO reporteDTO = null;
        if (r.isPresent()) {
            reporteDTO = reporteMapper.toDto(r.get());
        }
        return Optional.of(reporteDTO);
    }

    /**
     * Save a reporte.
     *
     * @param reporteDTO the entity to save.
     * @return the persisted entity.
     */
    public ReporteDTO save(ReporteDTO reporteDTO) {
        log.debug("Request to save Reporte : {}", reporteDTO);
        Reporte reporte = reporteMapper.toEntity(reporteDTO);
        reporte = reporteRepository.save(reporte);
        return reporteMapper.toDto(reporte);
    }

    /**
     * Update a reporte.
     *
     * @param reporteDTO the entity to save.
     * @return the persisted entity.
     */
    public ReporteDTO update(ReporteDTO reporteDTO) {
        log.debug("Request to update Reporte : {}", reporteDTO);
        Reporte reporte = reporteMapper.toEntity(reporteDTO);
        reporte = reporteRepository.save(reporte);
        return reporteMapper.toDto(reporte);
    }

    /**
     * Partially update a reporte.
     *
     * @param reporteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReporteDTO> partialUpdate(ReporteDTO reporteDTO) {
        log.debug("Request to partially update Reporte : {}", reporteDTO);

        return reporteRepository
            .findById(reporteDTO.getId())
            .map(existingReporte -> {
                reporteMapper.partialUpdate(existingReporte, reporteDTO);

                return existingReporte;
            })
            .map(reporteRepository::save)
            .map(reporteMapper::toDto);
    }

    /**
     * Get all the reportes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReporteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reportes");
        return reporteRepository.findAll(pageable).map(reporteMapper::toDto);
    }

    /**
     * Get all the reportes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReporteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reporteRepository.findAllWithEagerRelationships(pageable).map(reporteMapper::toDto);
    }

    /**
     * Get one reporte by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReporteDTO> findOne(Long id) {
        log.debug("Request to get Reporte : {}", id);
        return reporteRepository.findOneWithEagerRelationships(id).map(reporteMapper::toDto);
    }

    /**
     * Delete the reporte by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reporte : {}", id);
        reporteRepository.deleteById(id);
    }
}

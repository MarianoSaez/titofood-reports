package ar.edu.um.fi.programacion2.reports.web.rest;

import ar.edu.um.fi.programacion2.reports.asyncTasks.CancelReporte;
import ar.edu.um.fi.programacion2.reports.asyncTasks.HistReporteSender;
import ar.edu.um.fi.programacion2.reports.asyncTasks.RecurrReporteSender;
import ar.edu.um.fi.programacion2.reports.asyncTasks.ReporteSender;
import ar.edu.um.fi.programacion2.reports.domain.enumeration.TipoReporte;
import ar.edu.um.fi.programacion2.reports.repository.ReporteRepository;
import ar.edu.um.fi.programacion2.reports.service.ReporteService;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import ar.edu.um.fi.programacion2.reports.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.fi.programacion2.reports.domain.Reporte}.
 */
@RestController
@RequestMapping("/api")
public class ReporteResource {

    private final Logger log = LoggerFactory.getLogger(ReporteResource.class);

    private static final String ENTITY_NAME = "reporte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReporteService reporteService;

    private final ReporteRepository reporteRepository;

    @Autowired
    private ReporteSender recurrReporteSender;

    @Autowired
    private ReporteSender histReporteSender;

    @Autowired
    private CancelReporte cancelReporte;

    public ReporteResource(ReporteService reporteService, ReporteRepository reporteRepository) {
        this.reporteService = reporteService;
        this.reporteRepository = reporteRepository;
    }

    /**
     * {@code POST  /reportes} : Create a new reporte.
     *
     * @param reporteDTO the reporteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reporteDTO, or with status {@code 400 (Bad Request)} if the reporte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     *
     * Also this method handles the start up of the thread that will interact with the main service.
     */
    @PostMapping("/reportes")
    public ResponseEntity<ReporteDTO> createReporte(@RequestBody ReporteDTO reporteDTO) throws URISyntaxException {
        log.debug("REST request to save Reporte : {}", reporteDTO);
        if (reporteDTO.getId() != null) {
            throw new BadRequestAlertException("A new reporte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reporteDTO.setCancelado(false);
        ReporteDTO result = reporteService.save(reporteDTO);

        if (reporteDTO.getTipo().equals(TipoReporte.HIST)) {
            histReporteSender.sendReport(result);
        } else if (reporteDTO.getTipo().equals(TipoReporte.RECURR)) {
            recurrReporteSender.sendReport(result);
        } else if (reporteDTO.getTipo().equals(TipoReporte.CANCELAR)) {
            cancelReporte.cancel(reporteDTO);
        }

        return ResponseEntity
            .created(new URI("/api/reportes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reportes/:id} : Updates an existing reporte.
     *
     * @param id the id of the reporteDTO to save.
     * @param reporteDTO the reporteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reporteDTO,
     * or with status {@code 400 (Bad Request)} if the reporteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reporteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reportes/{id}")
    public ResponseEntity<ReporteDTO> updateReporte(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReporteDTO reporteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reporte : {}, {}", id, reporteDTO);
        if (reporteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reporteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reporteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReporteDTO result = reporteService.update(reporteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reporteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reportes/:id} : Partial updates given fields of an existing reporte, field will ignore if it is null
     *
     * @param id the id of the reporteDTO to save.
     * @param reporteDTO the reporteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reporteDTO,
     * or with status {@code 400 (Bad Request)} if the reporteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reporteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reporteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reportes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReporteDTO> partialUpdateReporte(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReporteDTO reporteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reporte partially : {}, {}", id, reporteDTO);
        if (reporteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reporteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reporteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReporteDTO> result = reporteService.partialUpdate(reporteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reporteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reportes} : get all the reportes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportes in body.
     */
    @GetMapping("/reportes")
    public ResponseEntity<List<ReporteDTO>> getAllReportes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Reportes");
        Page<ReporteDTO> page;
        if (eagerload) {
            page = reporteService.findAllWithEagerRelationships(pageable);
        } else {
            page = reporteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reportes/:id} : get the "id" reporte.
     *
     * @param id the id of the reporteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reporteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reportes/{id}")
    public ResponseEntity<ReporteDTO> getReporte(@PathVariable Long id) {
        log.debug("REST request to get Reporte : {}", id);
        Optional<ReporteDTO> reporteDTO = reporteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reporteDTO);
    }

    /**
     * {@code DELETE  /reportes/:id} : delete the "id" reporte.
     *
     * @param id the id of the reporteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Void> deleteReporte(@PathVariable Long id) {
        log.debug("REST request to delete Reporte : {}", id);
        reporteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

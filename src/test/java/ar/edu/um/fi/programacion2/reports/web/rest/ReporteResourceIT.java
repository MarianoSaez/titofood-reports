package ar.edu.um.fi.programacion2.reports.web.rest;

import static ar.edu.um.fi.programacion2.reports.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.fi.programacion2.reports.IntegrationTest;
import ar.edu.um.fi.programacion2.reports.domain.Reporte;
import ar.edu.um.fi.programacion2.reports.domain.enumeration.TipoReporte;
import ar.edu.um.fi.programacion2.reports.repository.ReporteRepository;
import ar.edu.um.fi.programacion2.reports.service.ReporteService;
import ar.edu.um.fi.programacion2.reports.service.dto.ReporteDTO;
import ar.edu.um.fi.programacion2.reports.service.mapper.ReporteMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReporteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReporteResourceIT {

    private static final TipoReporte DEFAULT_TIPO = TipoReporte.RECURR;
    private static final TipoReporte UPDATED_TIPO = TipoReporte.HIST;

    private static final ZonedDateTime DEFAULT_FECHA_INICIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_INICIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FECHA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_INTERVALO = "AAAAAAAAAA";
    private static final String UPDATED_INTERVALO = "BBBBBBBBBB";

    private static final Long DEFAULT_FOREIGN_ID = 1L;
    private static final Long UPDATED_FOREIGN_ID = 2L;

    private static final String ENTITY_API_URL = "/api/reportes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReporteRepository reporteRepository;

    @Mock
    private ReporteRepository reporteRepositoryMock;

    @Autowired
    private ReporteMapper reporteMapper;

    @Mock
    private ReporteService reporteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReporteMockMvc;

    private Reporte reporte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reporte createEntity(EntityManager em) {
        Reporte reporte = new Reporte()
            .tipo(DEFAULT_TIPO)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .intervalo(DEFAULT_INTERVALO)
            .foreignId(DEFAULT_FOREIGN_ID);
        return reporte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reporte createUpdatedEntity(EntityManager em) {
        Reporte reporte = new Reporte()
            .tipo(UPDATED_TIPO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .intervalo(UPDATED_INTERVALO)
            .foreignId(UPDATED_FOREIGN_ID);
        return reporte;
    }

    @BeforeEach
    public void initTest() {
        reporte = createEntity(em);
    }

    @Test
    @Transactional
    void createReporte() throws Exception {
        int databaseSizeBeforeCreate = reporteRepository.findAll().size();
        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);
        restReporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reporteDTO)))
            .andExpect(status().isCreated());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeCreate + 1);
        Reporte testReporte = reporteList.get(reporteList.size() - 1);
        assertThat(testReporte.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testReporte.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testReporte.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testReporte.getIntervalo()).isEqualTo(DEFAULT_INTERVALO);
        assertThat(testReporte.getForeignId()).isEqualTo(DEFAULT_FOREIGN_ID);
    }

    @Test
    @Transactional
    void createReporteWithExistingId() throws Exception {
        // Create the Reporte with an existing ID
        reporte.setId(1L);
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        int databaseSizeBeforeCreate = reporteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reporteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportes() throws Exception {
        // Initialize the database
        reporteRepository.saveAndFlush(reporte);

        // Get all the reporteList
        restReporteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reporte.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(sameInstant(DEFAULT_FECHA_INICIO))))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(sameInstant(DEFAULT_FECHA_FIN))))
            .andExpect(jsonPath("$.[*].intervalo").value(hasItem(DEFAULT_INTERVALO)))
            .andExpect(jsonPath("$.[*].foreignId").value(hasItem(DEFAULT_FOREIGN_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportesWithEagerRelationshipsIsEnabled() throws Exception {
        when(reporteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReporteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reporteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reporteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReporteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reporteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReporte() throws Exception {
        // Initialize the database
        reporteRepository.saveAndFlush(reporte);

        // Get the reporte
        restReporteMockMvc
            .perform(get(ENTITY_API_URL_ID, reporte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reporte.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(sameInstant(DEFAULT_FECHA_INICIO)))
            .andExpect(jsonPath("$.fechaFin").value(sameInstant(DEFAULT_FECHA_FIN)))
            .andExpect(jsonPath("$.intervalo").value(DEFAULT_INTERVALO))
            .andExpect(jsonPath("$.foreignId").value(DEFAULT_FOREIGN_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingReporte() throws Exception {
        // Get the reporte
        restReporteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReporte() throws Exception {
        // Initialize the database
        reporteRepository.saveAndFlush(reporte);

        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();

        // Update the reporte
        Reporte updatedReporte = reporteRepository.findById(reporte.getId()).get();
        // Disconnect from session so that the updates on updatedReporte are not directly saved in db
        em.detach(updatedReporte);
        updatedReporte
            .tipo(UPDATED_TIPO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .intervalo(UPDATED_INTERVALO)
            .foreignId(UPDATED_FOREIGN_ID);
        ReporteDTO reporteDTO = reporteMapper.toDto(updatedReporte);

        restReporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reporteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reporteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
        Reporte testReporte = reporteList.get(reporteList.size() - 1);
        assertThat(testReporte.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testReporte.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReporte.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testReporte.getIntervalo()).isEqualTo(UPDATED_INTERVALO);
        assertThat(testReporte.getForeignId()).isEqualTo(UPDATED_FOREIGN_ID);
    }

    @Test
    @Transactional
    void putNonExistingReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();
        reporte.setId(count.incrementAndGet());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reporteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reporteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();
        reporte.setId(count.incrementAndGet());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reporteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();
        reporte.setId(count.incrementAndGet());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReporteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reporteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReporteWithPatch() throws Exception {
        // Initialize the database
        reporteRepository.saveAndFlush(reporte);

        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();

        // Update the reporte using partial update
        Reporte partialUpdatedReporte = new Reporte();
        partialUpdatedReporte.setId(reporte.getId());

        partialUpdatedReporte.fechaInicio(UPDATED_FECHA_INICIO);

        restReporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReporte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReporte))
            )
            .andExpect(status().isOk());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
        Reporte testReporte = reporteList.get(reporteList.size() - 1);
        assertThat(testReporte.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testReporte.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReporte.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testReporte.getIntervalo()).isEqualTo(DEFAULT_INTERVALO);
        assertThat(testReporte.getForeignId()).isEqualTo(DEFAULT_FOREIGN_ID);
    }

    @Test
    @Transactional
    void fullUpdateReporteWithPatch() throws Exception {
        // Initialize the database
        reporteRepository.saveAndFlush(reporte);

        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();

        // Update the reporte using partial update
        Reporte partialUpdatedReporte = new Reporte();
        partialUpdatedReporte.setId(reporte.getId());

        partialUpdatedReporte
            .tipo(UPDATED_TIPO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .intervalo(UPDATED_INTERVALO)
            .foreignId(UPDATED_FOREIGN_ID);

        restReporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReporte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReporte))
            )
            .andExpect(status().isOk());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
        Reporte testReporte = reporteList.get(reporteList.size() - 1);
        assertThat(testReporte.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testReporte.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testReporte.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testReporte.getIntervalo()).isEqualTo(UPDATED_INTERVALO);
        assertThat(testReporte.getForeignId()).isEqualTo(UPDATED_FOREIGN_ID);
    }

    @Test
    @Transactional
    void patchNonExistingReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();
        reporte.setId(count.incrementAndGet());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reporteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reporteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();
        reporte.setId(count.incrementAndGet());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reporteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().size();
        reporte.setId(count.incrementAndGet());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReporteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reporteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReporte() throws Exception {
        // Initialize the database
        reporteRepository.saveAndFlush(reporte);

        int databaseSizeBeforeDelete = reporteRepository.findAll().size();

        // Delete the reporte
        restReporteMockMvc
            .perform(delete(ENTITY_API_URL_ID, reporte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reporte> reporteList = reporteRepository.findAll();
        assertThat(reporteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

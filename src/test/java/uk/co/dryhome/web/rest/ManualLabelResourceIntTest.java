package uk.co.dryhome.web.rest;

import uk.co.dryhome.Dryhomecrm1App;

import uk.co.dryhome.domain.ManualLabel;
import uk.co.dryhome.repository.ManualLabelRepository;
import uk.co.dryhome.service.ManualLabelService;
import uk.co.dryhome.service.MergeDocService;
import uk.co.dryhome.service.dto.ManualLabelDTO;
import uk.co.dryhome.service.mapper.ManualLabelMapper;
import uk.co.dryhome.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static uk.co.dryhome.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ManualLabelResource REST controller.
 *
 * @see ManualLabelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dryhomecrm1App.class)
public class ManualLabelResourceIntTest {

    private static final String DEFAULT_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_3 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_4 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_4 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_5 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_5 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_6 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_6 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_7 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_7 = "BBBBBBBBBB";

    @Autowired
    private ManualLabelRepository manualLabelRepository;

    @Autowired
    private ManualLabelMapper manualLabelMapper;

    @Autowired
    private ManualLabelService manualLabelService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restManualLabelMockMvc;

    private ManualLabel manualLabel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ManualLabelResource manualLabelResource = new ManualLabelResource(manualLabelService);
        this.restManualLabelMockMvc = MockMvcBuilders.standaloneSetup(manualLabelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualLabel createEntity(EntityManager em) {
        ManualLabel manualLabel = new ManualLabel()
            .line1(DEFAULT_LINE_1)
            .line2(DEFAULT_LINE_2)
            .line3(DEFAULT_LINE_3)
            .line4(DEFAULT_LINE_4)
            .line5(DEFAULT_LINE_5)
            .line6(DEFAULT_LINE_6)
            .line7(DEFAULT_LINE_7);
        return manualLabel;
    }

    @Before
    public void initTest() {
        manualLabel = createEntity(em);
    }

    @Test
    @Transactional
    public void createManualLabel() throws Exception {
        int databaseSizeBeforeCreate = manualLabelRepository.findAll().size();

        // Create the ManualLabel
        ManualLabelDTO manualLabelDTO = manualLabelMapper.toDto(manualLabel);
        restManualLabelMockMvc.perform(post("/api/manual-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualLabelDTO)))
            .andExpect(status().isCreated());

        // Validate the ManualLabel in the database
        List<ManualLabel> manualLabelList = manualLabelRepository.findAll();
        assertThat(manualLabelList).hasSize(databaseSizeBeforeCreate + 1);
        ManualLabel testManualLabel = manualLabelList.get(manualLabelList.size() - 1);
        assertThat(testManualLabel.getLine1()).isEqualTo(DEFAULT_LINE_1);
        assertThat(testManualLabel.getLine2()).isEqualTo(DEFAULT_LINE_2);
        assertThat(testManualLabel.getLine3()).isEqualTo(DEFAULT_LINE_3);
        assertThat(testManualLabel.getLine4()).isEqualTo(DEFAULT_LINE_4);
        assertThat(testManualLabel.getLine5()).isEqualTo(DEFAULT_LINE_5);
        assertThat(testManualLabel.getLine6()).isEqualTo(DEFAULT_LINE_6);
        assertThat(testManualLabel.getLine7()).isEqualTo(DEFAULT_LINE_7);

    }

    @Test
    @Transactional
    public void createManualLabelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = manualLabelRepository.findAll().size();

        // Create the ManualLabel with an existing ID
        manualLabel.setId(1L);
        ManualLabelDTO manualLabelDTO = manualLabelMapper.toDto(manualLabel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restManualLabelMockMvc.perform(post("/api/manual-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualLabelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ManualLabel in the database
        List<ManualLabel> manualLabelList = manualLabelRepository.findAll();
        assertThat(manualLabelList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = manualLabelRepository.findAll().size();
        // set the field null
        manualLabel.setLine1(null);

        // Create the ManualLabel, which fails.
        ManualLabelDTO manualLabelDTO = manualLabelMapper.toDto(manualLabel);

        restManualLabelMockMvc.perform(post("/api/manual-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualLabelDTO)))
            .andExpect(status().isBadRequest());

        List<ManualLabel> manualLabelList = manualLabelRepository.findAll();
        assertThat(manualLabelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllManualLabels() throws Exception {
        // Initialize the database
        manualLabelRepository.saveAndFlush(manualLabel);

        // Get all the manualLabelList
        restManualLabelMockMvc.perform(get("/api/manual-labels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manualLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].line1").value(hasItem(DEFAULT_LINE_1.toString())))
            .andExpect(jsonPath("$.[*].line2").value(hasItem(DEFAULT_LINE_2.toString())))
            .andExpect(jsonPath("$.[*].line3").value(hasItem(DEFAULT_LINE_3.toString())))
            .andExpect(jsonPath("$.[*].line4").value(hasItem(DEFAULT_LINE_4.toString())))
            .andExpect(jsonPath("$.[*].line5").value(hasItem(DEFAULT_LINE_5.toString())))
            .andExpect(jsonPath("$.[*].line6").value(hasItem(DEFAULT_LINE_6.toString())))
            .andExpect(jsonPath("$.[*].line7").value(hasItem(DEFAULT_LINE_7.toString())));
    }

    @Test
    @Transactional
    public void getManualLabel() throws Exception {
        // Initialize the database
        manualLabelRepository.saveAndFlush(manualLabel);

        // Get the manualLabel
        restManualLabelMockMvc.perform(get("/api/manual-labels/{id}", manualLabel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(manualLabel.getId().intValue()))
            .andExpect(jsonPath("$.line1").value(DEFAULT_LINE_1.toString()))
            .andExpect(jsonPath("$.line2").value(DEFAULT_LINE_2.toString()))
            .andExpect(jsonPath("$.line3").value(DEFAULT_LINE_3.toString()))
            .andExpect(jsonPath("$.line4").value(DEFAULT_LINE_4.toString()))
            .andExpect(jsonPath("$.line5").value(DEFAULT_LINE_5.toString()))
            .andExpect(jsonPath("$.line6").value(DEFAULT_LINE_6.toString()))
            .andExpect(jsonPath("$.line7").value(DEFAULT_LINE_7.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingManualLabel() throws Exception {
        // Get the manualLabel
        restManualLabelMockMvc.perform(get("/api/manual-labels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManualLabel() throws Exception {
        // Initialize the database
        manualLabelRepository.saveAndFlush(manualLabel);

        int databaseSizeBeforeUpdate = manualLabelRepository.findAll().size();

        // Update the manualLabel
        ManualLabel updatedManualLabel = manualLabelRepository.findById(manualLabel.getId()).get();
        // Disconnect from session so that the updates on updatedManualLabel are not directly saved in db
        em.detach(updatedManualLabel);
        updatedManualLabel
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .line3(UPDATED_LINE_3)
            .line4(UPDATED_LINE_4)
            .line5(UPDATED_LINE_5)
            .line6(UPDATED_LINE_6)
            .line7(UPDATED_LINE_7);
        ManualLabelDTO manualLabelDTO = manualLabelMapper.toDto(updatedManualLabel);

        restManualLabelMockMvc.perform(put("/api/manual-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualLabelDTO)))
            .andExpect(status().isOk());

        // Validate the ManualLabel in the database
        List<ManualLabel> manualLabelList = manualLabelRepository.findAll();
        assertThat(manualLabelList).hasSize(databaseSizeBeforeUpdate);
        ManualLabel testManualLabel = manualLabelList.get(manualLabelList.size() - 1);
        assertThat(testManualLabel.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testManualLabel.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testManualLabel.getLine3()).isEqualTo(UPDATED_LINE_3);
        assertThat(testManualLabel.getLine4()).isEqualTo(UPDATED_LINE_4);
        assertThat(testManualLabel.getLine5()).isEqualTo(UPDATED_LINE_5);
        assertThat(testManualLabel.getLine6()).isEqualTo(UPDATED_LINE_6);
        assertThat(testManualLabel.getLine7()).isEqualTo(UPDATED_LINE_7);

    }

    @Test
    @Transactional
    public void updateNonExistingManualLabel() throws Exception {
        int databaseSizeBeforeUpdate = manualLabelRepository.findAll().size();

        // Create the ManualLabel
        ManualLabelDTO manualLabelDTO = manualLabelMapper.toDto(manualLabel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualLabelMockMvc.perform(put("/api/manual-labels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualLabelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ManualLabel in the database
        List<ManualLabel> manualLabelList = manualLabelRepository.findAll();
        assertThat(manualLabelList).hasSize(databaseSizeBeforeUpdate);

    }

    @Test
    @Transactional
    public void deleteManualLabel() throws Exception {
        // Initialize the database
        manualLabelRepository.saveAndFlush(manualLabel);

        int databaseSizeBeforeDelete = manualLabelRepository.findAll().size();

        // Delete the manualLabel
        restManualLabelMockMvc.perform(delete("/api/manual-labels/{id}", manualLabel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ManualLabel> manualLabelList = manualLabelRepository.findAll();
        assertThat(manualLabelList).hasSize(databaseSizeBeforeDelete - 1);

    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualLabel.class);
        ManualLabel manualLabel1 = new ManualLabel();
        manualLabel1.setId(1L);
        ManualLabel manualLabel2 = new ManualLabel();
        manualLabel2.setId(manualLabel1.getId());
        assertThat(manualLabel1).isEqualTo(manualLabel2);
        manualLabel2.setId(2L);
        assertThat(manualLabel1).isNotEqualTo(manualLabel2);
        manualLabel1.setId(null);
        assertThat(manualLabel1).isNotEqualTo(manualLabel2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualLabelDTO.class);
        ManualLabelDTO manualLabelDTO1 = new ManualLabelDTO();
        manualLabelDTO1.setId(1L);
        ManualLabelDTO manualLabelDTO2 = new ManualLabelDTO();
        assertThat(manualLabelDTO1).isNotEqualTo(manualLabelDTO2);
        manualLabelDTO2.setId(manualLabelDTO1.getId());
        assertThat(manualLabelDTO1).isEqualTo(manualLabelDTO2);
        manualLabelDTO2.setId(2L);
        assertThat(manualLabelDTO1).isNotEqualTo(manualLabelDTO2);
        manualLabelDTO1.setId(null);
        assertThat(manualLabelDTO1).isNotEqualTo(manualLabelDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(manualLabelMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(manualLabelMapper.fromId(null)).isNull();
    }
}

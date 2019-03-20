package uk.co.dryhome.web.rest;

import uk.co.dryhome.Dryhomecrm1App;

import uk.co.dryhome.domain.Company;
import uk.co.dryhome.repository.CompanyRepository;
import uk.co.dryhome.repository.search.CompanySearchRepository;
import uk.co.dryhome.service.CompanyService;
import uk.co.dryhome.service.dto.CompanyDTO;
import uk.co.dryhome.service.mapper.CompanyMapper;
import uk.co.dryhome.web.rest.errors.ExceptionTranslator;
import uk.co.dryhome.service.dto.CompanyCriteria;
import uk.co.dryhome.service.CompanyQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


import static uk.co.dryhome.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dryhomecrm1App.class)
public class CompanyResourceIntTest {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_3 = "BBBBBBBBBB";

    private static final String DEFAULT_TOWN = "AAAAAAAAAA";
    private static final String UPDATED_TOWN = "BBBBBBBBBB";

    private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCTS = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTS = "BBBBBBBBBB";

    private static final String DEFAULT_INTERESTED = "AAAAAAAAAA";
    private static final String UPDATED_INTERESTED = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PAID = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAID = new BigDecimal(2);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    /**
     * This repository is mocked in the uk.co.dryhome.repository.search test package.
     *
     * @see uk.co.dryhome.repository.search.CompanySearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanySearchRepository mockCompanySearchRepository;

    @Autowired
    private CompanyQueryService companyQueryService;

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

    private MockMvc restCompanyMockMvc;

    private Company company;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyResource companyResource = new CompanyResource(companyService, companyQueryService);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
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
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .companyName(DEFAULT_COMPANY_NAME)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .address3(DEFAULT_ADDRESS_3)
            .town(DEFAULT_TOWN)
            .postCode(DEFAULT_POST_CODE)
            .title(DEFAULT_TITLE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .tel(DEFAULT_TEL)
            .mobile(DEFAULT_MOBILE)
            .email(DEFAULT_EMAIL)
            .products(DEFAULT_PRODUCTS)
            .interested(DEFAULT_INTERESTED)
            .paid(DEFAULT_PAID);
        return company;
    }

    @Before
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompany.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testCompany.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testCompany.getAddress3()).isEqualTo(DEFAULT_ADDRESS_3);
        assertThat(testCompany.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testCompany.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testCompany.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCompany.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCompany.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCompany.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCompany.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompany.getProducts()).isEqualTo(DEFAULT_PRODUCTS);
        assertThat(testCompany.getInterested()).isEqualTo(DEFAULT_INTERESTED);
        assertThat(testCompany.getPaid()).isEqualTo(DEFAULT_PAID);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }

    @Test
    @Transactional
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        company.setId(1L);
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }

    @Test
    @Transactional
    public void checkAddress1IsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setAddress1(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTownIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setTown(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setPostCode(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3.toString())))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].products").value(hasItem(DEFAULT_PRODUCTS.toString())))
            .andExpect(jsonPath("$.[*].interested").value(hasItem(DEFAULT_INTERESTED.toString())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())));
    }
    
    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.address3").value(DEFAULT_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.products").value(DEFAULT_PRODUCTS.toString()))
            .andExpect(jsonPath("$.interested").value(DEFAULT_INTERESTED.toString()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.intValue()));
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName equals to DEFAULT_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the companyList where companyName equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName is not null
        defaultCompanyShouldBeFound("companyName.specified=true");

        // Get all the companyList where companyName is null
        defaultCompanyShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress1IsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address1 equals to DEFAULT_ADDRESS_1
        defaultCompanyShouldBeFound("address1.equals=" + DEFAULT_ADDRESS_1);

        // Get all the companyList where address1 equals to UPDATED_ADDRESS_1
        defaultCompanyShouldNotBeFound("address1.equals=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress1IsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address1 in DEFAULT_ADDRESS_1 or UPDATED_ADDRESS_1
        defaultCompanyShouldBeFound("address1.in=" + DEFAULT_ADDRESS_1 + "," + UPDATED_ADDRESS_1);

        // Get all the companyList where address1 equals to UPDATED_ADDRESS_1
        defaultCompanyShouldNotBeFound("address1.in=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress1IsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address1 is not null
        defaultCompanyShouldBeFound("address1.specified=true");

        // Get all the companyList where address1 is null
        defaultCompanyShouldNotBeFound("address1.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress2IsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address2 equals to DEFAULT_ADDRESS_2
        defaultCompanyShouldBeFound("address2.equals=" + DEFAULT_ADDRESS_2);

        // Get all the companyList where address2 equals to UPDATED_ADDRESS_2
        defaultCompanyShouldNotBeFound("address2.equals=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress2IsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address2 in DEFAULT_ADDRESS_2 or UPDATED_ADDRESS_2
        defaultCompanyShouldBeFound("address2.in=" + DEFAULT_ADDRESS_2 + "," + UPDATED_ADDRESS_2);

        // Get all the companyList where address2 equals to UPDATED_ADDRESS_2
        defaultCompanyShouldNotBeFound("address2.in=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress2IsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address2 is not null
        defaultCompanyShouldBeFound("address2.specified=true");

        // Get all the companyList where address2 is null
        defaultCompanyShouldNotBeFound("address2.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress3IsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address3 equals to DEFAULT_ADDRESS_3
        defaultCompanyShouldBeFound("address3.equals=" + DEFAULT_ADDRESS_3);

        // Get all the companyList where address3 equals to UPDATED_ADDRESS_3
        defaultCompanyShouldNotBeFound("address3.equals=" + UPDATED_ADDRESS_3);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress3IsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address3 in DEFAULT_ADDRESS_3 or UPDATED_ADDRESS_3
        defaultCompanyShouldBeFound("address3.in=" + DEFAULT_ADDRESS_3 + "," + UPDATED_ADDRESS_3);

        // Get all the companyList where address3 equals to UPDATED_ADDRESS_3
        defaultCompanyShouldNotBeFound("address3.in=" + UPDATED_ADDRESS_3);
    }

    @Test
    @Transactional
    public void getAllCompaniesByAddress3IsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address3 is not null
        defaultCompanyShouldBeFound("address3.specified=true");

        // Get all the companyList where address3 is null
        defaultCompanyShouldNotBeFound("address3.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByTownIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where town equals to DEFAULT_TOWN
        defaultCompanyShouldBeFound("town.equals=" + DEFAULT_TOWN);

        // Get all the companyList where town equals to UPDATED_TOWN
        defaultCompanyShouldNotBeFound("town.equals=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTownIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where town in DEFAULT_TOWN or UPDATED_TOWN
        defaultCompanyShouldBeFound("town.in=" + DEFAULT_TOWN + "," + UPDATED_TOWN);

        // Get all the companyList where town equals to UPDATED_TOWN
        defaultCompanyShouldNotBeFound("town.in=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTownIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where town is not null
        defaultCompanyShouldBeFound("town.specified=true");

        // Get all the companyList where town is null
        defaultCompanyShouldNotBeFound("town.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByPostCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where postCode equals to DEFAULT_POST_CODE
        defaultCompanyShouldBeFound("postCode.equals=" + DEFAULT_POST_CODE);

        // Get all the companyList where postCode equals to UPDATED_POST_CODE
        defaultCompanyShouldNotBeFound("postCode.equals=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPostCodeIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where postCode in DEFAULT_POST_CODE or UPDATED_POST_CODE
        defaultCompanyShouldBeFound("postCode.in=" + DEFAULT_POST_CODE + "," + UPDATED_POST_CODE);

        // Get all the companyList where postCode equals to UPDATED_POST_CODE
        defaultCompanyShouldNotBeFound("postCode.in=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPostCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where postCode is not null
        defaultCompanyShouldBeFound("postCode.specified=true");

        // Get all the companyList where postCode is null
        defaultCompanyShouldNotBeFound("postCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title equals to DEFAULT_TITLE
        defaultCompanyShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the companyList where title equals to UPDATED_TITLE
        defaultCompanyShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCompanyShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the companyList where title equals to UPDATED_TITLE
        defaultCompanyShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title is not null
        defaultCompanyShouldBeFound("title.specified=true");

        // Get all the companyList where title is null
        defaultCompanyShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName equals to DEFAULT_FIRST_NAME
        defaultCompanyShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the companyList where firstName equals to UPDATED_FIRST_NAME
        defaultCompanyShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultCompanyShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the companyList where firstName equals to UPDATED_FIRST_NAME
        defaultCompanyShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName is not null
        defaultCompanyShouldBeFound("firstName.specified=true");

        // Get all the companyList where firstName is null
        defaultCompanyShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName equals to DEFAULT_LAST_NAME
        defaultCompanyShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the companyList where lastName equals to UPDATED_LAST_NAME
        defaultCompanyShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultCompanyShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the companyList where lastName equals to UPDATED_LAST_NAME
        defaultCompanyShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName is not null
        defaultCompanyShouldBeFound("lastName.specified=true");

        // Get all the companyList where lastName is null
        defaultCompanyShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel equals to DEFAULT_TEL
        defaultCompanyShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the companyList where tel equals to UPDATED_TEL
        defaultCompanyShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTelIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultCompanyShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the companyList where tel equals to UPDATED_TEL
        defaultCompanyShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel is not null
        defaultCompanyShouldBeFound("tel.specified=true");

        // Get all the companyList where tel is null
        defaultCompanyShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobile equals to DEFAULT_MOBILE
        defaultCompanyShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the companyList where mobile equals to UPDATED_MOBILE
        defaultCompanyShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultCompanyShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the companyList where mobile equals to UPDATED_MOBILE
        defaultCompanyShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobile is not null
        defaultCompanyShouldBeFound("mobile.specified=true");

        // Get all the companyList where mobile is null
        defaultCompanyShouldNotBeFound("mobile.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email equals to DEFAULT_EMAIL
        defaultCompanyShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email is not null
        defaultCompanyShouldBeFound("email.specified=true");

        // Get all the companyList where email is null
        defaultCompanyShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where products equals to DEFAULT_PRODUCTS
        defaultCompanyShouldBeFound("products.equals=" + DEFAULT_PRODUCTS);

        // Get all the companyList where products equals to UPDATED_PRODUCTS
        defaultCompanyShouldNotBeFound("products.equals=" + UPDATED_PRODUCTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByProductsIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where products in DEFAULT_PRODUCTS or UPDATED_PRODUCTS
        defaultCompanyShouldBeFound("products.in=" + DEFAULT_PRODUCTS + "," + UPDATED_PRODUCTS);

        // Get all the companyList where products equals to UPDATED_PRODUCTS
        defaultCompanyShouldNotBeFound("products.in=" + UPDATED_PRODUCTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByProductsIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where products is not null
        defaultCompanyShouldBeFound("products.specified=true");

        // Get all the companyList where products is null
        defaultCompanyShouldNotBeFound("products.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByInterestedIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where interested equals to DEFAULT_INTERESTED
        defaultCompanyShouldBeFound("interested.equals=" + DEFAULT_INTERESTED);

        // Get all the companyList where interested equals to UPDATED_INTERESTED
        defaultCompanyShouldNotBeFound("interested.equals=" + UPDATED_INTERESTED);
    }

    @Test
    @Transactional
    public void getAllCompaniesByInterestedIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where interested in DEFAULT_INTERESTED or UPDATED_INTERESTED
        defaultCompanyShouldBeFound("interested.in=" + DEFAULT_INTERESTED + "," + UPDATED_INTERESTED);

        // Get all the companyList where interested equals to UPDATED_INTERESTED
        defaultCompanyShouldNotBeFound("interested.in=" + UPDATED_INTERESTED);
    }

    @Test
    @Transactional
    public void getAllCompaniesByInterestedIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where interested is not null
        defaultCompanyShouldBeFound("interested.specified=true");

        // Get all the companyList where interested is null
        defaultCompanyShouldNotBeFound("interested.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where paid equals to DEFAULT_PAID
        defaultCompanyShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the companyList where paid equals to UPDATED_PAID
        defaultCompanyShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultCompanyShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the companyList where paid equals to UPDATED_PAID
        defaultCompanyShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllCompaniesByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where paid is not null
        defaultCompanyShouldBeFound("paid.specified=true");

        // Get all the companyList where paid is null
        defaultCompanyShouldNotBeFound("paid.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].products").value(hasItem(DEFAULT_PRODUCTS)))
            .andExpect(jsonPath("$.[*].interested").value(hasItem(DEFAULT_INTERESTED)))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())));

        // Check, that the count call also returns 1
        restCompanyMockMvc.perform(get("/api/companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc.perform(get("/api/companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .address3(UPDATED_ADDRESS_3)
            .town(UPDATED_TOWN)
            .postCode(UPDATED_POST_CODE)
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .tel(UPDATED_TEL)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .products(UPDATED_PRODUCTS)
            .interested(UPDATED_INTERESTED)
            .paid(UPDATED_PAID);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testCompany.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testCompany.getAddress3()).isEqualTo(UPDATED_ADDRESS_3);
        assertThat(testCompany.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testCompany.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testCompany.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCompany.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCompany.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCompany.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCompany.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompany.getProducts()).isEqualTo(UPDATED_PRODUCTS);
        assertThat(testCompany.getInterested()).isEqualTo(UPDATED_INTERESTED);
        assertThat(testCompany.getPaid()).isEqualTo(UPDATED_PAID);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }

    @Test
    @Transactional
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).deleteById(company.getId());
    }

    @Test
    @Transactional
    public void searchCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        when(mockCompanySearchRepository.search(queryStringQuery("id:" + company.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(company), PageRequest.of(0, 1), 1));
        // Search the company
        restCompanyMockMvc.perform(get("/api/_search/companies?query=id:" + company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].products").value(hasItem(DEFAULT_PRODUCTS)))
            .andExpect(jsonPath("$.[*].interested").value(hasItem(DEFAULT_INTERESTED)))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = new Company();
        company1.setId(1L);
        Company company2 = new Company();
        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);
        company2.setId(2L);
        assertThat(company1).isNotEqualTo(company2);
        company1.setId(null);
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyDTO.class);
        CompanyDTO companyDTO1 = new CompanyDTO();
        companyDTO1.setId(1L);
        CompanyDTO companyDTO2 = new CompanyDTO();
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO2.setId(companyDTO1.getId());
        assertThat(companyDTO1).isEqualTo(companyDTO2);
        companyDTO2.setId(2L);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO1.setId(null);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(companyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(companyMapper.fromId(null)).isNull();
    }
}

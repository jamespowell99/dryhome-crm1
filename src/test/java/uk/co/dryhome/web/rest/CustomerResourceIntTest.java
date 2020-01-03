package uk.co.dryhome.web.rest;

import uk.co.dryhome.Dryhomecrm1App;

import uk.co.dryhome.domain.Customer;
import uk.co.dryhome.domain.enumeration.InterestedType;
import uk.co.dryhome.repository.CustomerRepository;
import uk.co.dryhome.repository.search.CustomerSearchRepository;
import uk.co.dryhome.service.CustomerService;
import uk.co.dryhome.service.dto.CustomerDTO;
import uk.co.dryhome.service.mapper.CustomerMapper;
import uk.co.dryhome.web.rest.errors.ExceptionTranslator;
import uk.co.dryhome.service.dto.CustomerCriteria;
import uk.co.dryhome.service.CustomerQueryService;

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
import org.springframework.util.Base64Utils;
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

import uk.co.dryhome.domain.enumeration.CompanyType;
import uk.co.dryhome.domain.enumeration.LeadType;
import uk.co.dryhome.domain.enumeration.Status;
/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dryhomecrm1App.class)
public class CustomerResourceIntTest {

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

    private static final InterestedType DEFAULT_INTERESTED = InterestedType.DNI;
    private static final InterestedType UPDATED_INTERESTED = InterestedType.INT;

    private static final BigDecimal DEFAULT_PAID = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAID = new BigDecimal(2);

    private static final CompanyType DEFAULT_TYPE = CompanyType.DAMP_PROOFER;
    private static final CompanyType UPDATED_TYPE = CompanyType.DOMESTIC;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final LeadType DEFAULT_LEAD = LeadType.WEBSITE;
    private static final LeadType UPDATED_LEAD = LeadType.DAMP_PROOFER;

    private static final String DEFAULT_LEAD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEAD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LEAD_TEL = "AAAAAAAAAA";
    private static final String UPDATED_LEAD_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_LEAD_MOB = "AAAAAAAAAA";
    private static final String UPDATED_LEAD_MOB = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ENQUIRY;
    private static final Status UPDATED_STATUS = Status.SALE;

    private static final String DEFAULT_ENQUIRY_PROPERTY = "AAAAAAAAAA";
    private static final String UPDATED_ENQUIRY_PROPERTY = "BBBBBBBBBB";

    private static final String DEFAULT_ENQUIRY_UNIT_PQ = "AAAAAAAAAA";
    private static final String UPDATED_ENQUIRY_UNIT_PQ = "BBBBBBBBBB";

    private static final String DEFAULT_ENQUIRY_INST_PQ = "AAAAAAAAAA";
    private static final String UPDATED_ENQUIRY_INST_PQ = "BBBBBBBBBB";

    private static final String DEFAULT_SALE_PRODUCTS = "AAAAAAAAAA";
    private static final String UPDATED_SALE_PRODUCTS = "BBBBBBBBBB";

    private static final String DEFAULT_SALE_INVOICE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_SALE_INVOICE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_SALE_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SALE_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SALE_INVOICE_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_SALE_INVOICE_AMOUNT = "BBBBBBBBBB";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    /**
     * This repository is mocked in the uk.co.dryhome.repository.search test package.
     *
     * @see uk.co.dryhome.repository.search.CustomerSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerSearchRepository mockCustomerSearchRepository;

    @Autowired
    private CustomerQueryService customerQueryService;

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

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerResource customerResource = new CustomerResource(customerService, customerQueryService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
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
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
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
            .paid(DEFAULT_PAID)
            .type(DEFAULT_TYPE)
            .notes(DEFAULT_NOTES)
            .lead(DEFAULT_LEAD)
            .leadName(DEFAULT_LEAD_NAME)
            .leadTel(DEFAULT_LEAD_TEL)
            .leadMob(DEFAULT_LEAD_MOB)
            .status(DEFAULT_STATUS)
            .enquiryProperty(DEFAULT_ENQUIRY_PROPERTY)
            .enquiryUnitPq(DEFAULT_ENQUIRY_UNIT_PQ)
            .enquiryInstPq(DEFAULT_ENQUIRY_INST_PQ)
            .saleProducts(DEFAULT_SALE_PRODUCTS)
            .saleInvoiceDate(DEFAULT_SALE_INVOICE_DATE)
            .saleInvoiceNumber(DEFAULT_SALE_INVOICE_NUMBER)
            .saleInvoiceAmount(DEFAULT_SALE_INVOICE_AMOUNT);
        return customer;
    }

    @Before
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCustomer.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testCustomer.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testCustomer.getAddress3()).isEqualTo(DEFAULT_ADDRESS_3);
        assertThat(testCustomer.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testCustomer.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testCustomer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCustomer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomer.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCustomer.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomer.getProducts()).isEqualTo(DEFAULT_PRODUCTS);
        assertThat(testCustomer.getInterested()).isEqualTo(DEFAULT_INTERESTED);
        assertThat(testCustomer.getPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testCustomer.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCustomer.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCustomer.getLead()).isEqualTo(DEFAULT_LEAD);
        assertThat(testCustomer.getLeadName()).isEqualTo(DEFAULT_LEAD_NAME);
        assertThat(testCustomer.getLeadTel()).isEqualTo(DEFAULT_LEAD_TEL);
        assertThat(testCustomer.getLeadMob()).isEqualTo(DEFAULT_LEAD_MOB);
        assertThat(testCustomer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCustomer.getEnquiryProperty()).isEqualTo(DEFAULT_ENQUIRY_PROPERTY);
        assertThat(testCustomer.getEnquiryUnitPq()).isEqualTo(DEFAULT_ENQUIRY_UNIT_PQ);
        assertThat(testCustomer.getEnquiryInstPq()).isEqualTo(DEFAULT_ENQUIRY_INST_PQ);
        assertThat(testCustomer.getSaleProducts()).isEqualTo(DEFAULT_SALE_PRODUCTS);
        assertThat(testCustomer.getSaleInvoiceDate()).isEqualTo(DEFAULT_SALE_INVOICE_DATE);
        assertThat(testCustomer.getSaleInvoiceNumber()).isEqualTo(DEFAULT_SALE_INVOICE_NUMBER);
        assertThat(testCustomer.getSaleInvoiceAmount()).isEqualTo(DEFAULT_SALE_INVOICE_AMOUNT);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).save(testCustomer);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(0)).save(customer);
    }

    @Test
    @Transactional
    public void checkAddress1IsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setAddress1(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTownIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setTown(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setPostCode(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setType(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].lead").value(hasItem(DEFAULT_LEAD.toString())))
            .andExpect(jsonPath("$.[*].leadName").value(hasItem(DEFAULT_LEAD_NAME.toString())))
            .andExpect(jsonPath("$.[*].leadTel").value(hasItem(DEFAULT_LEAD_TEL.toString())))
            .andExpect(jsonPath("$.[*].leadMob").value(hasItem(DEFAULT_LEAD_MOB.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].enquiryProperty").value(hasItem(DEFAULT_ENQUIRY_PROPERTY.toString())))
            .andExpect(jsonPath("$.[*].enquiryUnitPq").value(hasItem(DEFAULT_ENQUIRY_UNIT_PQ.toString())))
            .andExpect(jsonPath("$.[*].enquiryInstPq").value(hasItem(DEFAULT_ENQUIRY_INST_PQ.toString())))
            .andExpect(jsonPath("$.[*].saleProducts").value(hasItem(DEFAULT_SALE_PRODUCTS.toString())))
            .andExpect(jsonPath("$.[*].saleInvoiceDate").value(hasItem(DEFAULT_SALE_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].saleInvoiceNumber").value(hasItem(DEFAULT_SALE_INVOICE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].saleInvoiceAmount").value(hasItem(DEFAULT_SALE_INVOICE_AMOUNT.toString())));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
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
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.lead").value(DEFAULT_LEAD.toString()))
            .andExpect(jsonPath("$.leadName").value(DEFAULT_LEAD_NAME.toString()))
            .andExpect(jsonPath("$.leadTel").value(DEFAULT_LEAD_TEL.toString()))
            .andExpect(jsonPath("$.leadMob").value(DEFAULT_LEAD_MOB.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.enquiryProperty").value(DEFAULT_ENQUIRY_PROPERTY.toString()))
            .andExpect(jsonPath("$.enquiryUnitPq").value(DEFAULT_ENQUIRY_UNIT_PQ.toString()))
            .andExpect(jsonPath("$.enquiryInstPq").value(DEFAULT_ENQUIRY_INST_PQ.toString()))
            .andExpect(jsonPath("$.saleProducts").value(DEFAULT_SALE_PRODUCTS.toString()))
            .andExpect(jsonPath("$.saleInvoiceDate").value(DEFAULT_SALE_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.saleInvoiceNumber").value(DEFAULT_SALE_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.saleInvoiceAmount").value(DEFAULT_SALE_INVOICE_AMOUNT.toString()));
    }

    @Test
    @Transactional
    public void getAllCustomersByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyName equals to DEFAULT_COMPANY_NAME
        defaultCustomerShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the customerList where companyName equals to UPDATED_COMPANY_NAME
        defaultCustomerShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultCustomerShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the customerList where companyName equals to UPDATED_COMPANY_NAME
        defaultCustomerShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where companyName is not null
        defaultCustomerShouldBeFound("companyName.specified=true");

        // Get all the customerList where companyName is null
        defaultCustomerShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress1IsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address1 equals to DEFAULT_ADDRESS_1
        defaultCustomerShouldBeFound("address1.equals=" + DEFAULT_ADDRESS_1);

        // Get all the customerList where address1 equals to UPDATED_ADDRESS_1
        defaultCustomerShouldNotBeFound("address1.equals=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress1IsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address1 in DEFAULT_ADDRESS_1 or UPDATED_ADDRESS_1
        defaultCustomerShouldBeFound("address1.in=" + DEFAULT_ADDRESS_1 + "," + UPDATED_ADDRESS_1);

        // Get all the customerList where address1 equals to UPDATED_ADDRESS_1
        defaultCustomerShouldNotBeFound("address1.in=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress1IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address1 is not null
        defaultCustomerShouldBeFound("address1.specified=true");

        // Get all the customerList where address1 is null
        defaultCustomerShouldNotBeFound("address1.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress2IsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address2 equals to DEFAULT_ADDRESS_2
        defaultCustomerShouldBeFound("address2.equals=" + DEFAULT_ADDRESS_2);

        // Get all the customerList where address2 equals to UPDATED_ADDRESS_2
        defaultCustomerShouldNotBeFound("address2.equals=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress2IsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address2 in DEFAULT_ADDRESS_2 or UPDATED_ADDRESS_2
        defaultCustomerShouldBeFound("address2.in=" + DEFAULT_ADDRESS_2 + "," + UPDATED_ADDRESS_2);

        // Get all the customerList where address2 equals to UPDATED_ADDRESS_2
        defaultCustomerShouldNotBeFound("address2.in=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress2IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address2 is not null
        defaultCustomerShouldBeFound("address2.specified=true");

        // Get all the customerList where address2 is null
        defaultCustomerShouldNotBeFound("address2.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress3IsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address3 equals to DEFAULT_ADDRESS_3
        defaultCustomerShouldBeFound("address3.equals=" + DEFAULT_ADDRESS_3);

        // Get all the customerList where address3 equals to UPDATED_ADDRESS_3
        defaultCustomerShouldNotBeFound("address3.equals=" + UPDATED_ADDRESS_3);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress3IsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address3 in DEFAULT_ADDRESS_3 or UPDATED_ADDRESS_3
        defaultCustomerShouldBeFound("address3.in=" + DEFAULT_ADDRESS_3 + "," + UPDATED_ADDRESS_3);

        // Get all the customerList where address3 equals to UPDATED_ADDRESS_3
        defaultCustomerShouldNotBeFound("address3.in=" + UPDATED_ADDRESS_3);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddress3IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address3 is not null
        defaultCustomerShouldBeFound("address3.specified=true");

        // Get all the customerList where address3 is null
        defaultCustomerShouldNotBeFound("address3.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByTownIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where town equals to DEFAULT_TOWN
        defaultCustomerShouldBeFound("town.equals=" + DEFAULT_TOWN);

        // Get all the customerList where town equals to UPDATED_TOWN
        defaultCustomerShouldNotBeFound("town.equals=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllCustomersByTownIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where town in DEFAULT_TOWN or UPDATED_TOWN
        defaultCustomerShouldBeFound("town.in=" + DEFAULT_TOWN + "," + UPDATED_TOWN);

        // Get all the customerList where town equals to UPDATED_TOWN
        defaultCustomerShouldNotBeFound("town.in=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllCustomersByTownIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where town is not null
        defaultCustomerShouldBeFound("town.specified=true");

        // Get all the customerList where town is null
        defaultCustomerShouldNotBeFound("town.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPostCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where postCode equals to DEFAULT_POST_CODE
        defaultCustomerShouldBeFound("postCode.equals=" + DEFAULT_POST_CODE);

        // Get all the customerList where postCode equals to UPDATED_POST_CODE
        defaultCustomerShouldNotBeFound("postCode.equals=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPostCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where postCode in DEFAULT_POST_CODE or UPDATED_POST_CODE
        defaultCustomerShouldBeFound("postCode.in=" + DEFAULT_POST_CODE + "," + UPDATED_POST_CODE);

        // Get all the customerList where postCode equals to UPDATED_POST_CODE
        defaultCustomerShouldNotBeFound("postCode.in=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPostCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where postCode is not null
        defaultCustomerShouldBeFound("postCode.specified=true");

        // Get all the customerList where postCode is null
        defaultCustomerShouldNotBeFound("postCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title equals to DEFAULT_TITLE
        defaultCustomerShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the customerList where title equals to UPDATED_TITLE
        defaultCustomerShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCustomersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCustomerShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the customerList where title equals to UPDATED_TITLE
        defaultCustomerShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCustomersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title is not null
        defaultCustomerShouldBeFound("title.specified=true");

        // Get all the customerList where title is null
        defaultCustomerShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName equals to DEFAULT_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the customerList where firstName equals to UPDATED_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the customerList where firstName equals to UPDATED_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName is not null
        defaultCustomerShouldBeFound("firstName.specified=true");

        // Get all the customerList where firstName is null
        defaultCustomerShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName equals to DEFAULT_LAST_NAME
        defaultCustomerShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the customerList where lastName equals to UPDATED_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultCustomerShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the customerList where lastName equals to UPDATED_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName is not null
        defaultCustomerShouldBeFound("lastName.specified=true");

        // Get all the customerList where lastName is null
        defaultCustomerShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where tel equals to DEFAULT_TEL
        defaultCustomerShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the customerList where tel equals to UPDATED_TEL
        defaultCustomerShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    public void getAllCustomersByTelIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultCustomerShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the customerList where tel equals to UPDATED_TEL
        defaultCustomerShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    public void getAllCustomersByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where tel is not null
        defaultCustomerShouldBeFound("tel.specified=true");

        // Get all the customerList where tel is null
        defaultCustomerShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobile equals to DEFAULT_MOBILE
        defaultCustomerShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the customerList where mobile equals to UPDATED_MOBILE
        defaultCustomerShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllCustomersByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultCustomerShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the customerList where mobile equals to UPDATED_MOBILE
        defaultCustomerShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllCustomersByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobile is not null
        defaultCustomerShouldBeFound("mobile.specified=true");

        // Get all the customerList where mobile is null
        defaultCustomerShouldNotBeFound("mobile.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email equals to DEFAULT_EMAIL
        defaultCustomerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email is not null
        defaultCustomerShouldBeFound("email.specified=true");

        // Get all the customerList where email is null
        defaultCustomerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where products equals to DEFAULT_PRODUCTS
        defaultCustomerShouldBeFound("products.equals=" + DEFAULT_PRODUCTS);

        // Get all the customerList where products equals to UPDATED_PRODUCTS
        defaultCustomerShouldNotBeFound("products.equals=" + UPDATED_PRODUCTS);
    }

    @Test
    @Transactional
    public void getAllCustomersByProductsIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where products in DEFAULT_PRODUCTS or UPDATED_PRODUCTS
        defaultCustomerShouldBeFound("products.in=" + DEFAULT_PRODUCTS + "," + UPDATED_PRODUCTS);

        // Get all the customerList where products equals to UPDATED_PRODUCTS
        defaultCustomerShouldNotBeFound("products.in=" + UPDATED_PRODUCTS);
    }

    @Test
    @Transactional
    public void getAllCustomersByProductsIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where products is not null
        defaultCustomerShouldBeFound("products.specified=true");

        // Get all the customerList where products is null
        defaultCustomerShouldNotBeFound("products.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByInterestedIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where interested equals to DEFAULT_INTERESTED
        defaultCustomerShouldBeFound("interested.equals=" + DEFAULT_INTERESTED);

        // Get all the customerList where interested equals to UPDATED_INTERESTED
        defaultCustomerShouldNotBeFound("interested.equals=" + UPDATED_INTERESTED);
    }

    @Test
    @Transactional
    public void getAllCustomersByInterestedIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where interested in DEFAULT_INTERESTED or UPDATED_INTERESTED
        defaultCustomerShouldBeFound("interested.in=" + DEFAULT_INTERESTED + "," + UPDATED_INTERESTED);

        // Get all the customerList where interested equals to UPDATED_INTERESTED
        defaultCustomerShouldNotBeFound("interested.in=" + UPDATED_INTERESTED);
    }

    @Test
    @Transactional
    public void getAllCustomersByInterestedIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where interested is not null
        defaultCustomerShouldBeFound("interested.specified=true");

        // Get all the customerList where interested is null
        defaultCustomerShouldNotBeFound("interested.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paid equals to DEFAULT_PAID
        defaultCustomerShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the customerList where paid equals to UPDATED_PAID
        defaultCustomerShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllCustomersByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultCustomerShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the customerList where paid equals to UPDATED_PAID
        defaultCustomerShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllCustomersByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where paid is not null
        defaultCustomerShouldBeFound("paid.specified=true");

        // Get all the customerList where paid is null
        defaultCustomerShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where type equals to DEFAULT_TYPE
        defaultCustomerShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the customerList where type equals to UPDATED_TYPE
        defaultCustomerShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCustomersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCustomerShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the customerList where type equals to UPDATED_TYPE
        defaultCustomerShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCustomersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where type is not null
        defaultCustomerShouldBeFound("type.specified=true");

        // Get all the customerList where type is null
        defaultCustomerShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lead equals to DEFAULT_LEAD
        defaultCustomerShouldBeFound("lead.equals=" + DEFAULT_LEAD);

        // Get all the customerList where lead equals to UPDATED_LEAD
        defaultCustomerShouldNotBeFound("lead.equals=" + UPDATED_LEAD);
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lead in DEFAULT_LEAD or UPDATED_LEAD
        defaultCustomerShouldBeFound("lead.in=" + DEFAULT_LEAD + "," + UPDATED_LEAD);

        // Get all the customerList where lead equals to UPDATED_LEAD
        defaultCustomerShouldNotBeFound("lead.in=" + UPDATED_LEAD);
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lead is not null
        defaultCustomerShouldBeFound("lead.specified=true");

        // Get all the customerList where lead is null
        defaultCustomerShouldNotBeFound("lead.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadName equals to DEFAULT_LEAD_NAME
        defaultCustomerShouldBeFound("leadName.equals=" + DEFAULT_LEAD_NAME);

        // Get all the customerList where leadName equals to UPDATED_LEAD_NAME
        defaultCustomerShouldNotBeFound("leadName.equals=" + UPDATED_LEAD_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadName in DEFAULT_LEAD_NAME or UPDATED_LEAD_NAME
        defaultCustomerShouldBeFound("leadName.in=" + DEFAULT_LEAD_NAME + "," + UPDATED_LEAD_NAME);

        // Get all the customerList where leadName equals to UPDATED_LEAD_NAME
        defaultCustomerShouldNotBeFound("leadName.in=" + UPDATED_LEAD_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadName is not null
        defaultCustomerShouldBeFound("leadName.specified=true");

        // Get all the customerList where leadName is null
        defaultCustomerShouldNotBeFound("leadName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadTelIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadTel equals to DEFAULT_LEAD_TEL
        defaultCustomerShouldBeFound("leadTel.equals=" + DEFAULT_LEAD_TEL);

        // Get all the customerList where leadTel equals to UPDATED_LEAD_TEL
        defaultCustomerShouldNotBeFound("leadTel.equals=" + UPDATED_LEAD_TEL);
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadTelIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadTel in DEFAULT_LEAD_TEL or UPDATED_LEAD_TEL
        defaultCustomerShouldBeFound("leadTel.in=" + DEFAULT_LEAD_TEL + "," + UPDATED_LEAD_TEL);

        // Get all the customerList where leadTel equals to UPDATED_LEAD_TEL
        defaultCustomerShouldNotBeFound("leadTel.in=" + UPDATED_LEAD_TEL);
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadTel is not null
        defaultCustomerShouldBeFound("leadTel.specified=true");

        // Get all the customerList where leadTel is null
        defaultCustomerShouldNotBeFound("leadTel.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadMobIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadMob equals to DEFAULT_LEAD_MOB
        defaultCustomerShouldBeFound("leadMob.equals=" + DEFAULT_LEAD_MOB);

        // Get all the customerList where leadMob equals to UPDATED_LEAD_MOB
        defaultCustomerShouldNotBeFound("leadMob.equals=" + UPDATED_LEAD_MOB);
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadMobIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadMob in DEFAULT_LEAD_MOB or UPDATED_LEAD_MOB
        defaultCustomerShouldBeFound("leadMob.in=" + DEFAULT_LEAD_MOB + "," + UPDATED_LEAD_MOB);

        // Get all the customerList where leadMob equals to UPDATED_LEAD_MOB
        defaultCustomerShouldNotBeFound("leadMob.in=" + UPDATED_LEAD_MOB);
    }

    @Test
    @Transactional
    public void getAllCustomersByLeadMobIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where leadMob is not null
        defaultCustomerShouldBeFound("leadMob.specified=true");

        // Get all the customerList where leadMob is null
        defaultCustomerShouldNotBeFound("leadMob.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where status equals to DEFAULT_STATUS
        defaultCustomerShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the customerList where status equals to UPDATED_STATUS
        defaultCustomerShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCustomersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultCustomerShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the customerList where status equals to UPDATED_STATUS
        defaultCustomerShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCustomersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where status is not null
        defaultCustomerShouldBeFound("status.specified=true");

        // Get all the customerList where status is null
        defaultCustomerShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryPropertyIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryProperty equals to DEFAULT_ENQUIRY_PROPERTY
        defaultCustomerShouldBeFound("enquiryProperty.equals=" + DEFAULT_ENQUIRY_PROPERTY);

        // Get all the customerList where enquiryProperty equals to UPDATED_ENQUIRY_PROPERTY
        defaultCustomerShouldNotBeFound("enquiryProperty.equals=" + UPDATED_ENQUIRY_PROPERTY);
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryPropertyIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryProperty in DEFAULT_ENQUIRY_PROPERTY or UPDATED_ENQUIRY_PROPERTY
        defaultCustomerShouldBeFound("enquiryProperty.in=" + DEFAULT_ENQUIRY_PROPERTY + "," + UPDATED_ENQUIRY_PROPERTY);

        // Get all the customerList where enquiryProperty equals to UPDATED_ENQUIRY_PROPERTY
        defaultCustomerShouldNotBeFound("enquiryProperty.in=" + UPDATED_ENQUIRY_PROPERTY);
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryPropertyIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryProperty is not null
        defaultCustomerShouldBeFound("enquiryProperty.specified=true");

        // Get all the customerList where enquiryProperty is null
        defaultCustomerShouldNotBeFound("enquiryProperty.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryUnitPqIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryUnitPq equals to DEFAULT_ENQUIRY_UNIT_PQ
        defaultCustomerShouldBeFound("enquiryUnitPq.equals=" + DEFAULT_ENQUIRY_UNIT_PQ);

        // Get all the customerList where enquiryUnitPq equals to UPDATED_ENQUIRY_UNIT_PQ
        defaultCustomerShouldNotBeFound("enquiryUnitPq.equals=" + UPDATED_ENQUIRY_UNIT_PQ);
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryUnitPqIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryUnitPq in DEFAULT_ENQUIRY_UNIT_PQ or UPDATED_ENQUIRY_UNIT_PQ
        defaultCustomerShouldBeFound("enquiryUnitPq.in=" + DEFAULT_ENQUIRY_UNIT_PQ + "," + UPDATED_ENQUIRY_UNIT_PQ);

        // Get all the customerList where enquiryUnitPq equals to UPDATED_ENQUIRY_UNIT_PQ
        defaultCustomerShouldNotBeFound("enquiryUnitPq.in=" + UPDATED_ENQUIRY_UNIT_PQ);
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryUnitPqIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryUnitPq is not null
        defaultCustomerShouldBeFound("enquiryUnitPq.specified=true");

        // Get all the customerList where enquiryUnitPq is null
        defaultCustomerShouldNotBeFound("enquiryUnitPq.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryInstPqIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryInstPq equals to DEFAULT_ENQUIRY_INST_PQ
        defaultCustomerShouldBeFound("enquiryInstPq.equals=" + DEFAULT_ENQUIRY_INST_PQ);

        // Get all the customerList where enquiryInstPq equals to UPDATED_ENQUIRY_INST_PQ
        defaultCustomerShouldNotBeFound("enquiryInstPq.equals=" + UPDATED_ENQUIRY_INST_PQ);
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryInstPqIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryInstPq in DEFAULT_ENQUIRY_INST_PQ or UPDATED_ENQUIRY_INST_PQ
        defaultCustomerShouldBeFound("enquiryInstPq.in=" + DEFAULT_ENQUIRY_INST_PQ + "," + UPDATED_ENQUIRY_INST_PQ);

        // Get all the customerList where enquiryInstPq equals to UPDATED_ENQUIRY_INST_PQ
        defaultCustomerShouldNotBeFound("enquiryInstPq.in=" + UPDATED_ENQUIRY_INST_PQ);
    }

    @Test
    @Transactional
    public void getAllCustomersByEnquiryInstPqIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where enquiryInstPq is not null
        defaultCustomerShouldBeFound("enquiryInstPq.specified=true");

        // Get all the customerList where enquiryInstPq is null
        defaultCustomerShouldNotBeFound("enquiryInstPq.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleProducts equals to DEFAULT_SALE_PRODUCTS
        defaultCustomerShouldBeFound("saleProducts.equals=" + DEFAULT_SALE_PRODUCTS);

        // Get all the customerList where saleProducts equals to UPDATED_SALE_PRODUCTS
        defaultCustomerShouldNotBeFound("saleProducts.equals=" + UPDATED_SALE_PRODUCTS);
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleProductsIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleProducts in DEFAULT_SALE_PRODUCTS or UPDATED_SALE_PRODUCTS
        defaultCustomerShouldBeFound("saleProducts.in=" + DEFAULT_SALE_PRODUCTS + "," + UPDATED_SALE_PRODUCTS);

        // Get all the customerList where saleProducts equals to UPDATED_SALE_PRODUCTS
        defaultCustomerShouldNotBeFound("saleProducts.in=" + UPDATED_SALE_PRODUCTS);
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleProductsIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleProducts is not null
        defaultCustomerShouldBeFound("saleProducts.specified=true");

        // Get all the customerList where saleProducts is null
        defaultCustomerShouldNotBeFound("saleProducts.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceDate equals to DEFAULT_SALE_INVOICE_DATE
        defaultCustomerShouldBeFound("saleInvoiceDate.equals=" + DEFAULT_SALE_INVOICE_DATE);

        // Get all the customerList where saleInvoiceDate equals to UPDATED_SALE_INVOICE_DATE
        defaultCustomerShouldNotBeFound("saleInvoiceDate.equals=" + UPDATED_SALE_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceDate in DEFAULT_SALE_INVOICE_DATE or UPDATED_SALE_INVOICE_DATE
        defaultCustomerShouldBeFound("saleInvoiceDate.in=" + DEFAULT_SALE_INVOICE_DATE + "," + UPDATED_SALE_INVOICE_DATE);

        // Get all the customerList where saleInvoiceDate equals to UPDATED_SALE_INVOICE_DATE
        defaultCustomerShouldNotBeFound("saleInvoiceDate.in=" + UPDATED_SALE_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceDate is not null
        defaultCustomerShouldBeFound("saleInvoiceDate.specified=true");

        // Get all the customerList where saleInvoiceDate is null
        defaultCustomerShouldNotBeFound("saleInvoiceDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceNumber equals to DEFAULT_SALE_INVOICE_NUMBER
        defaultCustomerShouldBeFound("saleInvoiceNumber.equals=" + DEFAULT_SALE_INVOICE_NUMBER);

        // Get all the customerList where saleInvoiceNumber equals to UPDATED_SALE_INVOICE_NUMBER
        defaultCustomerShouldNotBeFound("saleInvoiceNumber.equals=" + UPDATED_SALE_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceNumber in DEFAULT_SALE_INVOICE_NUMBER or UPDATED_SALE_INVOICE_NUMBER
        defaultCustomerShouldBeFound("saleInvoiceNumber.in=" + DEFAULT_SALE_INVOICE_NUMBER + "," + UPDATED_SALE_INVOICE_NUMBER);

        // Get all the customerList where saleInvoiceNumber equals to UPDATED_SALE_INVOICE_NUMBER
        defaultCustomerShouldNotBeFound("saleInvoiceNumber.in=" + UPDATED_SALE_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceNumber is not null
        defaultCustomerShouldBeFound("saleInvoiceNumber.specified=true");

        // Get all the customerList where saleInvoiceNumber is null
        defaultCustomerShouldNotBeFound("saleInvoiceNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceAmount equals to DEFAULT_SALE_INVOICE_AMOUNT
        defaultCustomerShouldBeFound("saleInvoiceAmount.equals=" + DEFAULT_SALE_INVOICE_AMOUNT);

        // Get all the customerList where saleInvoiceAmount equals to UPDATED_SALE_INVOICE_AMOUNT
        defaultCustomerShouldNotBeFound("saleInvoiceAmount.equals=" + UPDATED_SALE_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceAmountIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceAmount in DEFAULT_SALE_INVOICE_AMOUNT or UPDATED_SALE_INVOICE_AMOUNT
        defaultCustomerShouldBeFound("saleInvoiceAmount.in=" + DEFAULT_SALE_INVOICE_AMOUNT + "," + UPDATED_SALE_INVOICE_AMOUNT);

        // Get all the customerList where saleInvoiceAmount equals to UPDATED_SALE_INVOICE_AMOUNT
        defaultCustomerShouldNotBeFound("saleInvoiceAmount.in=" + UPDATED_SALE_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCustomersBySaleInvoiceAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where saleInvoiceAmount is not null
        defaultCustomerShouldBeFound("saleInvoiceAmount.specified=true");

        // Get all the customerList where saleInvoiceAmount is null
        defaultCustomerShouldNotBeFound("saleInvoiceAmount.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].lead").value(hasItem(DEFAULT_LEAD.toString())))
            .andExpect(jsonPath("$.[*].leadName").value(hasItem(DEFAULT_LEAD_NAME)))
            .andExpect(jsonPath("$.[*].leadTel").value(hasItem(DEFAULT_LEAD_TEL)))
            .andExpect(jsonPath("$.[*].leadMob").value(hasItem(DEFAULT_LEAD_MOB)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].enquiryProperty").value(hasItem(DEFAULT_ENQUIRY_PROPERTY)))
            .andExpect(jsonPath("$.[*].enquiryUnitPq").value(hasItem(DEFAULT_ENQUIRY_UNIT_PQ)))
            .andExpect(jsonPath("$.[*].enquiryInstPq").value(hasItem(DEFAULT_ENQUIRY_INST_PQ)))
            .andExpect(jsonPath("$.[*].saleProducts").value(hasItem(DEFAULT_SALE_PRODUCTS)))
            .andExpect(jsonPath("$.[*].saleInvoiceDate").value(hasItem(DEFAULT_SALE_INVOICE_DATE)))
            .andExpect(jsonPath("$.[*].saleInvoiceNumber").value(hasItem(DEFAULT_SALE_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].saleInvoiceAmount").value(hasItem(DEFAULT_SALE_INVOICE_AMOUNT)));

        // Check, that the count call also returns 1
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
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
            .paid(UPDATED_PAID)
            .type(UPDATED_TYPE)
            .notes(UPDATED_NOTES)
            .lead(UPDATED_LEAD)
            .leadName(UPDATED_LEAD_NAME)
            .leadTel(UPDATED_LEAD_TEL)
            .leadMob(UPDATED_LEAD_MOB)
            .status(UPDATED_STATUS)
            .enquiryProperty(UPDATED_ENQUIRY_PROPERTY)
            .enquiryUnitPq(UPDATED_ENQUIRY_UNIT_PQ)
            .enquiryInstPq(UPDATED_ENQUIRY_INST_PQ)
            .saleProducts(UPDATED_SALE_PRODUCTS)
            .saleInvoiceDate(UPDATED_SALE_INVOICE_DATE)
            .saleInvoiceNumber(UPDATED_SALE_INVOICE_NUMBER)
            .saleInvoiceAmount(UPDATED_SALE_INVOICE_AMOUNT);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCustomer.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testCustomer.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testCustomer.getAddress3()).isEqualTo(UPDATED_ADDRESS_3);
        assertThat(testCustomer.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testCustomer.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testCustomer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCustomer.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomer.getProducts()).isEqualTo(UPDATED_PRODUCTS);
        assertThat(testCustomer.getInterested()).isEqualTo(UPDATED_INTERESTED);
        assertThat(testCustomer.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testCustomer.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCustomer.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCustomer.getLead()).isEqualTo(UPDATED_LEAD);
        assertThat(testCustomer.getLeadName()).isEqualTo(UPDATED_LEAD_NAME);
        assertThat(testCustomer.getLeadTel()).isEqualTo(UPDATED_LEAD_TEL);
        assertThat(testCustomer.getLeadMob()).isEqualTo(UPDATED_LEAD_MOB);
        assertThat(testCustomer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCustomer.getEnquiryProperty()).isEqualTo(UPDATED_ENQUIRY_PROPERTY);
        assertThat(testCustomer.getEnquiryUnitPq()).isEqualTo(UPDATED_ENQUIRY_UNIT_PQ);
        assertThat(testCustomer.getEnquiryInstPq()).isEqualTo(UPDATED_ENQUIRY_INST_PQ);
        assertThat(testCustomer.getSaleProducts()).isEqualTo(UPDATED_SALE_PRODUCTS);
        assertThat(testCustomer.getSaleInvoiceDate()).isEqualTo(UPDATED_SALE_INVOICE_DATE);
        assertThat(testCustomer.getSaleInvoiceNumber()).isEqualTo(UPDATED_SALE_INVOICE_NUMBER);
        assertThat(testCustomer.getSaleInvoiceAmount()).isEqualTo(UPDATED_SALE_INVOICE_AMOUNT);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).save(testCustomer);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(0)).save(customer);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).deleteById(customer.getId());
    }

    @Test
    @Transactional
    public void searchCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        when(mockCustomerSearchRepository.search(queryStringQuery("id:" + customer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customer), PageRequest.of(0, 1), 1));
        // Search the customer
        restCustomerMockMvc.perform(get("/api/_search/customers?query=id:" + customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].lead").value(hasItem(DEFAULT_LEAD.toString())))
            .andExpect(jsonPath("$.[*].leadName").value(hasItem(DEFAULT_LEAD_NAME)))
            .andExpect(jsonPath("$.[*].leadTel").value(hasItem(DEFAULT_LEAD_TEL)))
            .andExpect(jsonPath("$.[*].leadMob").value(hasItem(DEFAULT_LEAD_MOB)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].enquiryProperty").value(hasItem(DEFAULT_ENQUIRY_PROPERTY)))
            .andExpect(jsonPath("$.[*].enquiryUnitPq").value(hasItem(DEFAULT_ENQUIRY_UNIT_PQ)))
            .andExpect(jsonPath("$.[*].enquiryInstPq").value(hasItem(DEFAULT_ENQUIRY_INST_PQ)))
            .andExpect(jsonPath("$.[*].saleProducts").value(hasItem(DEFAULT_SALE_PRODUCTS)))
            .andExpect(jsonPath("$.[*].saleInvoiceDate").value(hasItem(DEFAULT_SALE_INVOICE_DATE)))
            .andExpect(jsonPath("$.[*].saleInvoiceNumber").value(hasItem(DEFAULT_SALE_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].saleInvoiceAmount").value(hasItem(DEFAULT_SALE_INVOICE_AMOUNT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerDTO.class);
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId(1L);
        CustomerDTO customerDTO2 = new CustomerDTO();
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
        customerDTO2.setId(customerDTO1.getId());
        assertThat(customerDTO1).isEqualTo(customerDTO2);
        customerDTO2.setId(2L);
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
        customerDTO1.setId(null);
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customerMapper.fromId(null)).isNull();
    }
}

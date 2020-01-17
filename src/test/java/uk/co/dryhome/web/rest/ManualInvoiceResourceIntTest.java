package uk.co.dryhome.web.rest;

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
import uk.co.dryhome.Dryhomecrm1App;
import uk.co.dryhome.domain.ManualInvoice;
import uk.co.dryhome.domain.ManualInvoiceItem;
import uk.co.dryhome.repository.ManualInvoiceItemRepository;
import uk.co.dryhome.repository.ManualInvoiceRepository;
import uk.co.dryhome.service.ManualInvoiceService;
import uk.co.dryhome.service.MergeDocService;
import uk.co.dryhome.service.dto.ManualInvoiceDTO;
import uk.co.dryhome.service.dto.ManualInvoiceDetailDTO;
import uk.co.dryhome.service.mapper.ManualInvoiceMapper;
import uk.co.dryhome.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.dryhome.web.rest.TestUtil.createFormattingConversionService;

/**
 * Test class for the ManualInvoiceResource REST controller.
 *
 * @see ManualInvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dryhomecrm1App.class)
public class ManualInvoiceResourceIntTest {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER = "BBBBBBBBBB";

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

    private static final String DEFAULT_TEL_NO = "AAAAAAAAAA";
    private static final String UPDATED_TEL_NO = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_ADDRESS_3 = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_ADDRESS_3 = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_ADDRESS_4 = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_ADDRESS_4 = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIAL_INSTRUCTIONS_1 = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_INSTRUCTIONS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIAL_INSTRUCTIONS_2 = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_INSTRUCTIONS_2 = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PAYMENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VAT_RATE = new BigDecimal(20);
    private static final BigDecimal UPDATED_VAT_RATE = new BigDecimal("19.5");

    @Autowired
    private ManualInvoiceRepository manualInvoiceRepository;

    @Autowired
    private ManualInvoiceItemRepository manualInvoiceItemRepository;

    @Autowired
    private ManualInvoiceMapper manualInvoiceMapper;

    @Autowired
    private ManualInvoiceService manualInvoiceService;

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

    private MockMvc restManualInvoiceMockMvc;

    private ManualInvoice manualInvoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ManualInvoiceResource manualInvoiceResource = new ManualInvoiceResource(manualInvoiceService);
        this.restManualInvoiceMockMvc = MockMvcBuilders.standaloneSetup(manualInvoiceResource)
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
    public static ManualInvoice createEntity(EntityManager em) {
        ManualInvoice manualInvoice = new ManualInvoice()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .ref(DEFAULT_REF)
            .customer(DEFAULT_CUSTOMER)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .address3(DEFAULT_ADDRESS_3)
            .town(DEFAULT_TOWN)
            .postCode(DEFAULT_POST_CODE)
            .telNo(DEFAULT_TEL_NO)
            .deliveryAddress1(DEFAULT_DELIVERY_ADDRESS_1)
            .deliveryAddress2(DEFAULT_DELIVERY_ADDRESS_2)
            .deliveryAddress3(DEFAULT_DELIVERY_ADDRESS_3)
            .deliveryAddress4(DEFAULT_DELIVERY_ADDRESS_4)
            .specialInstructions1(DEFAULT_SPECIAL_INSTRUCTIONS_1)
            .specialInstructions2(DEFAULT_SPECIAL_INSTRUCTIONS_2)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .vatRate(DEFAULT_VAT_RATE)
            .addItems(new ManualInvoiceItem()
                .price(new BigDecimal("99.99"))
                .quantity(2)
                .product("product1"))
            .addItems(new ManualInvoiceItem()
                .price(new BigDecimal("5.96"))
                .quantity(1)
                .product("product2"));
        return manualInvoice;
    }

    @Before
    public void initTest() {
        manualInvoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createManualInvoice() throws Exception {
        int databaseSizeBeforeCreate = manualInvoiceRepository.findAll().size();

        // Create the ManualInvoice
        ManualInvoiceDetailDTO manualInvoiceDetailDTO = manualInvoiceMapper.toDetailDto(manualInvoice);
        String location = restManualInvoiceMockMvc.perform(post("/api/manual-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualInvoiceDetailDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF.toString()))
            .andExpect(jsonPath("$.customer").value(DEFAULT_CUSTOMER.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.address3").value(DEFAULT_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.telNo").value(DEFAULT_TEL_NO.toString()))
            .andExpect(jsonPath("$.deliveryAddress1").value(DEFAULT_DELIVERY_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.deliveryAddress2").value(DEFAULT_DELIVERY_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.deliveryAddress3").value(DEFAULT_DELIVERY_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.deliveryAddress4").value(DEFAULT_DELIVERY_ADDRESS_4.toString()))
            .andExpect(jsonPath("$.specialInstructions1").value(DEFAULT_SPECIAL_INSTRUCTIONS_1.toString()))
            .andExpect(jsonPath("$.specialInstructions2").value(DEFAULT_SPECIAL_INSTRUCTIONS_2.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.intValue()))
            .andExpect(jsonPath("$.items.length()").value(2))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[0].quantity").value(2))
            .andExpect(jsonPath("$.items[0].price").value("99.99"))
            .andExpect(jsonPath("$.items[0].product").value("product1"))
            .andExpect(jsonPath("$.items[1].id").isNotEmpty())
            .andExpect(jsonPath("$.items[1].quantity").value(1))
            .andExpect(jsonPath("$.items[1].price").value("5.96"))
            .andExpect(jsonPath("$.items[1].product").value("product2"))
            .andExpect(jsonPath("$.subTotal").value("205.94"))
            .andExpect(jsonPath("$.vatAmount").value("41.19"))
            .andExpect(jsonPath("$.total").value("247.13"))
            .andReturn().getResponse().getHeader("Location");

// Get the manualInvoice
        restManualInvoiceMockMvc.perform(get(location, manualInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(location.substring(location.lastIndexOf("/") + 1)))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF.toString()))
            .andExpect(jsonPath("$.customer").value(DEFAULT_CUSTOMER.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.address3").value(DEFAULT_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.telNo").value(DEFAULT_TEL_NO.toString()))
            .andExpect(jsonPath("$.deliveryAddress1").value(DEFAULT_DELIVERY_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.deliveryAddress2").value(DEFAULT_DELIVERY_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.deliveryAddress3").value(DEFAULT_DELIVERY_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.deliveryAddress4").value(DEFAULT_DELIVERY_ADDRESS_4.toString()))
            .andExpect(jsonPath("$.specialInstructions1").value(DEFAULT_SPECIAL_INSTRUCTIONS_1.toString()))
            .andExpect(jsonPath("$.specialInstructions2").value(DEFAULT_SPECIAL_INSTRUCTIONS_2.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.intValue()))
            .andExpect(jsonPath("$.items.length()").value(2))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[0].quantity").value(2))
            .andExpect(jsonPath("$.items[0].price").value("99.99"))
            .andExpect(jsonPath("$.items[0].product").value("product1"))
            .andExpect(jsonPath("$.items[1].id").isNotEmpty())
            .andExpect(jsonPath("$.items[1].quantity").value(1))
            .andExpect(jsonPath("$.items[1].price").value("5.96"))
            .andExpect(jsonPath("$.items[1].product").value("product2"))
            .andExpect(jsonPath("$.subTotal").value("205.94"))
            .andExpect(jsonPath("$.vatAmount").value("41.19"))
            .andExpect(jsonPath("$.total").value("247.13"));

        // Validate the ManualInvoice in the database
        List<ManualInvoice> manualInvoiceList = manualInvoiceRepository.findAll();
        assertThat(manualInvoiceList).hasSize(databaseSizeBeforeCreate + 1);
        ManualInvoice testManualInvoice = manualInvoiceList.get(manualInvoiceList.size() - 1);
        assertThat(testManualInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testManualInvoice.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testManualInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testManualInvoice.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testManualInvoice.getCustomer()).isEqualTo(DEFAULT_CUSTOMER);
        assertThat(testManualInvoice.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testManualInvoice.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testManualInvoice.getAddress3()).isEqualTo(DEFAULT_ADDRESS_3);
        assertThat(testManualInvoice.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testManualInvoice.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testManualInvoice.getTelNo()).isEqualTo(DEFAULT_TEL_NO);
        assertThat(testManualInvoice.getDeliveryAddress1()).isEqualTo(DEFAULT_DELIVERY_ADDRESS_1);
        assertThat(testManualInvoice.getDeliveryAddress2()).isEqualTo(DEFAULT_DELIVERY_ADDRESS_2);
        assertThat(testManualInvoice.getDeliveryAddress3()).isEqualTo(DEFAULT_DELIVERY_ADDRESS_3);
        assertThat(testManualInvoice.getDeliveryAddress4()).isEqualTo(DEFAULT_DELIVERY_ADDRESS_4);
        assertThat(testManualInvoice.getSpecialInstructions1()).isEqualTo(DEFAULT_SPECIAL_INSTRUCTIONS_1);
        assertThat(testManualInvoice.getSpecialInstructions2()).isEqualTo(DEFAULT_SPECIAL_INSTRUCTIONS_2);
        assertThat(testManualInvoice.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testManualInvoice.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testManualInvoice.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testManualInvoice.getPaymentAmount()).isEqualTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testManualInvoice.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);

    }

    @Test
    @Transactional
    public void createManualInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = manualInvoiceRepository.findAll().size();

        // Create the ManualInvoice with an existing ID
        manualInvoice.setId(1L);
        ManualInvoiceDTO manualInvoiceDTO = manualInvoiceMapper.toDto(manualInvoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restManualInvoiceMockMvc.perform(post("/api/manual-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualInvoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ManualInvoice in the database
        List<ManualInvoice> manualInvoiceList = manualInvoiceRepository.findAll();
        assertThat(manualInvoiceList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualInvoiceRepository.findAll().size();
        // set the field null
        manualInvoice.setInvoiceNumber(null);

        // Create the ManualInvoice, which fails.
        ManualInvoiceDTO manualInvoiceDTO = manualInvoiceMapper.toDto(manualInvoice);

        restManualInvoiceMockMvc.perform(post("/api/manual-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualInvoiceDTO)))
            .andExpect(status().isBadRequest());

        List<ManualInvoice> manualInvoiceList = manualInvoiceRepository.findAll();
        assertThat(manualInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualInvoiceRepository.findAll().size();
        // set the field null
        manualInvoice.setOrderNumber(null);

        // Create the ManualInvoice, which fails.
        ManualInvoiceDTO manualInvoiceDTO = manualInvoiceMapper.toDto(manualInvoice);

        restManualInvoiceMockMvc.perform(post("/api/manual-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualInvoiceDTO)))
            .andExpect(status().isBadRequest());

        List<ManualInvoice> manualInvoiceList = manualInvoiceRepository.findAll();
        assertThat(manualInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvoiceDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualInvoiceRepository.findAll().size();
        // set the field null
        manualInvoice.setInvoiceDate(null);

        // Create the ManualInvoice, which fails.
        ManualInvoiceDTO manualInvoiceDTO = manualInvoiceMapper.toDto(manualInvoice);

        restManualInvoiceMockMvc.perform(post("/api/manual-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualInvoiceDTO)))
            .andExpect(status().isBadRequest());

        List<ManualInvoice> manualInvoiceList = manualInvoiceRepository.findAll();
        assertThat(manualInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVatRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualInvoiceRepository.findAll().size();
        // Create the ManualInvoice, which fails.
        ManualInvoiceDTO manualInvoiceDTO = manualInvoiceMapper.toDto(manualInvoice);

        // set the field null
        manualInvoiceDTO.setVatRate(null);

        restManualInvoiceMockMvc.perform(post("/api/manual-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualInvoiceDTO)))
            .andExpect(status().isBadRequest());

        List<ManualInvoice> manualInvoiceList = manualInvoiceRepository.findAll();
        assertThat(manualInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllManualInvoices() throws Exception {
        // Initialize the database
        manualInvoiceRepository.saveAndFlush(manualInvoice);

        // Get all the manualInvoiceList
        restManualInvoiceMockMvc.perform(get("/api/manual-invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manualInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF.toString())))
            .andExpect(jsonPath("$.[*].customer").value(hasItem(DEFAULT_CUSTOMER.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3.toString())))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].telNo").value(hasItem(DEFAULT_TEL_NO.toString())))
            .andExpect(jsonPath("$.[*].deliveryAddress1").value(hasItem(DEFAULT_DELIVERY_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].deliveryAddress2").value(hasItem(DEFAULT_DELIVERY_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].deliveryAddress3").value(hasItem(DEFAULT_DELIVERY_ADDRESS_3.toString())))
            .andExpect(jsonPath("$.[*].deliveryAddress4").value(hasItem(DEFAULT_DELIVERY_ADDRESS_4.toString())))
            .andExpect(jsonPath("$.[*].specialInstructions1").value(hasItem(DEFAULT_SPECIAL_INSTRUCTIONS_1.toString())))
            .andExpect(jsonPath("$.[*].specialInstructions2").value(hasItem(DEFAULT_SPECIAL_INSTRUCTIONS_2.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.intValue())));
    }

    @Test
    @Transactional
    public void getManualInvoice() throws Exception {
        // Initialize the database
        manualInvoiceRepository.saveAndFlush(manualInvoice);

        // Get the manualInvoice
        restManualInvoiceMockMvc.perform(get("/api/manual-invoices/{id}", manualInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(manualInvoice.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF.toString()))
            .andExpect(jsonPath("$.customer").value(DEFAULT_CUSTOMER.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.address3").value(DEFAULT_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.telNo").value(DEFAULT_TEL_NO.toString()))
            .andExpect(jsonPath("$.deliveryAddress1").value(DEFAULT_DELIVERY_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.deliveryAddress2").value(DEFAULT_DELIVERY_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.deliveryAddress3").value(DEFAULT_DELIVERY_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.deliveryAddress4").value(DEFAULT_DELIVERY_ADDRESS_4.toString()))
            .andExpect(jsonPath("$.specialInstructions1").value(DEFAULT_SPECIAL_INSTRUCTIONS_1.toString()))
            .andExpect(jsonPath("$.specialInstructions2").value(DEFAULT_SPECIAL_INSTRUCTIONS_2.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingManualInvoice() throws Exception {
        // Get the manualInvoice
        restManualInvoiceMockMvc.perform(get("/api/manual-invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManualInvoice() throws Exception {
        // Initialize the database
        ManualInvoice savedManualInvoice = manualInvoiceRepository.saveAndFlush(this.manualInvoice);
        manualInvoice.getItems().forEach(i -> {
            i.setManualInvoice(savedManualInvoice);
            manualInvoiceItemRepository.saveAndFlush(i);
        });

        int databaseSizeBeforeUpdate = manualInvoiceRepository.findAll().size();

        // Update the manualInvoice
        ManualInvoice updatedManualInvoice = manualInvoiceRepository.findById(this.manualInvoice.getId()).get();
        // Disconnect from session so that the updates on updatedManualInvoice are not directly saved in db
        em.detach(updatedManualInvoice);
        updatedManualInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .ref(UPDATED_REF)
            .customer(UPDATED_CUSTOMER)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .address3(UPDATED_ADDRESS_3)
            .town(UPDATED_TOWN)
            .postCode(UPDATED_POST_CODE)
            .telNo(UPDATED_TEL_NO)
            .deliveryAddress1(UPDATED_DELIVERY_ADDRESS_1)
            .deliveryAddress2(UPDATED_DELIVERY_ADDRESS_2)
            .deliveryAddress3(UPDATED_DELIVERY_ADDRESS_3)
            .deliveryAddress4(UPDATED_DELIVERY_ADDRESS_4)
            .specialInstructions1(UPDATED_SPECIAL_INSTRUCTIONS_1)
            .specialInstructions2(UPDATED_SPECIAL_INSTRUCTIONS_2)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .vatRate(UPDATED_VAT_RATE);
        ManualInvoiceDetailDTO manualInvoiceDetailDTO = manualInvoiceMapper.toDetailDto(updatedManualInvoice);
        manualInvoiceDetailDTO.getItems().get(0).setPrice(new BigDecimal("49.99"));

        restManualInvoiceMockMvc.perform(put("/api/manual-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualInvoiceDetailDTO)))
            .andExpect(status().isOk());

            restManualInvoiceMockMvc.perform(get("/api/manual-invoices/{id}", manualInvoice.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(manualInvoiceDetailDTO.getId()))
                .andExpect(jsonPath("$.invoiceNumber").value(UPDATED_INVOICE_NUMBER.toString()))
                .andExpect(jsonPath("$.orderNumber").value(UPDATED_ORDER_NUMBER.toString()))
                .andExpect(jsonPath("$.invoiceDate").value(UPDATED_INVOICE_DATE.toString()))
                .andExpect(jsonPath("$.ref").value(UPDATED_REF.toString()))
                .andExpect(jsonPath("$.customer").value(UPDATED_CUSTOMER.toString()))
                .andExpect(jsonPath("$.address1").value(UPDATED_ADDRESS_1.toString()))
                .andExpect(jsonPath("$.address2").value(UPDATED_ADDRESS_2.toString()))
                .andExpect(jsonPath("$.address3").value(UPDATED_ADDRESS_3.toString()))
                .andExpect(jsonPath("$.town").value(UPDATED_TOWN.toString()))
                .andExpect(jsonPath("$.postCode").value(UPDATED_POST_CODE.toString()))
                .andExpect(jsonPath("$.telNo").value(UPDATED_TEL_NO.toString()))
                .andExpect(jsonPath("$.deliveryAddress1").value(UPDATED_DELIVERY_ADDRESS_1.toString()))
                .andExpect(jsonPath("$.deliveryAddress2").value(UPDATED_DELIVERY_ADDRESS_2.toString()))
                .andExpect(jsonPath("$.deliveryAddress3").value(UPDATED_DELIVERY_ADDRESS_3.toString()))
                .andExpect(jsonPath("$.deliveryAddress4").value(UPDATED_DELIVERY_ADDRESS_4.toString()))
                .andExpect(jsonPath("$.specialInstructions1").value(UPDATED_SPECIAL_INSTRUCTIONS_1.toString()))
                .andExpect(jsonPath("$.specialInstructions2").value(UPDATED_SPECIAL_INSTRUCTIONS_2.toString()))
                .andExpect(jsonPath("$.paymentDate").value(UPDATED_PAYMENT_DATE.toString()))
                .andExpect(jsonPath("$.paymentStatus").value(UPDATED_PAYMENT_STATUS.toString()))
                .andExpect(jsonPath("$.paymentType").value(UPDATED_PAYMENT_TYPE.toString()))
                .andExpect(jsonPath("$.paymentAmount").value(UPDATED_PAYMENT_AMOUNT.intValue()))
                .andExpect(jsonPath("$.vatRate").value(UPDATED_VAT_RATE.intValue()))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].id").isNotEmpty())
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].price").value("49.99"))
                .andExpect(jsonPath("$.items[0].product").value("product1"))
                .andExpect(jsonPath("$.items[1].id").isNotEmpty())
                .andExpect(jsonPath("$.items[1].quantity").value(1))
                .andExpect(jsonPath("$.items[1].price").value("5.96"))
                .andExpect(jsonPath("$.items[1].product").value("product2"))
                .andExpect(jsonPath("$.subTotal").value("105.94"))
                .andExpect(jsonPath("$.vatAmount").value("20.66"))
                .andExpect(jsonPath("$.total").value("126.6"));
    }

    @Test
    @Transactional
    public void updateNonExistingManualInvoice() throws Exception {
        int databaseSizeBeforeUpdate = manualInvoiceRepository.findAll().size();

        // Create the ManualInvoice
        ManualInvoiceDTO manualInvoiceDTO = manualInvoiceMapper.toDto(manualInvoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualInvoiceMockMvc.perform(put("/api/manual-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manualInvoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ManualInvoice in the database
        List<ManualInvoice> manualInvoiceList = manualInvoiceRepository.findAll();
        assertThat(manualInvoiceList).hasSize(databaseSizeBeforeUpdate);

    }

    @Test
    @Transactional
    public void deleteManualInvoice() throws Exception {
        // Initialize the database
        manualInvoiceRepository.saveAndFlush(manualInvoice);

        int databaseSizeBeforeDelete = manualInvoiceRepository.findAll().size();

        // Delete the manualInvoice
        restManualInvoiceMockMvc.perform(delete("/api/manual-invoices/{id}", manualInvoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ManualInvoice> manualInvoiceList = manualInvoiceRepository.findAll();
        assertThat(manualInvoiceList).hasSize(databaseSizeBeforeDelete - 1);

    }


    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualInvoice.class);
        ManualInvoice manualInvoice1 = new ManualInvoice();
        manualInvoice1.setId(1L);
        ManualInvoice manualInvoice2 = new ManualInvoice();
        manualInvoice2.setId(manualInvoice1.getId());
        assertThat(manualInvoice1).isEqualTo(manualInvoice2);
        manualInvoice2.setId(2L);
        assertThat(manualInvoice1).isNotEqualTo(manualInvoice2);
        manualInvoice1.setId(null);
        assertThat(manualInvoice1).isNotEqualTo(manualInvoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualInvoiceDTO.class);
        ManualInvoiceDTO manualInvoiceDTO1 = new ManualInvoiceDTO();
        manualInvoiceDTO1.setId(1L);
        ManualInvoiceDTO manualInvoiceDTO2 = new ManualInvoiceDTO();
        assertThat(manualInvoiceDTO1).isNotEqualTo(manualInvoiceDTO2);
        manualInvoiceDTO2.setId(manualInvoiceDTO1.getId());
        assertThat(manualInvoiceDTO1).isEqualTo(manualInvoiceDTO2);
        manualInvoiceDTO2.setId(2L);
        assertThat(manualInvoiceDTO1).isNotEqualTo(manualInvoiceDTO2);
        manualInvoiceDTO1.setId(null);
        assertThat(manualInvoiceDTO1).isNotEqualTo(manualInvoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(manualInvoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(manualInvoiceMapper.fromId(null)).isNull();
    }
}

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
import uk.co.dryhome.domain.Customer;
import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.domain.OrderItem;
import uk.co.dryhome.domain.Product;
import uk.co.dryhome.domain.enumeration.OrderMethod;
import uk.co.dryhome.domain.enumeration.OrderStatus;
import uk.co.dryhome.repository.CustomerOrderRepository;
import uk.co.dryhome.repository.OrderItemRepository;
import uk.co.dryhome.repository.ProductRepository;
import uk.co.dryhome.service.CustomerOrderQueryService;
import uk.co.dryhome.service.CustomerOrderService;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;
import uk.co.dryhome.service.dto.OrderItemDTO;
import uk.co.dryhome.service.mapper.CustomerOrderMapper;
import uk.co.dryhome.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

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
 * Test class for the CustomerOrderResource REST controller.
 *
 * @see CustomerOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dryhomecrm1App.class)
public class CustomerOrderResourceIntTest {

    public static final String PRODUCT_NAME_CARRIAGE = "Carriage";
    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTES_1 = "AAAAAAAAAA";
    private static final String UPDATED_NOTES_1 = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES_2 = "AAAAAAAAAA";
    private static final String UPDATED_NOTES_2 = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DESPATCH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DESPATCH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_VAT_RATE = new BigDecimal(20);
    private static final BigDecimal UPDATED_VAT_RATE = new BigDecimal("19.5");

    private static final String DEFAULT_INTERNAL_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_PLACED_BY = "AAAAAAAAAA";
    private static final String UPDATED_PLACED_BY = "BBBBBBBBBB";

    private static final OrderMethod DEFAULT_METHOD = OrderMethod.PHONE;
    private static final OrderMethod UPDATED_METHOD = OrderMethod.FAX;
    public static final long PRODUCT_ID_REMCON = 6L;
    public static final String PRODUCT_NAME_REMCON = "REMCON";
    public static final long PRODUCT_ID_CARRIAGE = 2L;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private CustomerOrderQueryService customerOrderQueryService;

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

    @Autowired
    private ProductRepository productRepository;

    private MockMvc restCustomerOrderMockMvc;

    private CustomerOrder customerOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerOrderResource customerOrderResource = new CustomerOrderResource(customerOrderService, customerOrderQueryService);
        this.restCustomerOrderMockMvc = MockMvcBuilders.standaloneSetup(customerOrderResource)
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
    public static CustomerOrder createEntity(EntityManager em) {
        Product productRemcon = new Product();
        productRemcon.setName(PRODUCT_NAME_REMCON);
        Product productCarriage = new Product();
        productCarriage.setName(PRODUCT_NAME_CARRIAGE);

        em.persist(productRemcon);
        em.persist(productCarriage);
        em.flush();

        CustomerOrder customerOrder = new CustomerOrder()
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .orderDate(DEFAULT_ORDER_DATE)
            .notes1(DEFAULT_NOTES_1)
            .notes2(DEFAULT_NOTES_2)
            .despatchDate(DEFAULT_DESPATCH_DATE)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .vatRate(DEFAULT_VAT_RATE)
            .internalNotes(DEFAULT_INTERNAL_NOTES)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .placedBy(DEFAULT_PLACED_BY)
            .method(DEFAULT_METHOD)
            .status(OrderStatus.PAID)
            .addItems(new OrderItem()
                .price(new BigDecimal("99.99"))
                .quantity(2)
                .notes("some notes")
                .serialNumber("xyz")
                .product(productRemcon)
                .subTotal(new BigDecimal("199.98")))
            .addItems(new OrderItem()
                .price(new BigDecimal("5.96"))
                .quantity(1)
                .product(productCarriage)
                .subTotal(new BigDecimal("5.96")))
            .subTotal(new BigDecimal("205.94"))
            .vatAmount(new BigDecimal("41.19"))
            .total(new BigDecimal("247.13"));
        // Add required entity
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        customerOrder.setCustomer(customer);
        return customerOrder;
    }

    @Before
    public void initTest() {
        customerOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerOrder() throws Exception {
        int databaseSizeBeforeCreate = customerOrderRepository.findAll().size();

        // Create the CustomerOrder
        CustomerOrderDetailDTO customerOrderDetailDTO = customerOrderMapper.toDetailDto(customerOrder);
        customerOrderDetailDTO.setItems(customerOrder.getItems().stream().map(customerOrderMapper::itemToDto).collect(Collectors.toList()));
        String location = restCustomerOrderMockMvc.perform(post("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderDetailDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.notes1").value(DEFAULT_NOTES_1.toString()))
            .andExpect(jsonPath("$.notes2").value(DEFAULT_NOTES_2.toString()))
            .andExpect(jsonPath("$.despatchDate").value(DEFAULT_DESPATCH_DATE.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.intValue()))
            .andExpect(jsonPath("$.internalNotes").value(DEFAULT_INTERNAL_NOTES.toString()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.placedBy").value(DEFAULT_PLACED_BY.toString()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()))
            .andExpect(jsonPath("$.items.length()").value(2))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[0].quantity").value(2))
            .andExpect(jsonPath("$.items[0].price").value("99.99"))
            .andExpect(jsonPath("$.items[0].notes").value("some notes"))
            .andExpect(jsonPath("$.items[0].serialNumber").value("xyz"))
            .andExpect(jsonPath("$.items[0].product").value("REMCON"))
            .andExpect(jsonPath("$.items[1].id").isNotEmpty())
            .andExpect(jsonPath("$.items[1].quantity").value(1))
            .andExpect(jsonPath("$.items[1].price").value("5.96"))
            .andExpect(jsonPath("$.items[1].notes").isEmpty())
            .andExpect(jsonPath("$.items[1].serialNumber").isEmpty())
            .andExpect(jsonPath("$.items[1].product").value("Carriage"))
            .andExpect(jsonPath("$.subTotal").value("205.94"))
            .andExpect(jsonPath("$.vatAmount").value("41.19"))
            .andExpect(jsonPath("$.total").value("247.13"))
            .andReturn().getResponse().getHeader("Location");

        restCustomerOrderMockMvc.perform(get(location, customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(location.substring(location.lastIndexOf("/") + 1)))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.notes1").value(DEFAULT_NOTES_1.toString()))
            .andExpect(jsonPath("$.notes2").value(DEFAULT_NOTES_2.toString()))
            .andExpect(jsonPath("$.despatchDate").value(DEFAULT_DESPATCH_DATE.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.intValue()))
            .andExpect(jsonPath("$.internalNotes").value(DEFAULT_INTERNAL_NOTES.toString()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.placedBy").value(DEFAULT_PLACED_BY.toString()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()))
            .andExpect(jsonPath("$.items.length()").value(2))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[0].quantity").value(2))
            .andExpect(jsonPath("$.items[0].price").value("99.99"))
            .andExpect(jsonPath("$.items[0].notes").value("some notes"))
            .andExpect(jsonPath("$.items[0].serialNumber").value("xyz"))
            .andExpect(jsonPath("$.items[0].product").value("REMCON"))
            .andExpect(jsonPath("$.items[1].id").isNotEmpty())
            .andExpect(jsonPath("$.items[1].quantity").value(1))
            .andExpect(jsonPath("$.items[1].price").value("5.96"))
            .andExpect(jsonPath("$.items[1].notes").isEmpty())
            .andExpect(jsonPath("$.items[1].serialNumber").isEmpty())
            .andExpect(jsonPath("$.items[1].product").value("Carriage"))
            .andExpect(jsonPath("$.subTotal").value("205.94"))
            .andExpect(jsonPath("$.vatAmount").value("41.19"))
            .andExpect(jsonPath("$.total").value("247.13"));
    }

    @Test
    @Transactional
    public void createCustomerOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerOrderRepository.findAll().size();

        // Create the CustomerOrder with an existing ID
        customerOrder.setId(1L);
        CustomerOrderDetailDTO customerOrderDetailDTO = customerOrderMapper.toDetailDto(customerOrder);
        customerOrderDetailDTO.setItems(customerOrder.getItems().stream().map(customerOrderMapper::itemToDto).collect(Collectors.toList()));

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerOrderMockMvc.perform(post("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setOrderNumber(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderSummaryDTO customerOrderSummaryDTO = customerOrderMapper.toDto(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setOrderDate(null);

        // Create the CustomerOrder, which fails.
        CustomerOrderSummaryDTO customerOrderSummaryDTO = customerOrderMapper.toDto(customerOrder);

        restCustomerOrderMockMvc.perform(post("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVatRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();

        // Create the CustomerOrder, which fails.
        CustomerOrderSummaryDTO customerOrderSummaryDTO = customerOrderMapper.toDto(customerOrder);
        // set the field null
        customerOrderSummaryDTO.setVatRate(null);

        restCustomerOrderMockMvc.perform(post("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerOrders() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList
        restCustomerOrderMockMvc.perform(get("/api/customer-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes1").value(hasItem(DEFAULT_NOTES_1.toString())))
            .andExpect(jsonPath("$.[*].notes2").value(hasItem(DEFAULT_NOTES_2.toString())))
            .andExpect(jsonPath("$.[*].despatchDate").value(hasItem(DEFAULT_DESPATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].internalNotes").value(hasItem(DEFAULT_INTERNAL_NOTES.toString())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].placedBy").value(hasItem(DEFAULT_PLACED_BY.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())));
    }

    @Test
    @Transactional
    public void getCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get("/api/customer-orders/{id}", customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.notes1").value(DEFAULT_NOTES_1.toString()))
            .andExpect(jsonPath("$.notes2").value(DEFAULT_NOTES_2.toString()))
            .andExpect(jsonPath("$.despatchDate").value(DEFAULT_DESPATCH_DATE.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.intValue()))
            .andExpect(jsonPath("$.internalNotes").value(DEFAULT_INTERNAL_NOTES.toString()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.placedBy").value(DEFAULT_PLACED_BY.toString()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()));
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderNumber equals to DEFAULT_ORDER_NUMBER
        defaultCustomerOrderShouldBeFound("orderNumber.equals=" + DEFAULT_ORDER_NUMBER);

        // Get all the customerOrderList where orderNumber equals to UPDATED_ORDER_NUMBER
        defaultCustomerOrderShouldNotBeFound("orderNumber.equals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderNumber in DEFAULT_ORDER_NUMBER or UPDATED_ORDER_NUMBER
        defaultCustomerOrderShouldBeFound("orderNumber.in=" + DEFAULT_ORDER_NUMBER + "," + UPDATED_ORDER_NUMBER);

        // Get all the customerOrderList where orderNumber equals to UPDATED_ORDER_NUMBER
        defaultCustomerOrderShouldNotBeFound("orderNumber.in=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderNumber is not null
        defaultCustomerOrderShouldBeFound("orderNumber.specified=true");

        // Get all the customerOrderList where orderNumber is null
        defaultCustomerOrderShouldNotBeFound("orderNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderDate equals to DEFAULT_ORDER_DATE
        defaultCustomerOrderShouldBeFound("orderDate.equals=" + DEFAULT_ORDER_DATE);

        // Get all the customerOrderList where orderDate equals to UPDATED_ORDER_DATE
        defaultCustomerOrderShouldNotBeFound("orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderDate in DEFAULT_ORDER_DATE or UPDATED_ORDER_DATE
        defaultCustomerOrderShouldBeFound("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE);

        // Get all the customerOrderList where orderDate equals to UPDATED_ORDER_DATE
        defaultCustomerOrderShouldNotBeFound("orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderDate is not null
        defaultCustomerOrderShouldBeFound("orderDate.specified=true");

        // Get all the customerOrderList where orderDate is null
        defaultCustomerOrderShouldNotBeFound("orderDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByOrderDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderDate greater than or equals to DEFAULT_ORDER_DATE
        defaultCustomerOrderShouldBeFound("orderDate.greaterOrEqualThan=" + DEFAULT_ORDER_DATE);

        // Get all the customerOrderList where orderDate greater than or equals to UPDATED_ORDER_DATE
        defaultCustomerOrderShouldNotBeFound("orderDate.greaterOrEqualThan=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByOrderDateIsLessThanSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where orderDate less than or equals to DEFAULT_ORDER_DATE
        defaultCustomerOrderShouldNotBeFound("orderDate.lessThan=" + DEFAULT_ORDER_DATE);

        // Get all the customerOrderList where orderDate less than or equals to UPDATED_ORDER_DATE
        defaultCustomerOrderShouldBeFound("orderDate.lessThan=" + UPDATED_ORDER_DATE);
    }


    @Test
    @Transactional
    public void getAllCustomerOrdersByNotes1IsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where notes1 equals to DEFAULT_NOTES_1
        defaultCustomerOrderShouldBeFound("notes1.equals=" + DEFAULT_NOTES_1);

        // Get all the customerOrderList where notes1 equals to UPDATED_NOTES_1
        defaultCustomerOrderShouldNotBeFound("notes1.equals=" + UPDATED_NOTES_1);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByNotes1IsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where notes1 in DEFAULT_NOTES_1 or UPDATED_NOTES_1
        defaultCustomerOrderShouldBeFound("notes1.in=" + DEFAULT_NOTES_1 + "," + UPDATED_NOTES_1);

        // Get all the customerOrderList where notes1 equals to UPDATED_NOTES_1
        defaultCustomerOrderShouldNotBeFound("notes1.in=" + UPDATED_NOTES_1);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByNotes1IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where notes1 is not null
        defaultCustomerOrderShouldBeFound("notes1.specified=true");

        // Get all the customerOrderList where notes1 is null
        defaultCustomerOrderShouldNotBeFound("notes1.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByNotes2IsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where notes2 equals to DEFAULT_NOTES_2
        defaultCustomerOrderShouldBeFound("notes2.equals=" + DEFAULT_NOTES_2);

        // Get all the customerOrderList where notes2 equals to UPDATED_NOTES_2
        defaultCustomerOrderShouldNotBeFound("notes2.equals=" + UPDATED_NOTES_2);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByNotes2IsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where notes2 in DEFAULT_NOTES_2 or UPDATED_NOTES_2
        defaultCustomerOrderShouldBeFound("notes2.in=" + DEFAULT_NOTES_2 + "," + UPDATED_NOTES_2);

        // Get all the customerOrderList where notes2 equals to UPDATED_NOTES_2
        defaultCustomerOrderShouldNotBeFound("notes2.in=" + UPDATED_NOTES_2);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByNotes2IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where notes2 is not null
        defaultCustomerOrderShouldBeFound("notes2.specified=true");

        // Get all the customerOrderList where notes2 is null
        defaultCustomerOrderShouldNotBeFound("notes2.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByDespatchDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where despatchDate equals to DEFAULT_DESPATCH_DATE
        defaultCustomerOrderShouldBeFound("despatchDate.equals=" + DEFAULT_DESPATCH_DATE);

        // Get all the customerOrderList where despatchDate equals to UPDATED_DESPATCH_DATE
        defaultCustomerOrderShouldNotBeFound("despatchDate.equals=" + UPDATED_DESPATCH_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByDespatchDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where despatchDate in DEFAULT_DESPATCH_DATE or UPDATED_DESPATCH_DATE
        defaultCustomerOrderShouldBeFound("despatchDate.in=" + DEFAULT_DESPATCH_DATE + "," + UPDATED_DESPATCH_DATE);

        // Get all the customerOrderList where despatchDate equals to UPDATED_DESPATCH_DATE
        defaultCustomerOrderShouldNotBeFound("despatchDate.in=" + UPDATED_DESPATCH_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByDespatchDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where despatchDate is not null
        defaultCustomerOrderShouldBeFound("despatchDate.specified=true");

        // Get all the customerOrderList where despatchDate is null
        defaultCustomerOrderShouldNotBeFound("despatchDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByDespatchDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where despatchDate greater than or equals to DEFAULT_DESPATCH_DATE
        defaultCustomerOrderShouldBeFound("despatchDate.greaterOrEqualThan=" + DEFAULT_DESPATCH_DATE);

        // Get all the customerOrderList where despatchDate greater than or equals to UPDATED_DESPATCH_DATE
        defaultCustomerOrderShouldNotBeFound("despatchDate.greaterOrEqualThan=" + UPDATED_DESPATCH_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByDespatchDateIsLessThanSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where despatchDate less than or equals to DEFAULT_DESPATCH_DATE
        defaultCustomerOrderShouldNotBeFound("despatchDate.lessThan=" + DEFAULT_DESPATCH_DATE);

        // Get all the customerOrderList where despatchDate less than or equals to UPDATED_DESPATCH_DATE
        defaultCustomerOrderShouldBeFound("despatchDate.lessThan=" + UPDATED_DESPATCH_DATE);
    }


    @Test
    @Transactional
    public void getAllCustomerOrdersByInvoiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where invoiceDate equals to DEFAULT_INVOICE_DATE
        defaultCustomerOrderShouldBeFound("invoiceDate.equals=" + DEFAULT_INVOICE_DATE);

        // Get all the customerOrderList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultCustomerOrderShouldNotBeFound("invoiceDate.equals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInvoiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where invoiceDate in DEFAULT_INVOICE_DATE or UPDATED_INVOICE_DATE
        defaultCustomerOrderShouldBeFound("invoiceDate.in=" + DEFAULT_INVOICE_DATE + "," + UPDATED_INVOICE_DATE);

        // Get all the customerOrderList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultCustomerOrderShouldNotBeFound("invoiceDate.in=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInvoiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where invoiceDate is not null
        defaultCustomerOrderShouldBeFound("invoiceDate.specified=true");

        // Get all the customerOrderList where invoiceDate is null
        defaultCustomerOrderShouldNotBeFound("invoiceDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInvoiceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where invoiceDate greater than or equals to DEFAULT_INVOICE_DATE
        defaultCustomerOrderShouldBeFound("invoiceDate.greaterOrEqualThan=" + DEFAULT_INVOICE_DATE);

        // Get all the customerOrderList where invoiceDate greater than or equals to UPDATED_INVOICE_DATE
        defaultCustomerOrderShouldNotBeFound("invoiceDate.greaterOrEqualThan=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInvoiceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where invoiceDate less than or equals to DEFAULT_INVOICE_DATE
        defaultCustomerOrderShouldNotBeFound("invoiceDate.lessThan=" + DEFAULT_INVOICE_DATE);

        // Get all the customerOrderList where invoiceDate less than or equals to UPDATED_INVOICE_DATE
        defaultCustomerOrderShouldBeFound("invoiceDate.lessThan=" + UPDATED_INVOICE_DATE);
    }


    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultCustomerOrderShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the customerOrderList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultCustomerOrderShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultCustomerOrderShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the customerOrderList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultCustomerOrderShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentDate is not null
        defaultCustomerOrderShouldBeFound("paymentDate.specified=true");

        // Get all the customerOrderList where paymentDate is null
        defaultCustomerOrderShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentDate greater than or equals to DEFAULT_PAYMENT_DATE
        defaultCustomerOrderShouldBeFound("paymentDate.greaterOrEqualThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the customerOrderList where paymentDate greater than or equals to UPDATED_PAYMENT_DATE
        defaultCustomerOrderShouldNotBeFound("paymentDate.greaterOrEqualThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentDate less than or equals to DEFAULT_PAYMENT_DATE
        defaultCustomerOrderShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the customerOrderList where paymentDate less than or equals to UPDATED_PAYMENT_DATE
        defaultCustomerOrderShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }


    @Test
    @Transactional
    public void getAllCustomerOrdersByVatRateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where vatRate equals to DEFAULT_VAT_RATE
        defaultCustomerOrderShouldBeFound("vatRate.equals=" + DEFAULT_VAT_RATE);

        // Get all the customerOrderList where vatRate equals to UPDATED_VAT_RATE
        defaultCustomerOrderShouldNotBeFound("vatRate.equals=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByVatRateIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where vatRate in DEFAULT_VAT_RATE or UPDATED_VAT_RATE
        defaultCustomerOrderShouldBeFound("vatRate.in=" + DEFAULT_VAT_RATE + "," + UPDATED_VAT_RATE);

        // Get all the customerOrderList where vatRate equals to UPDATED_VAT_RATE
        defaultCustomerOrderShouldNotBeFound("vatRate.in=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByVatRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where vatRate is not null
        defaultCustomerOrderShouldBeFound("vatRate.specified=true");

        // Get all the customerOrderList where vatRate is null
        defaultCustomerOrderShouldNotBeFound("vatRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInternalNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where internalNotes equals to DEFAULT_INTERNAL_NOTES
        defaultCustomerOrderShouldBeFound("internalNotes.equals=" + DEFAULT_INTERNAL_NOTES);

        // Get all the customerOrderList where internalNotes equals to UPDATED_INTERNAL_NOTES
        defaultCustomerOrderShouldNotBeFound("internalNotes.equals=" + UPDATED_INTERNAL_NOTES);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInternalNotesIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where internalNotes in DEFAULT_INTERNAL_NOTES or UPDATED_INTERNAL_NOTES
        defaultCustomerOrderShouldBeFound("internalNotes.in=" + DEFAULT_INTERNAL_NOTES + "," + UPDATED_INTERNAL_NOTES);

        // Get all the customerOrderList where internalNotes equals to UPDATED_INTERNAL_NOTES
        defaultCustomerOrderShouldNotBeFound("internalNotes.in=" + UPDATED_INTERNAL_NOTES);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInternalNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where internalNotes is not null
        defaultCustomerOrderShouldBeFound("internalNotes.specified=true");

        // Get all the customerOrderList where internalNotes is null
        defaultCustomerOrderShouldNotBeFound("internalNotes.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where invoiceNumber equals to DEFAULT_INVOICE_NUMBER
        defaultCustomerOrderShouldBeFound("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER);

        // Get all the customerOrderList where invoiceNumber equals to UPDATED_INVOICE_NUMBER
        defaultCustomerOrderShouldNotBeFound("invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where invoiceNumber in DEFAULT_INVOICE_NUMBER or UPDATED_INVOICE_NUMBER
        defaultCustomerOrderShouldBeFound("invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER);

        // Get all the customerOrderList where invoiceNumber equals to UPDATED_INVOICE_NUMBER
        defaultCustomerOrderShouldNotBeFound("invoiceNumber.in=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where invoiceNumber is not null
        defaultCustomerOrderShouldBeFound("invoiceNumber.specified=true");

        // Get all the customerOrderList where invoiceNumber is null
        defaultCustomerOrderShouldNotBeFound("invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentStatus equals to DEFAULT_PAYMENT_STATUS
        defaultCustomerOrderShouldBeFound("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS);

        // Get all the customerOrderList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultCustomerOrderShouldNotBeFound("paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentStatus in DEFAULT_PAYMENT_STATUS or UPDATED_PAYMENT_STATUS
        defaultCustomerOrderShouldBeFound("paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS);

        // Get all the customerOrderList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultCustomerOrderShouldNotBeFound("paymentStatus.in=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentStatus is not null
        defaultCustomerOrderShouldBeFound("paymentStatus.specified=true");

        // Get all the customerOrderList where paymentStatus is null
        defaultCustomerOrderShouldNotBeFound("paymentStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentType equals to DEFAULT_PAYMENT_TYPE
        defaultCustomerOrderShouldBeFound("paymentType.equals=" + DEFAULT_PAYMENT_TYPE);

        // Get all the customerOrderList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultCustomerOrderShouldNotBeFound("paymentType.equals=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentType in DEFAULT_PAYMENT_TYPE or UPDATED_PAYMENT_TYPE
        defaultCustomerOrderShouldBeFound("paymentType.in=" + DEFAULT_PAYMENT_TYPE + "," + UPDATED_PAYMENT_TYPE);

        // Get all the customerOrderList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultCustomerOrderShouldNotBeFound("paymentType.in=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentType is not null
        defaultCustomerOrderShouldBeFound("paymentType.specified=true");

        // Get all the customerOrderList where paymentType is null
        defaultCustomerOrderShouldNotBeFound("paymentType.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentAmount equals to DEFAULT_PAYMENT_AMOUNT
        defaultCustomerOrderShouldBeFound("paymentAmount.equals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the customerOrderList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultCustomerOrderShouldNotBeFound("paymentAmount.equals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentAmount in DEFAULT_PAYMENT_AMOUNT or UPDATED_PAYMENT_AMOUNT
        defaultCustomerOrderShouldBeFound("paymentAmount.in=" + DEFAULT_PAYMENT_AMOUNT + "," + UPDATED_PAYMENT_AMOUNT);

        // Get all the customerOrderList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultCustomerOrderShouldNotBeFound("paymentAmount.in=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where paymentAmount is not null
        defaultCustomerOrderShouldBeFound("paymentAmount.specified=true");

        // Get all the customerOrderList where paymentAmount is null
        defaultCustomerOrderShouldNotBeFound("paymentAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPlacedByIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where placedBy equals to DEFAULT_PLACED_BY
        defaultCustomerOrderShouldBeFound("placedBy.equals=" + DEFAULT_PLACED_BY);

        // Get all the customerOrderList where placedBy equals to UPDATED_PLACED_BY
        defaultCustomerOrderShouldNotBeFound("placedBy.equals=" + UPDATED_PLACED_BY);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPlacedByIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where placedBy in DEFAULT_PLACED_BY or UPDATED_PLACED_BY
        defaultCustomerOrderShouldBeFound("placedBy.in=" + DEFAULT_PLACED_BY + "," + UPDATED_PLACED_BY);

        // Get all the customerOrderList where placedBy equals to UPDATED_PLACED_BY
        defaultCustomerOrderShouldNotBeFound("placedBy.in=" + UPDATED_PLACED_BY);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByPlacedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where placedBy is not null
        defaultCustomerOrderShouldBeFound("placedBy.specified=true");

        // Get all the customerOrderList where placedBy is null
        defaultCustomerOrderShouldNotBeFound("placedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where method equals to DEFAULT_METHOD
        defaultCustomerOrderShouldBeFound("method.equals=" + DEFAULT_METHOD);

        // Get all the customerOrderList where method equals to UPDATED_METHOD
        defaultCustomerOrderShouldNotBeFound("method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where method in DEFAULT_METHOD or UPDATED_METHOD
        defaultCustomerOrderShouldBeFound("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD);

        // Get all the customerOrderList where method equals to UPDATED_METHOD
        defaultCustomerOrderShouldNotBeFound("method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList where method is not null
        defaultCustomerOrderShouldBeFound("method.specified=true");

        // Get all the customerOrderList where method is null
        defaultCustomerOrderShouldNotBeFound("method.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerOrdersByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        OrderItem items = createOrderItemEntity(em);
        em.persist(items);
        em.flush();
        customerOrder.addItems(items);
        customerOrderRepository.saveAndFlush(customerOrder);
        Long itemsId = items.getId();

        // Get all the customerOrderList where items equals to itemsId
        defaultCustomerOrderShouldBeFound("itemsId.equals=" + itemsId);

        // Get all the customerOrderList where items equals to itemsId + 1
        defaultCustomerOrderShouldNotBeFound("itemsId.equals=" + (itemsId + 1));
    }


    @Test
    @Transactional
    public void getAllCustomerOrdersByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        customerOrder.setCustomer(customer);
        customerOrderRepository.saveAndFlush(customerOrder);
        Long customerId = customer.getId();

        // Get all the customerOrderList where customer equals to customerId
        defaultCustomerOrderShouldBeFound("customerId.equals=" + customerId);

        // Get all the customerOrderList where customer equals to customerId + 1
        defaultCustomerOrderShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCustomerOrderShouldBeFound(String filter) throws Exception {
        restCustomerOrderMockMvc.perform(get("/api/customer-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes1").value(hasItem(DEFAULT_NOTES_1)))
            .andExpect(jsonPath("$.[*].notes2").value(hasItem(DEFAULT_NOTES_2)))
            .andExpect(jsonPath("$.[*].despatchDate").value(hasItem(DEFAULT_DESPATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].internalNotes").value(hasItem(DEFAULT_INTERNAL_NOTES)))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].placedBy").value(hasItem(DEFAULT_PLACED_BY)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())));

        // Check, that the count call also returns 1
        restCustomerOrderMockMvc.perform(get("/api/customer-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCustomerOrderShouldNotBeFound(String filter) throws Exception {
        restCustomerOrderMockMvc.perform(get("/api/customer-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerOrderMockMvc.perform(get("/api/customer-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCustomerOrder() throws Exception {
        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get("/api/customer-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerOrder() throws Exception {
        // Initialize the database
        CustomerOrder savedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);
        customerOrder.getItems().forEach(i -> {
            i.setCustomerOrder(savedCustomerOrder);
            orderItemRepository.saveAndFlush(i);
        });

        Product newProduct = new Product();
        newProduct.setName("new carriage");
        newProduct.setDescription("new carriage desc");
        productRepository.saveAndFlush(newProduct);

        // Update the customerOrder
        CustomerOrder updatedCustomerOrder = customerOrderRepository.findById(customerOrder.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerOrder are not directly saved in db
        em.detach(updatedCustomerOrder);
        updatedCustomerOrder
            .orderNumber(UPDATED_ORDER_NUMBER)
            .orderDate(UPDATED_ORDER_DATE)
            .notes1(UPDATED_NOTES_1)
            .notes2(UPDATED_NOTES_2)
            .despatchDate(UPDATED_DESPATCH_DATE)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .vatRate(UPDATED_VAT_RATE)
            .internalNotes(UPDATED_INTERNAL_NOTES)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .placedBy(UPDATED_PLACED_BY)
            .method(UPDATED_METHOD);
        CustomerOrderDetailDTO customerOrderDetailDTO = customerOrderMapper.toDetailDto(updatedCustomerOrder);
        customerOrderDetailDTO.getItems().get(0).setPrice(new BigDecimal("49.99"));
        customerOrderDetailDTO.getItems().get(1).setProductId(newProduct.getId());

        restCustomerOrderMockMvc.perform(put("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderDetailDTO)))
            .andExpect(status().isOk());

        restCustomerOrderMockMvc.perform(get("/api/customer-orders/{id}", customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerOrderDetailDTO.getId()))
            .andExpect(jsonPath("$.orderNumber").value(UPDATED_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.orderDate").value(UPDATED_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.notes1").value(UPDATED_NOTES_1.toString()))
            .andExpect(jsonPath("$.notes2").value(UPDATED_NOTES_2.toString()))
            .andExpect(jsonPath("$.despatchDate").value(UPDATED_DESPATCH_DATE.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(UPDATED_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(UPDATED_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.vatRate").value(UPDATED_VAT_RATE.intValue()))
            .andExpect(jsonPath("$.internalNotes").value(UPDATED_INTERNAL_NOTES.toString()))
            .andExpect(jsonPath("$.invoiceNumber").value(UPDATED_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(UPDATED_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(UPDATED_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(UPDATED_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.placedBy").value(UPDATED_PLACED_BY.toString()))
            .andExpect(jsonPath("$.method").value(UPDATED_METHOD.toString()))
            .andExpect(jsonPath("$.items.length()").value(2))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[0].quantity").value(2))
            .andExpect(jsonPath("$.items[0].price").value("49.99"))
            .andExpect(jsonPath("$.items[0].notes").value("some notes"))
            .andExpect(jsonPath("$.items[0].serialNumber").value("xyz"))
            .andExpect(jsonPath("$.items[0].product").value("REMCON"))
            .andExpect(jsonPath("$.items[1].id").isNotEmpty())
            .andExpect(jsonPath("$.items[1].quantity").value(1))
            .andExpect(jsonPath("$.items[1].price").value("5.96"))
            .andExpect(jsonPath("$.items[1].notes").isEmpty())
            .andExpect(jsonPath("$.items[1].serialNumber").isEmpty())
            .andExpect(jsonPath("$.items[1].product").value("new carriage"))
            .andExpect(jsonPath("$.subTotal").value("105.94"))
            .andExpect(jsonPath("$.vatAmount").value("20.66"))
            .andExpect(jsonPath("$.total").value("126.6")); //todo why not 2 dp - Jackson?


    }

    @Test
    @Transactional
    public void updateCustomerOrderAddItems() throws Exception {
        // Initialize the database
        CustomerOrder savedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);
        customerOrder.getItems().forEach(i -> {
            i.setCustomerOrder(savedCustomerOrder);
            orderItemRepository.saveAndFlush(i);
        });

        // Update the customerOrder
        CustomerOrder updatedCustomerOrder = customerOrderRepository.findById(customerOrder.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerOrder are not directly saved in db
        em.detach(updatedCustomerOrder);
        CustomerOrderDetailDTO customerOrderDetailDTO = customerOrderMapper.toDetailDto(updatedCustomerOrder);
        OrderItemDTO newItem = new OrderItemDTO();
        customerOrderDetailDTO.getItems().add(newItem);
        newItem.setProductId(productRepository.findByName("REMCON").orElseThrow(() -> new RuntimeException("product not found: ")).getId());
        newItem.setPrice(new BigDecimal("1.99"));
        newItem.setQuantity(2);

        restCustomerOrderMockMvc.perform(put("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderDetailDTO)))
            .andExpect(status().isOk());

        restCustomerOrderMockMvc.perform(get("/api/customer-orders/{id}", customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerOrderDetailDTO.getId()))
            .andExpect(jsonPath("$.items.length()").value(3))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[0].quantity").value(2))
            .andExpect(jsonPath("$.items[0].price").value("99.99"))
            .andExpect(jsonPath("$.items[0].notes").value("some notes"))
            .andExpect(jsonPath("$.items[0].serialNumber").value("xyz"))
            .andExpect(jsonPath("$.items[0].product").value("REMCON"))
            .andExpect(jsonPath("$.items[1].id").isNotEmpty())
            .andExpect(jsonPath("$.items[1].quantity").value(1))
            .andExpect(jsonPath("$.items[1].price").value("5.96"))
            .andExpect(jsonPath("$.items[1].notes").isEmpty())
            .andExpect(jsonPath("$.items[1].serialNumber").isEmpty())
            .andExpect(jsonPath("$.items[1].product").value("Carriage"))
            .andExpect(jsonPath("$.items[2].id").isNotEmpty())
            .andExpect(jsonPath("$.items[2].quantity").value(2))
            .andExpect(jsonPath("$.items[2].price").value("1.99"))
            .andExpect(jsonPath("$.items[2].notes").isEmpty())
            .andExpect(jsonPath("$.items[2].serialNumber").isEmpty())
            .andExpect(jsonPath("$.items[2].product").value("REMCON"))
            .andExpect(jsonPath("$.subTotal").value("209.92"))
            .andExpect(jsonPath("$.vatAmount").value("41.98"))
            .andExpect(jsonPath("$.total").value("251.9")); //todo why not 2 dp - Jackson?
    }

    @Test
    @Transactional
    public void updateCustomerOrderRemoveOrderItem() throws Exception {
        // Initialize the database
        CustomerOrder savedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);
        customerOrder.getItems().forEach(i -> {
            i.setCustomerOrder(savedCustomerOrder);
            orderItemRepository.saveAndFlush(i);
        });

        // Update the customerOrder
        CustomerOrder updatedCustomerOrder = customerOrderRepository.findById(customerOrder.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerOrder are not directly saved in db
        em.detach(updatedCustomerOrder);
        CustomerOrderDetailDTO customerOrderDetailDTO = customerOrderMapper.toDetailDto(updatedCustomerOrder);
        customerOrderDetailDTO.getItems().removeIf(x -> x.getProduct().equals("Carriage"));

        restCustomerOrderMockMvc.perform(put("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderDetailDTO)))
            .andExpect(status().isOk());

        restCustomerOrderMockMvc.perform(get("/api/customer-orders/{id}", customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerOrderDetailDTO.getId()))
            .andExpect(jsonPath("$.items.length()").value(1))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[0].quantity").value(2))
            .andExpect(jsonPath("$.items[0].price").value("99.99"))
            .andExpect(jsonPath("$.items[0].notes").value("some notes"))
            .andExpect(jsonPath("$.items[0].serialNumber").value("xyz"))
            .andExpect(jsonPath("$.items[0].product").value("REMCON"))
            .andExpect(jsonPath("$.subTotal").value("199.98"))
            .andExpect(jsonPath("$.vatAmount").value("40.0")) //todo why 1 dp
            .andExpect(jsonPath("$.total").value("239.98"));
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerOrder() throws Exception {
        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();

        // Create the CustomerOrder
        CustomerOrderSummaryDTO customerOrderSummaryDTO = customerOrderMapper.toDto(customerOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc.perform(put("/api/customer-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerOrderSummaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);

    }

    @Test
    @Transactional
    public void deleteCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        int databaseSizeBeforeDelete = customerOrderRepository.findAll().size();

        // Delete the customerOrder
        restCustomerOrderMockMvc.perform(delete("/api/customer-orders/{id}", customerOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeDelete - 1);

    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerOrder.class);
        CustomerOrder customerOrder1 = new CustomerOrder();
        customerOrder1.setId(1L);
        CustomerOrder customerOrder2 = new CustomerOrder();
        customerOrder2.setId(customerOrder1.getId());
        assertThat(customerOrder1).isEqualTo(customerOrder2);
        customerOrder2.setId(2L);
        assertThat(customerOrder1).isNotEqualTo(customerOrder2);
        customerOrder1.setId(null);
        assertThat(customerOrder1).isNotEqualTo(customerOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerOrderSummaryDTO.class);
        CustomerOrderSummaryDTO customerOrderDTO1 = new CustomerOrderSummaryDTO();
        customerOrderDTO1.setId(1L);
        CustomerOrderSummaryDTO customerOrderDTO2 = new CustomerOrderSummaryDTO();
        assertThat(customerOrderDTO1).isNotEqualTo(customerOrderDTO2);
        customerOrderDTO2.setId(customerOrderDTO1.getId());
        assertThat(customerOrderDTO1).isEqualTo(customerOrderDTO2);
        customerOrderDTO2.setId(2L);
        assertThat(customerOrderDTO1).isNotEqualTo(customerOrderDTO2);
        customerOrderDTO1.setId(null);
        assertThat(customerOrderDTO1).isNotEqualTo(customerOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customerOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customerOrderMapper.fromId(null)).isNull();
    }

    public static OrderItem createOrderItemEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .notes(DEFAULT_NOTES)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .subTotal(BigDecimal.ONE);
        // Add required entity
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        orderItem.setProduct(product);
        // Add required entity
        CustomerOrder customerOrder = CustomerOrderResourceIntTest.createEntity(em);
        em.persist(customerOrder);
        em.flush();
        orderItem.setCustomerOrder(customerOrder);
        return orderItem;
    }
}

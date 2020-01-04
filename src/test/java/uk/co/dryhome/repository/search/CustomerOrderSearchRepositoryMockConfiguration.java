package uk.co.dryhome.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CustomerOrderSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CustomerOrderSearchRepositoryMockConfiguration {

    @MockBean
    private CustomerOrderSearchRepository mockCustomerOrderSearchRepository;

}

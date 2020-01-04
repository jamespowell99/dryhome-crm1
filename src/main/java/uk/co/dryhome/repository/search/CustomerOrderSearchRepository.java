package uk.co.dryhome.repository.search;

import uk.co.dryhome.domain.CustomerOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomerOrder entity.
 */
public interface CustomerOrderSearchRepository extends ElasticsearchRepository<CustomerOrder, Long> {
}

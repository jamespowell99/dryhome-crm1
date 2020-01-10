package uk.co.dryhome.repository;

import uk.co.dryhome.domain.OrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the OrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByCustomerOrderIdOrderById(Long customerOrderId);

    int deleteByCustomerOrderId(Long customerOrderId);

}

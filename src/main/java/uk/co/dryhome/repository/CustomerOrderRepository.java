package uk.co.dryhome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.domain.OrderSummary;


/**
 * Spring Data  repository for the CustomerOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {
    @Query( nativeQuery = true, value = "select count(*), sum(subtotal) preVatTotal, sum(total) postVatTotal from (\n" +
        "  select  id, ROUND(sum(sub.item_subtotal), 2) subtotal,\n" +
        "    ROUND(sum(sub.item_subtotal * (1 + (vat_rate / 100))), 2) total From (\n" +
        "      select co.id, vat_rate, (oi.price * oi.quantity) item_subtotal\n" +
        "      from dryhomecrm.customer_order co , dryhomecrm.order_item oi\n" +
        "      where order_date > '2020-01-01' and co.id = oi.customer_order_id ) sub\n" +
        "    group by id) sub2;")
    OrderSummary getSummary();
}

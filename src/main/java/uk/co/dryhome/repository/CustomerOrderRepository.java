package uk.co.dryhome.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.domain.CustomerOrderSums;
import uk.co.dryhome.domain.enumeration.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the CustomerOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {
    Page<CustomerOrder> findByOrderDateBetweenAndStatusIn(LocalDate from, LocalDate to, List<OrderStatus> statuses, Pageable pageable);

    @Query("SELECT sum(co.subTotal) as subTotal, sum(co.vatAmount) as vatAmount, sum(total) as total, count(co.id) as count " +
        "from CustomerOrder co where co.orderDate between ?1 and ?2" +
        " and co.status in ?3")
    List<CustomerOrderSums> sumAmountsByOrderDateBetween(LocalDate from, LocalDate to, List<OrderStatus> statuses);
}

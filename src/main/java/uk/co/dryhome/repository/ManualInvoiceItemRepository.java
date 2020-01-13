package uk.co.dryhome.repository;

import uk.co.dryhome.domain.ManualInvoiceItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ManualInvoiceItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualInvoiceItemRepository extends JpaRepository<ManualInvoiceItem, Long> {
    List<ManualInvoiceItem> findByManualInvoiceIdOrderById(Long manualInvoiceId);
    int deleteByManualInvoiceId(Long manualInvoiceId);

}

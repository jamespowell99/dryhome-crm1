package uk.co.dryhome.repository;

import uk.co.dryhome.domain.ManualInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ManualInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualInvoiceRepository extends JpaRepository<ManualInvoice, Long> {

}

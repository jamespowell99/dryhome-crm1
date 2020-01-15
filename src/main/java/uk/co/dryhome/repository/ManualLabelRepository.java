package uk.co.dryhome.repository;

import uk.co.dryhome.domain.ManualLabel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ManualLabel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualLabelRepository extends JpaRepository<ManualLabel, Long> {

}

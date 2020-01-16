package uk.co.dryhome.service.mapper;

import org.junit.Test;
import uk.co.dryhome.domain.ManualInvoice;
import uk.co.dryhome.domain.ManualInvoiceItem;
import uk.co.dryhome.service.dto.ManualInvoiceItemDTO;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ManualInvoiceMapperTest {
    @Test
    public void test() {
        ManualInvoiceItem item = new ManualInvoiceItem();
        item.setId(99L);
        item.setProduct("some prod");
        item.setQuantity(9);
        item.setPrice(new BigDecimal("9.99"));
        ManualInvoice manualInvoice = new ManualInvoice();
        manualInvoice.setId(98L);
        item.setManualInvoice(manualInvoice);

        ManualInvoiceItemDTO manualInvoiceItemDTO = new ManualInvoiceMapperImpl().itemToDto(item);

        assertEquals("some prod", manualInvoiceItemDTO.getProduct());
    }

}

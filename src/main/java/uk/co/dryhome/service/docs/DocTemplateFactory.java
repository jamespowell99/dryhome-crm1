package uk.co.dryhome.service.docs;

import uk.co.dryhome.domain.ManualLabel;

import java.util.Arrays;

public class DocTemplateFactory {
    public static DocTemplate fromTemplateName(Class docTemplateClass, String templateName) {

        DocTemplate[] values;
        if (docTemplateClass.equals(CustomerDocTemplate.class)) {
            values = CustomerDocTemplate.values();
        } else if (docTemplateClass.equals(CustomerOrderDocTemplate.class)) {
            values = CustomerOrderDocTemplate.values();
        } else if (docTemplateClass.equals(ManualInvoiceDocTemplate.class)) {
            values = ManualInvoiceDocTemplate.values();
        } else if (docTemplateClass.equals(ManualLabelsDocTemplate.class)) {
            values = ManualLabelsDocTemplate.values();
        } else {
            throw new RuntimeException("unrecognised type: " + docTemplateClass);
        }

        return Arrays.stream(values)
            .filter( n -> n.getTemplateName().equals(templateName)).findFirst()
            .orElseThrow(() -> new RuntimeException("templateName not found: " + templateName + " in " + docTemplateClass));
    }
}

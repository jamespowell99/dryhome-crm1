package uk.co.dryhome.service.docs;

import java.util.Arrays;

public enum CustomerDocTemplate implements DocTemplate {
    DP_RECORD("1ogLfDXyXpZjPMVokcJ-WHlJl2owQtnlAI_HNN8ruwNU", "dp-record"),
    LABELS("1QuxZoVdzQTAjqFXnXOMyAj3TNWDfN8K_8wG5_7sfcdg", "labels"),
    REMCON_PROD_LIT("1o-wqBqBI_i_UQubIjyElaFZh4G9C1v8QOtX4h4MGFWc", "remcon-prod-lit");

    CustomerDocTemplate(String templateId, String templateName) {
        this.templateId = templateId;
        this.templateName = templateName;
    }

    private String templateId;
    private String templateName;

    @Override
    public String getTemplateId() {
        return templateId;
    }

    @Override
    public String getTemplateName() {
        return templateName;
    }

}

package uk.co.dryhome.service.docs;

public enum ManualLabelsDocTemplate implements DocTemplate{
    LABELS("1QuxZoVdzQTAjqFXnXOMyAj3TNWDfN8K_8wG5_7sfcdg", "labels");

    ManualLabelsDocTemplate(String templateId, String templateName) {
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

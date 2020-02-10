package uk.co.dryhome.service.docs;

public enum CustomerOrderDocTemplate implements DocTemplate {
    CUSTOMER_INVOICE("1ImiHYF0dWNL0hY73TGIIzaeCZ0BItfl9poE2kRIacvw", "customer-invoice");

    CustomerOrderDocTemplate(String templateId, String templateName) {
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

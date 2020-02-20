package uk.co.dryhome.service.docs;

public enum ManualInvoiceDocTemplate implements DocTemplate{
    CUSTOMER_INVOICE("1ImiHYF0dWNL0hY73TGIIzaeCZ0BItfl9poE2kRIacvw", "customer-invoice"),
    ACCOUNTANT_INVOICE("does-not-exist-yet", "accountant-invoice"),
    FILE_INVOICE("does-not-exist-yet", "file-invoice");

    ManualInvoiceDocTemplate(String templateId, String templateName) {
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

package uk.co.dryhome.service;

import javax.servlet.http.HttpServletResponse;

public interface MergeDocSourceService {
    void createDocument(Long id, HttpServletResponse response, String templateName, DocPrintType docPrintType);
}

package uk.co.dryhome.service;

import uk.co.dryhome.domain.MergeDocumentSource;

import javax.servlet.http.HttpServletResponse;

public interface MergeDocSourceService {
    void createDocument(Long id, HttpServletResponse response, String documentName);
    }

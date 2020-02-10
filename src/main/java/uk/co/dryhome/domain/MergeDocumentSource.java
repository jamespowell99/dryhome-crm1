package uk.co.dryhome.domain;

import uk.co.dryhome.service.docs.DocTemplate;

import java.util.Map;

public interface MergeDocumentSource {
    Map<String, String> documentMappings();
    String getMergeDocPrefix(DocTemplate docTemplate);
}

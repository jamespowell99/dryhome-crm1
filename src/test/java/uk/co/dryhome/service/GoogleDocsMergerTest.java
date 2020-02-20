package uk.co.dryhome.service;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.dryhome.config.DryhomeProperties;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GoogleDocsMergerTest {
    private static final String TEMPLATE_DOCUMENT_ID = "1ogLfDXyXpZjPMVokcJ-WHlJl2owQtnlAI_HNN8ruwNU";
    private GoogleDocsMerger googleDocsMerger = new GoogleDocsMerger(getProperties());

    private DryhomeProperties getProperties() {
        DryhomeProperties dryhomeProperties = new DryhomeProperties();
        DryhomeProperties.MergeDocs mergeDocs = new DryhomeProperties.MergeDocs();
        mergeDocs.setGoogleCredsFileLocation("/Users/james/dev/dryhome/creds.json");
        dryhomeProperties.setMergeDocs(mergeDocs);
        return dryhomeProperties;
    }

    ;

    @Test
    @Ignore
    public void test() throws IOException {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("companyId", "1234");
        mappings.put("companyName", "Powtech Consulting Ltd");
        mappings.put("notes", "Some very \nlong notes \n\n\n which are on multiple lines");
        mappings.put("other", "other");

        String documentId = googleDocsMerger.merge(TEMPLATE_DOCUMENT_ID, "dp-detail", mappings);

        byte[] bytes = googleDocsMerger.getDocument(documentId);

        File outputFile = new File("dp-details_out_" + System.currentTimeMillis() + ".pdf");
        FileUtils.writeByteArrayToFile(outputFile, bytes);
        System.out.println("Wrote to " + outputFile);
    }
}

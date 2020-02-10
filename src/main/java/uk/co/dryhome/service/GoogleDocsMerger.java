package uk.co.dryhome.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.BatchUpdateDocumentRequest;
import com.google.api.services.docs.v1.model.ReplaceAllTextRequest;
import com.google.api.services.docs.v1.model.Request;
import com.google.api.services.docs.v1.model.SubstringMatchCriteria;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//todo improve this - can be way more performant
@Service
public class GoogleDocsMerger {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleDocsMerger.class);

    private static final String MERGE_RESULTS_LOCATION = "1T__9GbH4z9Nw3hQHEE3n2t8vLtd4YzLt";
    private static final String CREDENTIALS_FILE_PATH = "/Users/james/dev/dryhome/creds.json";


    public String merge(String templateDocumentId, String documentPrefix, Map<String, String> mappings) {
        LOG.info("merge start");

        // Build a new authorized API client service.
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Credential credentials = getCredentials();
            Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            String copyTitle = documentPrefix + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            File copyMetadata = new File().setName(copyTitle).setParents(Collections.singletonList(MERGE_RESULTS_LOCATION));
            LOG.info("copying file");
            File documentCopyFile =
                    driveService.files().copy(templateDocumentId, copyMetadata).execute();
            String documentCopyId = documentCopyFile.getId();

            List<Request> requests = new ArrayList<>();
            mappings.keySet().forEach(k -> {
                requests.add(new Request()
                        .setReplaceAllText(new ReplaceAllTextRequest()
                                .setContainsText(new SubstringMatchCriteria()
                                        .setText("${" + k + "}")
                                        .setMatchCase(true))
                                .setReplaceText(mappings.get(k))));
            });


            LOG.info("starting batch update");

            BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();
            service.documents().batchUpdate(documentCopyId, body.setRequests(requests)).execute();

            return documentCopyId;

        } catch (Exception e) {
            throw new RuntimeException("problem merging", e);
        }
    }

    private static final String APPLICATION_NAME = "DryhomeCRM";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static Credential getCredentials() throws IOException {
        //todo deprecated?
        LOG.info("getting creds");
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(SCOPES);
        return credential;
    }

    public byte[] getDocument(String documentId) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            LOG.info("downloading doc");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            driveService.files().export(documentId, "application/pdf")
                    .executeMediaAndDownloadTo(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("problem downloading", e);
        }
    }
}

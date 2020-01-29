package uk.co.dryhome.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.powtechconsulting.mailmerge.WordMerger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import uk.co.dryhome.config.DryhomeProperties;
import uk.co.dryhome.domain.MergeDocumentSource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MergeDocService {
    private final DryhomeProperties properties;

    public void generateDocument(String documentName, HttpServletResponse response, Set<String> allowedDocuments, MergeDocumentSource source) {
        if (!allowedDocuments.contains(documentName)) {
            throw new RuntimeException("unrecognised document name: " + documentName);
        }
        Map<String, String> mappings = source.documentMappings();
        String filename = documentName + ".docx";
        log.info("Merging doc {}. Values: {}", filename, mappings);
        byte[] document = new WordMerger().merge(getFile(filename), mappings);

        try {
            // get your file as InputStream
            InputStream is = new ByteArrayInputStream(document);
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream. Filename was '{}'", documentName, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    private String getFile(String filename) {
        //todo clean this all up
        //todo use input streams instead. Or tmp file at least?
        if (!new File("/tmp").exists()) {
            new File("/tmp").mkdir();
        }
        String fullFilename = "/tmp/" + filename;
        if (new File(fullFilename).exists()) {
            log.info("file {} already exists", filename);
            return fullFilename;
        }
        log.info("file {} does not exist, getting from S3", filename);
        DryhomeProperties.MergeDocs config = properties.getMergeDocs();
        Regions clientRegion = Regions.fromName(config.getS3Region());
        String bucketName = config.getS3BucketName();

        S3Object fullObject = null;
        try {
            BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                config.getAwsAccessKey(),
                config.getAwsSecretKey(), null);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .build();

            // Get an object and print its contents.
            System.out.println("Downloading an object");
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, filename));
            System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
            System.out.println("Content: ");


            File targetFile = new File(fullFilename);

            try {
                FileUtils.copyInputStreamToFile(fullObject.getObjectContent(), targetFile);

                return targetFile.getPath();
            } catch (IOException e) {
                throw new RuntimeException("problem writing file", e);
            }

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            throw new RuntimeException("AmazonServiceException: ", e);
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            throw new RuntimeException("SdkClientException: ", e);

        } finally {
            // To ensure that the network connection doesn't remain open, close any open input streams.
            try {
                if (fullObject != null) {
                    fullObject.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("problen in finally", e);
            }
        }
    }
}

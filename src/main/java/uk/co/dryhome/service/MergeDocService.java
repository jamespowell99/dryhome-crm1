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
import com.google.common.base.Preconditions;
import com.powtechconsulting.mailmerge.WordMerger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.h2.command.dml.Merge;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import uk.co.dryhome.config.DryhomeProperties;
import uk.co.dryhome.domain.MergeDocumentSource;
import uk.co.dryhome.service.docs.CustomerDocTemplate;
import uk.co.dryhome.service.docs.DocTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MergeDocService {
    private final DryhomeProperties properties;
    private final GoogleDocsMerger googleDocsMerger;

    public void generateDocument(DocTemplate template, DocPrintType docPrintType, HttpServletResponse response, MergeDocumentSource source) {
        if (docPrintType == DocPrintType.DOCX) {
            generateDocViaWord(template, response, source);
        } else {
            generateDocViaGoogle(template, response, source);
        }
    }

    private void generateDocViaWord(DocTemplate template, HttpServletResponse response,MergeDocumentSource source) {
        String filename = template.getTemplateName() + ".docx";
        log.debug("Merging doc {}. Values: {}", filename, source.documentMappings());
        byte[] document = new WordMerger().merge(getFile(filename), source.documentMappings());

        try {
            // get your file as InputStream
            InputStream is = new ByteArrayInputStream(document);
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream: " + template, ex);
        }
    }

    private void generateDocViaGoogle(DocTemplate template, HttpServletResponse response, MergeDocumentSource source) {
        String newDocumentId = googleDocsMerger.merge(template.getTemplateId(), source.getMergeDocPrefix(template), source.documentMappings());
        byte[] document = googleDocsMerger.getDocument(newDocumentId);

        try {
            InputStream is = new ByteArrayInputStream(document);
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream: " + template, ex);
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

package uk.co.dryhome.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(
    prefix = "dryhome",
    ignoreUnknownFields = false
)
@Data
@Validated
public class DryhomeProperties {
   @NotNull
   @Valid
    private MergeDocs mergeDocs;

   @Getter
   @Setter
   public static class MergeDocs {
       @NotNull
       private String s3Region;
       @NotNull
       private String s3BucketName;
       @NotNull
       private String awsAccessKey;
       @NotNull
       private String awsSecretKey;
   }

}

package uk.gov.dft.bluebadge.webapp.citizen.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class FileUploader {

  private String fieldLabel;
  private String fieldName;
  private MultipartFile file;
  private String allowedFiles;
  private Boolean multipleFiles = false;
  private Boolean enableAjaxFileUploader = true;
}

package uk.gov.dft.bluebadge.webapp.citizen.service;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileCheckerService {

  private static final Set<String> IMAGE_MIME_TYPES =
      ImmutableSet.of("image/jpeg", "image/gif", "image/png");
  private static final String PDF_MIME_TYPE = "application/pdf";
  private static final Set<String> SUPPORTED_TYPES =
      ImmutableSet.<String>builder().addAll(IMAGE_MIME_TYPES).add(PDF_MIME_TYPE).build();

  public boolean isValidFile(String mimeType, InputStream inputStream) {
    if (!SUPPORTED_TYPES.contains(mimeType)) {
      log.error("Failed to verify file. Unsupported mime type: {}", mimeType);
      return false;
    }
    if (PDF_MIME_TYPE.equals(mimeType)) {
      return isPdfValid(inputStream);
    } else {
      return isImageValid(inputStream);
    }
  }

  private boolean isImageValid(InputStream inputStream) {
    try {
      BufferedImage bufferedImage = ImageIO.read(inputStream);
      if (null != bufferedImage) {
        return true;
      }
    } catch (Exception e) {
      log.info("Failed to read file: {}", e.toString());
    }
    return false;
  }

  private boolean isPdfValid(InputStream inputStream) {
    try (PDDocument pdDocument = PDDocument.load(inputStream)) {
      return true;
    } catch (IOException e) {
      log.info("Failed to read file: {}", e.toString());
    }

    return false;
  }
}

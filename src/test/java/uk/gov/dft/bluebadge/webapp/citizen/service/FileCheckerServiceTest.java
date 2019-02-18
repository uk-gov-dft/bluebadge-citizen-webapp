package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class FileCheckerServiceTest {
  FileCheckerService fileCheckerService = new FileCheckerService();

  @Test
  @SneakyThrows
  void isValidFile_validJpg() {
    ClassPathResource r = new ClassPathResource("testFiles/capture.jpg");
    assertThat(fileCheckerService.isValidFile("image/jpeg", r.getInputStream())).isTrue();
  }

  @Test
  @SneakyThrows
  void isValidFile_validGif() {
    ClassPathResource r = new ClassPathResource("testFiles/load.gif");
    assertThat(fileCheckerService.isValidFile("image/gif", r.getInputStream())).isTrue();
  }

  @Test
  @SneakyThrows
  void isValidFile_validPng() {
    ClassPathResource r = new ClassPathResource("testFiles/phone.png");
    assertThat(fileCheckerService.isValidFile("image/png", r.getInputStream())).isTrue();
  }

  @Test
  @SneakyThrows
  void isValidFile_validPdf() {
    ClassPathResource r = new ClassPathResource("testFiles/menu.pdf");
    assertThat(fileCheckerService.isValidFile("application/pdf", r.getInputStream())).isTrue();
  }

  @Test
  @SneakyThrows
  void isValidFile_invalidJpg() {
    ClassPathResource r = new ClassPathResource("testFiles/fake_image.jpg");
    assertThat(fileCheckerService.isValidFile("image/jpeg", r.getInputStream())).isFalse();
  }

  @Test
  @SneakyThrows
  void isValidFile_invalidPdf() {
    ClassPathResource r = new ClassPathResource("testFiles/fake_pdf.pdf");
    assertThat(fileCheckerService.isValidFile("application/pdf", r.getInputStream())).isFalse();
  }

  @Test
  @SneakyThrows
  void isValidFile_invalidMimeType() {
    ClassPathResource r = new ClassPathResource("testFiles/menu.pdf");
    assertThat(fileCheckerService.isValidFile("application/text", r.getInputStream())).isFalse();
  }

  @Test
  @SneakyThrows
  void isValidFile_wrongMimeTypeForFile() {
    ClassPathResource r = new ClassPathResource("testFiles/menu.pdf");
    assertThat(fileCheckerService.isValidFile("image/jpeg", r.getInputStream())).isFalse();
  }
}

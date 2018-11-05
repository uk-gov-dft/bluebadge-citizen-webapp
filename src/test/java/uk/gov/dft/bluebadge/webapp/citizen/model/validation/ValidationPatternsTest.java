package uk.gov.dft.bluebadge.webapp.citizen.model.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.model.validation.ValidationPatterns.NINO;
import static uk.gov.dft.bluebadge.webapp.citizen.model.validation.ValidationPatterns.NINO_CASE_INSENSITIVE;
import static uk.gov.dft.bluebadge.webapp.citizen.model.validation.ValidationPatterns.PERSON_NAME;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class ValidationPatternsTest {
  @Test
  public void personNameRegex() {
    Pattern pattern = Pattern.compile(PERSON_NAME);
    assertThat(pattern.matcher("").find()).isTrue();
    assertThat(pattern.matcher("Bob").find()).isTrue();
    assertThat(pattern.matcher("Bob.Jones").find()).isTrue();

    assertThat(pattern.matcher("123").find()).isFalse();
    assertThat(pattern.matcher("Bob 1").find()).isFalse();
    assertThat(pattern.matcher("Bob ,").find()).isFalse();
  }

  @Test
  public void ninoRegex() {
    Pattern pattern = Pattern.compile(NINO);
    assertThat(pattern.matcher("JA341288A").find()).isTrue();
    assertThat(pattern.matcher("JA 34 12 88 A").find()).isTrue();

    assertThat(pattern.matcher("123").find()).isFalse();
    assertThat(pattern.matcher("Bob 1").find()).isFalse();
    assertThat(pattern.matcher("Bob ,").find()).isFalse();
  }

  @Test
  public void ninoCaseInsensitiveRegex() {
    Pattern pattern = Pattern.compile(NINO_CASE_INSENSITIVE);
    assertThat(pattern.matcher("jr341288a").find()).isTrue();
    assertThat(pattern.matcher("jR341288B").find()).isTrue();
    assertThat(pattern.matcher("Je 34 12 88 a").find()).isTrue();
  }
}

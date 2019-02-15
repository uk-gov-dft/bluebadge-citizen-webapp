package uk.gov.dft.bluebadge.webapp.citizen.model.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.common.util.ValidationPattern.EMPTY_OR_PERSON_NAME;
import static uk.gov.dft.bluebadge.common.util.ValidationPattern.NINO_CASE_INSENSITIVE;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import uk.gov.dft.bluebadge.common.util.ValidationPattern;

class ValidationPatternsTest {
  @Test
  public void personNameRegex() {
    Pattern pattern = Pattern.compile(EMPTY_OR_PERSON_NAME);
    assertThat(pattern.matcher("").find()).isTrue();
    assertThat(pattern.matcher("Bob").find()).isTrue();
    assertThat(pattern.matcher("Bob.Jones").find()).isTrue();

    assertThat(pattern.matcher("123").find()).isFalse();
    assertThat(pattern.matcher("Bob 1").find()).isFalse();
    assertThat(pattern.matcher("Bob ,").find()).isFalse();
  }

  @Test
  public void ninoRegex() {
    Pattern pattern = Pattern.compile(ValidationPattern.NINO);
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

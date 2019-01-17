package uk.gov.dft.bluebadge.webapp.citizen.controllers.thdialect;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.spring5.util.DetailedError;

public class CitizenApplicationThymeleafUtilsTest {

  private CitizenApplicationThymeleafUtils citizenApplicationThymeleafUtils;

  private List<DetailedError> unordered;
  private DetailedError error1;
  private DetailedError error2;
  private DetailedError error3;

  @Before
  public void setUp() {
    citizenApplicationThymeleafUtils = new CitizenApplicationThymeleafUtils();
    error1 = new DetailedError("field1", "field1", null, "error field1");
    error2 = new DetailedError("field2", "field2", null, "error field2");
    error3 = new DetailedError("field3", "field3", null, "error field3");
    unordered = Lists.newArrayList(error2, error3, error1);
  }

  @Test
  public void sortDetailedErrors_shouldSortDetailedErrorsAccordingToOrder_WhenTheOrderIsNotEmpty() {
    List<String> order = Lists.newArrayList("field1", "field2", "field3");
    List<DetailedError> expectedOrderedDetailedErrors = Lists.newArrayList(error1, error2, error3);
    List<DetailedError> ordered =
        citizenApplicationThymeleafUtils.sortDetailedErrors(unordered, order);
    assertThat(ordered).isEqualTo(expectedOrderedDetailedErrors);
  }

  @Test
  public void sortDetailedErrors_shouldNotSortDetailedErrors_WhenTheOrderIsEmpty() {
    List<DetailedError> ordered =
        citizenApplicationThymeleafUtils.sortDetailedErrors(unordered, Lists.newArrayList());
    assertThat(ordered).isEqualTo(unordered);
  }

  @Test
  public void sortDetailedErrors_shouldNotSortDetailedErrors_WhenTheOrderIsNull() {
    List<DetailedError> ordered =
        citizenApplicationThymeleafUtils.sortDetailedErrors(unordered, null);
    assertThat(ordered).isEqualTo(unordered);
  }

  @Test
  public void
      sortDetailedErrors_shouldNotSortDetailedErrors_WhenTheOrderRefersToNonExistingFields() {
    List<String> order = Lists.newArrayList("field5", "field6", "field7");
    List<DetailedError> ordered =
        citizenApplicationThymeleafUtils.sortDetailedErrors(unordered, order);
    assertThat(ordered).isEqualTo(unordered);
  }
}

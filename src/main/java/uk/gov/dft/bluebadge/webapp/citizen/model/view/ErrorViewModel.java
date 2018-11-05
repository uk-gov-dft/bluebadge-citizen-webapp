package uk.gov.dft.bluebadge.webapp.citizen.model.view;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.util.Assert;

public class ErrorViewModel implements Serializable {

  private String title = "error.summary.title";
  private String description = null;

  public ErrorViewModel() {}

  public ErrorViewModel(String title) {
    setTitle(title);
  }

  public ErrorViewModel(String title, String description) {
    setTitle(title);
    setDescription(description);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    Assert.notNull(title, "title cannot be null");
    Assert.hasText(title, "title should have text");
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    Assert.notNull(description, "description cannot be null");
    Assert.hasText(description, "description should have text");
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ErrorViewModel that = (ErrorViewModel) o;
    return Objects.equals(title, that.title) && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {

    return Objects.hash(title, description);
  }

  @Override
  public String toString() {
    return "ErrorViewModel{"
        + "title='"
        + title
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }
}

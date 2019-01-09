package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;

public interface ArtifactForm {
  default JourneyArtifact getJourneyArtifact() {
    return null;
  }

  default List<JourneyArtifact> getJourneyArtifacts() {
    return null == getJourneyArtifact()
        ? Collections.emptyList()
        : ImmutableList.of(getJourneyArtifact());
  }

  default Boolean hasArtifacts() {
    return null != getJourneyArtifacts() && !getJourneyArtifacts().isEmpty();
  }
}

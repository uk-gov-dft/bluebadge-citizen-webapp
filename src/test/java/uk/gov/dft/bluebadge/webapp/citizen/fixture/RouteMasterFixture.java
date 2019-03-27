package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import uk.gov.dft.bluebadge.webapp.citizen.config.JourneyTaskConfig;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.JourneySpecification;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;

public class RouteMasterFixture {
  public static RouteMaster routeMaster() {
    return new RouteMaster(fullJourneySpec());
  }

  public static JourneySpecification fullJourneySpec() {
    JourneyTaskConfig config = new JourneyTaskConfig();
    return config.journeyBuilder();
  }
}

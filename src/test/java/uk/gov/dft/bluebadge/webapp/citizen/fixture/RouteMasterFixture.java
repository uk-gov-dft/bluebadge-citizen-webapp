package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.JourneyMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;

public class RouteMasterFixture {
  public static RouteMaster routeMaster() {
    return new RouteMaster(new JourneyMaster());
  }
}

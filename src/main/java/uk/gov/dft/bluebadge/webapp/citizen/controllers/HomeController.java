package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.ApplicationManagementApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Controller
public class HomeController {

  public static final String TEMPLTE_DECLARATION = "application-end/declaration";

  @Autowired
  ReferenceDataService referenceDataService;
  @Autowired
  ApplicationManagementApiClient applicationManagementApiClient;

  @GetMapping("/")
  public String showDeclaration() {
    List<ReferenceData> referenceDatas = referenceDataService.retrieveLocalAuthorities();
    referenceDatas.stream().forEach(System.out::println);

    return TEMPLTE_DECLARATION;
  }

  @GetMapping("/createApp")
  public String fakeAppCreate() {
    Application app = new Application();
    app.setApplicationId("bob");
    applicationManagementApiClient.createApplication(app);

    return TEMPLTE_DECLARATION;
  }
}

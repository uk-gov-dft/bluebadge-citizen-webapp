package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.ApplicationManagementApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Controller
public class HomeController {

  public static final String TEMPLATE_HOME = "home";

  @Autowired ReferenceDataService referenceDataService;
  @Autowired ApplicationManagementApiClient applicationManagementApiClient;

  @GetMapping("/")
  public String show() {
    List<ReferenceData> referenceDatas = referenceDataService.retrieveLocalAuthorities();
    referenceDatas.stream().forEach(System.out::println);

    return TEMPLATE_HOME;
  }

  @GetMapping("/createApp")
  public String fakeAppCreate() {
    Application app = Application.builder().applicationId("bob").build();
    applicationManagementApiClient.createApplication(app);

    return TEMPLATE_HOME;
  }
}

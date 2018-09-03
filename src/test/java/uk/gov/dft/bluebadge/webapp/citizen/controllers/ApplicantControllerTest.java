package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;

public class ApplicantControllerTest {

  private MockMvc mockMvc;
  private ApplicantController controller;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new ApplicantController();
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void showApplicant_ShouldDisplayTheApplicantTemplate() throws Exception {

    ApplicantForm formRequest = ApplicantForm.builder().build();

    ReferenceData yourself = new ReferenceData();
    ReferenceData someone = new ReferenceData();
    yourself.setShortCode(ApplicantType.YOURSELF.toString());
    yourself.setDescription("Yourself");
    someone.setShortCode(ApplicantType.SOMEONE_ELSE.toString());
    someone.setDescription("Someone else");

    List<ReferenceData> applicantOptions = Lists.newArrayList(yourself, someone);

    mockMvc
        .perform(get("/applicant"))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant"))
        .andExpect(model().attribute("applicantOptions", applicantOptions))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void submitApplicant_ShouldStoreApplicantFormIntoSessionAndDisplayNextPageInTheJourney()
      throws Exception {
    mockMvc
        .perform(post("/applicant")
                .param("applicantType", "YOURSELF")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/apply-for-a-badge/declaration"));
  }

  @Test
  public void submitApplicant_shouldDisplayValidationMessageWhenNoApplicantTypeIsSelected() throws Exception {
    mockMvc
        .perform(post("/applicant")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "applicantType", "NotNull"));
  }
}

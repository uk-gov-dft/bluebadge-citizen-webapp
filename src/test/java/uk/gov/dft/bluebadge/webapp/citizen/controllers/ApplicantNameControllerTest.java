package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.SerializationUtils;
import uk.gov.dft.bluebadge.webapp.citizen.FormObjectToParamMapper;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ApplicantNameControllerTest {

    private MockMvc mockMvc;
    private ApplicantNameController controller;
    private Journey journey;

    @Mock
    ApplicationManagementService appService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controller = new ApplicantNameController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(new StandaloneMvcTestViewResolver())
                .build();

        ApplicantForm applicantForm = ApplicantForm.builder()
                .applicantType(ApplicantType.YOURSELF.toString())
                .build();

        journey = new Journey();
        journey.setApplicantForm(applicantForm);
    }

    @Test
    public void showApplicantName_ShouldDisplayTemplate() throws Exception {

        ApplicantNameForm applicantNameForm = ApplicantNameForm.builder().build();

        mockMvc.perform(get("/name")
                .sessionAttr("JOURNEY", journey))
                .andExpect(status().isOk())
                .andExpect(view().name("applicant-name"))
                .andExpect(model().attribute("formRequest", applicantNameForm));
    }

    @Test
    public void ShowApplicantName_ShouldDisplayTemplate_WithPrePopulatedFormSessionValues() throws Exception {
        ApplicantNameForm sessionApplicantNameForm = ApplicantNameForm.builder()
                .fullName("John").hasBirthName(true).birthName("Doe").build();

        journey.setApplicantNameForm(sessionApplicantNameForm);
        mockMvc.perform(get("/name")
                .sessionAttr("JOURNEY", journey))
                .andExpect(status().isOk())
                .andExpect(view().name("applicant-name"))
                .andExpect(model().attribute("formRequest", sessionApplicantNameForm));
    }

    @Test
    public void submitApplicantName_ShouldMoveDirectApplicantToNextStepInJourney() throws Exception {
        ApplicantNameForm applicantNameForm = ApplicantNameForm.builder()
                .fullName("John").hasBirthName(false).build();

        mockMvc.perform(post("/name")
                .params(FormObjectToParamMapper.convert(applicantNameForm))
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
                .andExpect(status().isFound())
                .andExpect(model().attribute("formRequest", applicantNameForm))
                .andExpect(redirectedUrl("/health-conditions"));
    }

    @Test
    public void submitApplicationName_ShouldDisplayValidation_WhenMandatoryFieldsAreNotSet() throws Exception {
        ApplicantNameForm applicantNameForm = ApplicantNameForm.builder().build();

        mockMvc.perform(post("/name")
                .params(FormObjectToParamMapper.convert(applicantNameForm))
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
                .andExpect(status().isOk())
                .andExpect(model().attribute("formRequest", applicantNameForm))
                .andExpect(model().attributeHasFieldErrorCode("formRequest", "fullName", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("formRequest", "hasBirthName", "NotNull"));
    }

    @Test
    public void submitApplicationName_ShouldDisplayValidation_BirthNameIsNotSet() throws Exception {
        ApplicantNameForm applicantNameForm = ApplicantNameForm.builder()
                .fullName("John").hasBirthName(true).birthName("").build();

        mockMvc.perform(post("/name")
                .params(FormObjectToParamMapper.convert(applicantNameForm))
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
                .andExpect(status().isOk())
                .andExpect(model().attribute("formRequest", applicantNameForm))
                .andExpect(model().attributeHasFieldErrorCode("formRequest", "birthName", "field.birthName.NotBlank"));
    }

}

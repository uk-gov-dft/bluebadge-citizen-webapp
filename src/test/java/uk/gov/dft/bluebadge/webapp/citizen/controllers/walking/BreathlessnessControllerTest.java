package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.BreathlessnessForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.BREATHLESSNESS;

public class BreathlessnessControllerTest extends ControllerTestFixture<BreathlessnessController> {

    private static final String SUCCESS_URL = Mappings.URL_MOBILITY_AID_LIST;
    private static final String ERROR_URL = Mappings.URL_BREATHLESS + RouteMaster.ERROR_SUFFIX;

    @Before
    public void setup() {
        super.setup(new BreathlessnessController(new RouteMaster()));
        journey =
                JourneyFixture.getDefaultJourneyToStep(
                        StepDefinition.BREATHLESSNESS, EligibilityCodeField.WALKD, false);
    }

    @Override
    protected String getTemplateName() {
        return "walking/breathlessness";
    }

    @Override
    protected String getUrl() {
        return "/breathless";
    }

    @Override
    protected StepDefinition getStep() {
        return BREATHLESSNESS;
    }

    @Override
    protected EligibilityCodeField getEligibilityType() {
        return EligibilityCodeField.WALKD;
    }

    @Test
    public void show_ShouldDisplayTemplate() throws Exception {
        super.show_ShouldDisplayTemplate();
    }

    @Test
    public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
        super.show_shouldRedirect_whenJourneyNotSetup();
    }

    @Test
    public void submit_showRedirectToNextStepInJourney() throws Exception {
        mockMvc
                .perform(
                        post(getUrl())
                                .param("breathlessnessTypes", "UPHILL, KEEPUP")
                                .contentType("application/x-www-form-urlencoded")
                                .sessionAttr("JOURNEY", journey))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(SUCCESS_URL));
    }

    @Test
    public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
            throws Exception {
        BreathlessnessForm form = BreathlessnessForm.builder().build();

        mockMvc
                .perform(
                        post(getUrl())
                                .contentType("application/x-www-form-urlencoded")
                                .sessionAttr("JOURNEY", journey))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(ERROR_URL))
                .andExpect(flash().attribute("formRequest", form));
    }

    @Test
    public void submit_whenBlankFormSubmitted_thenShouldHaveNotEmptyErrorMessage() throws Exception {
        mockMvc
                .perform(
                        post(getUrl())
                                .contentType("application/x-www-form-urlencoded")
                                .sessionAttr("JOURNEY", journey))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ERROR_URL))
                .andExpect(
                        ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                                "breathlessnessTypes", "NotEmpty"));
    }

    @Test
    public void submit_whenOtherSelectedWithoutDescription_thenShouldHaveNotBlankErrorMessage() throws Exception {
        mockMvc
                .perform(
                        post(getUrl())
                                .param("breathlessnessTypes", "OTHER")
                                .contentType("application/x-www-form-urlencoded")
                                .sessionAttr("JOURNEY", journey))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ERROR_URL))
                .andExpect(
                        ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                                "breathlessnessOtherDescription", "NotBlank"));
    }
}
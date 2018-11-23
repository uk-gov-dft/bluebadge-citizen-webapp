package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.net.URL;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveIdentityForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService;

public class ProveIdentityControllerTest {

  private MockMvc mockMvc;

  private Journey journey;
  private static final String SUCCESS_URL = Mappings.URL_DECLARATIONS;
  private static final String ERROR_URL = Mappings.URL_PROVE_IDENTITY + RouteMaster.ERROR_SUFFIX;
  private ArtifactService artifactServiceMock;
  private URL signedUrl;
  private URL docUrl;

  @Before
  @SneakyThrows
  public void setup() {
    artifactServiceMock = mock(ArtifactService.class);
    ProveIdentityController controller =
        new ProveIdentityController(new RouteMaster(), artifactServiceMock);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.PROVE_IDENTITY);
    docUrl = new URL("http://test");
    signedUrl = new URL("http://testSigned");
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    mockMvc
        .perform(get("/prove-identity").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("prove-identity"))
        .andExpect(model().attribute("formRequest", ProveIdentityForm.builder().build()));
  }

  @Test
  public void show_givenExistingDoc_ShouldDisplayTemplateAndHaveLinkToDoc() throws Exception {
    JourneyArtifact journeyArtifact =
        JourneyArtifact.builder()
            .fileName("test.jpg")
            .type("image")
            .url(new URL("http://test"))
            .build();
    ProveIdentityForm existingForm =
        ProveIdentityForm.builder().journeyArtifact(journeyArtifact).build();
    journey.setFormForStep(existingForm);

    doAnswer(
            invocation -> {
              JourneyArtifact param = invocation.getArgument(0);
              param.setSignedUrl(signedUrl);
              return null;
            })
        .when(artifactServiceMock)
        .createAccessibleLinks(journeyArtifact);

    MvcResult mvcResult =
        mockMvc
            .perform(get("/prove-identity").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(view().name("prove-identity"))
            .andExpect(model().attribute("formRequest", existingForm))
            .andReturn();

    verify(artifactServiceMock, times(1)).createAccessibleLinks(journeyArtifact);
    ProveIdentityForm formRequest =
        (ProveIdentityForm) mvcResult.getModelAndView().getModel().get("formRequest");
    assertThat(formRequest.getJourneyArtifact()).isNotNull();
    assertThat(formRequest.getJourneyArtifact().getSignedUrl()).isEqualTo(signedUrl);
  }

  @Test
  public void onByPassLink_ShouldRedirectToSuccess() throws Exception {
    mockMvc
        .perform(get("/proveId-bypass").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void ajaxSubmit_givenSuccessUpload_thenArtifactInSession() throws Exception {
    JourneyArtifact journeyArtifact =
        JourneyArtifact.builder()
            .fileName("test.pdf")
            .url(docUrl)
            .signedUrl(signedUrl)
            .type("file")
            .build();
    when(artifactServiceMock.upload(any())).thenReturn(journeyArtifact);

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/prove-identity-ajax").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("success").value("true"))
        .andExpect(jsonPath("artifact.fileName").value("test.pdf"))
        .andExpect(jsonPath("artifact.url").value(docUrl.toString()))
        .andExpect(jsonPath("artifact.signedUrl").value(signedUrl.toString()));
    ProveIdentityForm form = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    assertThat(form.getJourneyArtifact()).isSameAs(journeyArtifact);
  }

  @Test
  public void ajaxSubmit_givenFailedUpload_thenErrorResponse() throws Exception {
    when(artifactServiceMock.upload(any())).thenThrow(new RuntimeException("Test"));

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(multipart("/prove-identity-ajax").file(mockMultifile))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("error").exists())
        .andExpect(jsonPath("artifact").doesNotExist());
  }

  @Test
  public void ajaxSubmit_givenNoDocument_thenBadRequest() throws Exception {
    mockMvc.perform(multipart("/prove-identity-ajax")).andExpect(status().isBadRequest());
  }

  @Test
  public void submit_GivenAlreadyUploadedDoc_thenShouldDisplayRedirectToSuccess() throws Exception {
    JourneyArtifact journeyArtifact =
        JourneyArtifact.builder()
            .fileName("test.jpg")
            .type("image")
            .url(new URL("http://test"))
            .build();
    journey.setFormForStep(ProveIdentityForm.builder().journeyArtifact(journeyArtifact).build());
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", (byte[]) null);

    mockMvc
        .perform(multipart("/prove-identity").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    ProveIdentityForm form = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    assertThat(form.getJourneyArtifact()).isSameAs(journeyArtifact);
  }

  @Test
  public void
      submit_GivenAlreadyUploadedDoc_whenSubmittedWithNewDoc_thenShouldDisplayRedirectToSuccessAndHaveNewArtifact()
          throws Exception {
    URL testUrl = new URL("http://test");
    JourneyArtifact journeyArtifact =
        JourneyArtifact.builder().fileName("test.jpg").type("image").url(testUrl).build();
    journey.setFormForStep(ProveIdentityForm.builder().journeyArtifact(journeyArtifact).build());
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", "test".getBytes());

    URL replacementUrl = new URL("http://test");
    JourneyArtifact replacingArtifact =
        JourneyArtifact.builder()
            .fileName("originalFile2.jpg")
            .type("image")
            .url(replacementUrl)
            .build();
    when(artifactServiceMock.upload(mockMultifile)).thenReturn(replacingArtifact);

    mockMvc
        .perform(multipart("/prove-identity").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    ProveIdentityForm form = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    JourneyArtifact sessionArtitfact = form.getJourneyArtifact();
    assertThat(sessionArtitfact).isNotSameAs(journeyArtifact);
    assertThat(sessionArtitfact.getUrl()).isEqualTo(replacementUrl);
    assertThat(sessionArtitfact.getType()).isEqualTo("image");
    assertThat(sessionArtitfact.getFileName()).isEqualTo("originalFile2.jpg");
  }

  @Test
  public void
      submit_GivenNoExistingDoc_whenSubmittedWithNewDoc_thenShouldDisplayRedirectToSuccessAndHaveNewArtifact()
          throws Exception {
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", "test".getBytes());

    URL replacementUrl = new URL("http://test");
    JourneyArtifact replacingArtifact =
        JourneyArtifact.builder()
            .fileName("originalFile2.jpg")
            .type("image")
            .url(replacementUrl)
            .build();
    when(artifactServiceMock.upload(mockMultifile)).thenReturn(replacingArtifact);

    mockMvc
        .perform(multipart("/prove-identity").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    ProveIdentityForm form = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    JourneyArtifact sessionArtitfact = form.getJourneyArtifact();
    assertThat(sessionArtitfact.getUrl()).isEqualTo(replacementUrl);
    assertThat(sessionArtitfact.getType()).isEqualTo("image");
    assertThat(sessionArtitfact.getFileName()).isEqualTo("originalFile2.jpg");
  }

  @Test
  public void submit_givenNoDocument_thenBadRequest() throws Exception {
    mockMvc.perform(multipart("/prove-identity")).andExpect(status().isBadRequest());
  }

  @Test
  public void submit_givenNoSessionDoc_whenSubmitWithNoNewDoc_thenValidationError()
      throws Exception {
    MockMultipartFile mockMultifile = new MockMultipartFile("document", "", "", (byte[]) null);

    mockMvc
        .perform(multipart("/prove-identity").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(ERROR_URL));

    ProveIdentityForm form = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    assertThat(form.getJourneyArtifact()).isNull();
  }
}

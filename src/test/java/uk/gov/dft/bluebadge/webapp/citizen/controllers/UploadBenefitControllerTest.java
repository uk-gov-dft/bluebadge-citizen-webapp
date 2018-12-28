package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
import static uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService.IMAGE_PDF_MIME_TYPES;

import com.google.common.collect.Lists;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.UploadBenefitForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService;

public class UploadBenefitControllerTest {

  private MockMvc mockMvc;

  private Journey journey;
  private static final String SUCCESS_URL = Mappings.URL_PROVE_IDENTITY;
  private static final String ERROR_URL = Mappings.URL_UPLOAD_BENEFIT + RouteMaster.ERROR_SUFFIX;
  private ArtifactService artifactServiceMock;
  private URL signedUrl;
  private URL docUrl;

  @Before
  @SneakyThrows
  public void setup() {
    artifactServiceMock = mock(ArtifactService.class);
    UploadBenefitController controller =
        new UploadBenefitController(new RouteMaster(), artifactServiceMock);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.UPLOAD_BENEFIT);
    journey.setFormForStep(UploadBenefitForm.builder().build());
    docUrl = new URL("http://test");
    signedUrl = new URL("http://testSigned");
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    mockMvc
        .perform(get("/upload-benefit").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("upload-benefit"))
        .andExpect(model().attribute("formRequest", UploadBenefitForm.builder().build()));
  }

  @Test
  public void show_givenExistingDoc_ShouldDisplayTemplateAndHaveLinkToDoc() throws Exception {
    JourneyArtifact journeyArtifact = testArtifactBuilder().fileName("test.jpg").build();

    UploadBenefitForm existingForm =
        UploadBenefitForm.builder().journeyArtifacts(Lists.newArrayList(journeyArtifact)).build();
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
            .perform(get("/upload-benefit").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(view().name("upload-benefit"))
            .andExpect(model().attribute("formRequest", existingForm))
            .andReturn();

    verify(artifactServiceMock, times(1)).createAccessibleLinks(journeyArtifact);
    UploadBenefitForm formRequest =
        (UploadBenefitForm) mvcResult.getModelAndView().getModel().get("formRequest");
    assertThat(formRequest.getJourneyArtifacts()).isNotEmpty();
    assertThat(formRequest.getJourneyArtifacts()).extracting("signedUrl").containsOnly(signedUrl);
  }

  private JourneyArtifact.JourneyArtifactBuilder testArtifactBuilder()
      throws MalformedURLException {
    return JourneyArtifact.builder()
        .fileName("abc.jpg")
        .type("image")
        .url(new URL("http://test/abc"));
  }

  @Test
  public void onByPassLink_ShouldRedirectToSuccess() throws Exception {
    mockMvc
        .perform(get("/upload-benefit-bypass").sessionAttr("JOURNEY", journey))
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
    when(artifactServiceMock.upload(anyList(), any()))
        .thenReturn(Lists.newArrayList(journeyArtifact));

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-benefit-ajax").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("success").value("true"))
        .andExpect(jsonPath("artifact[0].fileName").value("test.pdf"))
        .andExpect(jsonPath("artifact[0].url").value(docUrl.toString()))
        .andExpect(jsonPath("artifact[0].signedUrl").value(signedUrl.toString()));
    UploadBenefitForm form = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
    assertThat(form.getJourneyArtifacts()).containsOnly(journeyArtifact);
  }

  @Test
  public void ajaxSubmit_givenSuccessMultipleUpload_thenArtifactInSession() throws Exception {
    JourneyArtifact journeyArtifact1 =
        testArtifactBuilder().fileName("test1.pdf").signedUrl(signedUrl).build();
    JourneyArtifact journeyArtifact2 =
        testArtifactBuilder().fileName("test2.pdf").signedUrl(signedUrl).build();
    when(artifactServiceMock.upload(anyList(), any()))
        .thenReturn(Lists.newArrayList(journeyArtifact1, journeyArtifact2));

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile1 =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());
    MockMultipartFile mockMultifile2 =
        new MockMultipartFile("document", "originalFile2.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-benefit-ajax")
                .file(mockMultifile1)
                .file(mockMultifile2)
                .sessionAttr("JOURNEY", journey))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("success").value("true"))
        .andExpect(jsonPath("artifact[0].fileName").value("test1.pdf"))
        .andExpect(jsonPath("artifact[0].url").value(journeyArtifact1.getUrl().toString()))
        .andExpect(jsonPath("artifact[0].signedUrl").value(signedUrl.toString()))
        .andExpect(jsonPath("artifact[1].fileName").value("test2.pdf"))
        .andExpect(jsonPath("artifact[1].url").value(journeyArtifact2.getUrl().toString()))
        .andExpect(jsonPath("artifact[1].signedUrl").value(signedUrl.toString()));
    UploadBenefitForm form = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
    assertThat(form.getJourneyArtifacts()).containsExactly(journeyArtifact1, journeyArtifact2);
  }

  @Test
  public void ajaxSubmit_givenSuccessUpload_andExistingArtifact_thenAdditionalArtifactInSession()
      throws Exception {
    JourneyArtifact existingArtifact = addArtifactToJourney("test.jpg");
    JourneyArtifact journeyArtifact =
        testArtifactBuilder().fileName("test.pdf").type("file").signedUrl(signedUrl).build();
    when(artifactServiceMock.upload(anyList(), any()))
        .thenReturn(Lists.newArrayList(journeyArtifact));

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-benefit-ajax").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("success").value("true"))
        .andExpect(jsonPath("artifact[0].fileName").value("test.pdf"))
        .andExpect(jsonPath("artifact[0].url").value(journeyArtifact.getUrl().toString()))
        .andExpect(jsonPath("artifact[0].signedUrl").value(signedUrl.toString()));
    UploadBenefitForm form = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
    assertThat(form.getJourneyArtifacts()).containsOnly(existingArtifact, journeyArtifact);
  }

  @Test
  public void
      ajaxSubmit_givenSuccessUpload_andExistingArtifact_whenClearRequested_thenOnlyNewArtifactInSession()
          throws Exception {
    addArtifactToJourney("test.jpg");
    JourneyArtifact journeyArtifact =
        testArtifactBuilder().fileName("test.pdf").type("file").signedUrl(signedUrl).build();
    when(artifactServiceMock.upload(anyList(), any()))
        .thenReturn(Lists.newArrayList(journeyArtifact));

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-benefit-ajax")
                .file(mockMultifile)
                .param("clear", "true")
                .sessionAttr("JOURNEY", journey))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("success").value("true"))
        .andExpect(jsonPath("artifact[0].fileName").value("test.pdf"))
        .andExpect(jsonPath("artifact[0].url").value(journeyArtifact.getUrl().toString()))
        .andExpect(jsonPath("artifact[0].signedUrl").value(signedUrl.toString()));
    UploadBenefitForm form = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
    assertThat(form.getJourneyArtifacts()).containsOnly(journeyArtifact);
  }

  @Test
  public void ajaxSubmit_givenFailedUpload_thenErrorResponse() throws Exception {
    when(artifactServiceMock.upload(anyList(), any())).thenThrow(new RuntimeException("Test"));

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(multipart("/upload-benefit-ajax").file(mockMultifile))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("error").exists())
        .andExpect(jsonPath("artifact").doesNotExist());
  }

  @Test
  public void
      submit_GivenAlreadyUploadedDoc_whenSubmittedWithoutADoc_thenShouldDisplayRedirectToSuccess()
          throws Exception {
    JourneyArtifact journeyArtifact = testArtifactBuilder().fileName("test.jpg").build();
    journey.setFormForStep(
        UploadBenefitForm.builder().journeyArtifacts(Lists.newArrayList(journeyArtifact)).build());

    mockMvc
        .perform(multipart("/upload-benefit").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    UploadBenefitForm form = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
    assertThat(form.getJourneyArtifacts()).containsOnly(journeyArtifact);
  }

  @Test
  public void
      submit_GivenAlreadyUploadedDoc_whenSubmittedWithNewDoc_thenShouldDisplayRedirectToSuccessAndHaveNewArtifact()
          throws Exception {
    JourneyArtifact journeyArtifact = addArtifactToJourney("test.jpg");
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", "test".getBytes());

    URL replacementUrl = new URL("http://test");
    JourneyArtifact replacingArtifact =
        JourneyArtifact.builder()
            .fileName("originalFile2.jpg")
            .type("image")
            .url(replacementUrl)
            .build();
    when(artifactServiceMock.upload(Lists.newArrayList(mockMultifile), IMAGE_PDF_MIME_TYPES))
        .thenReturn(Lists.newArrayList(replacingArtifact));

    mockMvc
        .perform(multipart("/upload-benefit").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    UploadBenefitForm form = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
    List<JourneyArtifact> sessionArtitfacts = form.getJourneyArtifacts();
    assertThat(sessionArtitfacts).containsOnly(replacingArtifact);
    JourneyArtifact sessionArtitfact = sessionArtitfacts.get(0);
    assertThat(sessionArtitfact).isNotSameAs(journeyArtifact);
    assertThat(sessionArtitfact.getUrl()).isEqualTo(replacementUrl);
    assertThat(sessionArtitfact.getType()).isEqualTo("image");
    assertThat(sessionArtitfact.getFileName()).isEqualTo("originalFile2.jpg");
  }

  private JourneyArtifact addArtifactToJourney(String fileName) throws MalformedURLException {
    JourneyArtifact journeyArtifact = testArtifactBuilder().fileName(fileName).build();
    journey.setFormForStep(
        UploadBenefitForm.builder().journeyArtifacts(Lists.newArrayList(journeyArtifact)).build());
    return journeyArtifact;
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
    when(artifactServiceMock.upload(Lists.newArrayList(mockMultifile), IMAGE_PDF_MIME_TYPES))
        .thenReturn(Lists.newArrayList(replacingArtifact));

    mockMvc
        .perform(multipart("/upload-benefit").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    UploadBenefitForm form = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
    List<JourneyArtifact> sessionArtitfacts = form.getJourneyArtifacts();
    assertThat(sessionArtitfacts).containsOnlyOnce(replacingArtifact);
    JourneyArtifact sessionArtitfact = sessionArtitfacts.get(0);
    assertThat(sessionArtitfact.getUrl()).isEqualTo(replacementUrl);
    assertThat(sessionArtitfact.getType()).isEqualTo("image");
    assertThat(sessionArtitfact.getFileName()).isEqualTo("originalFile2.jpg");
  }

  @Test
  public void submit_givenNoSessionDoc_whenSubmitWithNoNewDoc_thenValidationError()
      throws Exception {
    MockMultipartFile mockMultifile = new MockMultipartFile("document", "", "", (byte[]) null);

    mockMvc
        .perform(multipart("/upload-benefit").file(mockMultifile).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(ERROR_URL));

    UploadBenefitForm form = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
    assertThat(form.getJourneyArtifact()).isNull();
  }
}

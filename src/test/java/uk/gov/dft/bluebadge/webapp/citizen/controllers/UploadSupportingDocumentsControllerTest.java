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
import static uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService.IMAGE_PDF_MIME_TYPES;

import com.google.common.collect.Lists;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.UploadSupportingDocumentsForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService;

public class UploadSupportingDocumentsControllerTest {

  private MockMvc mockMvc;

  private Journey journey;
  private static final String SUCCESS_URL = Mappings.URL_TREATMENT_LIST;
  private static final String ERROR_URL =
      Mappings.URL_UPLOAD_SUPPORTING_DOCUMENTS + RouteMaster.ERROR_SUFFIX;
  private ArtifactService artifactServiceMock;
  private URL signedUrl;
  private URL docUrl;

  // private URL artifactUrl;
  // private JourneyArtifact journeyArtifact;
  private UploadSupportingDocumentsForm form;

  @Before
  @SneakyThrows
  public void setup() throws MalformedURLException {
    artifactServiceMock = mock(ArtifactService.class);
    UploadSupportingDocumentsController controller =
        new UploadSupportingDocumentsController(new RouteMaster(), artifactServiceMock);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    /*
        artifactUrl = new URL("http", "localhost", 8080, "file");
        journeyArtifact = JourneyArtifact.builder().fileName("filename").type("type").url(artifactUrl).build();
        form = UploadSupportingDocumentsForm.builder().hasDocuments(true)
          .journeyArtifact(journeyArtifact).build();
    */
    //  artifactUrl = new URL("http", "localhost", 8080, "file");
    // journeyArtifact =
    // JourneyArtifact.builder().fileName("filename").type("type").url(artifactUrl).build();
    form = UploadSupportingDocumentsForm.builder().build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS, EligibilityCodeField.WALKD);
    journey.setFormForStep(form);
    docUrl = new URL("http://test");
    signedUrl = new URL("http://testSigned");
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    mockMvc
        .perform(get("/upload-supporting-documents").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("upload-supporting-documents"))
        .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void show_givenExistingDoc_ShouldDisplayTemplateAndHaveLinkToDoc() throws Exception {
    JourneyArtifact journeyArtifact =
        JourneyArtifact.builder()
            .fileName("test.jpg")
            .type("image")
            .url(new URL("http://test"))
            .build();
    UploadSupportingDocumentsForm existingForm =
        UploadSupportingDocumentsForm.builder()
            .journeyArtifacts(Lists.newArrayList(journeyArtifact))
            .build();
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
            .perform(get("/upload-supporting-documents").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(view().name("upload-supporting-documents"))
            .andExpect(model().attribute("formRequest", existingForm))
            .andReturn();

    verify(artifactServiceMock, times(1)).createAccessibleLinks(journeyArtifact);
    UploadSupportingDocumentsForm formRequest =
        (UploadSupportingDocumentsForm) mvcResult.getModelAndView().getModel().get("formRequest");
    assertThat(formRequest.getJourneyArtifacts()).isNotEmpty();
    assertThat(formRequest.getJourneyArtifacts()).extracting("signedUrl").containsOnly(signedUrl);
  }

  @Test
  public void onByPassLink_ShouldRedirectToSuccess() throws Exception {
    mockMvc
        .perform(get("/upload-supporting-documents-bypass").sessionAttr("JOURNEY", journey))
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
    when(artifactServiceMock.upload(any(), any())).thenReturn(journeyArtifact);

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-supporting-documents-ajax")
                .file(mockMultifile)
                .sessionAttr("JOURNEY", journey))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("success").value("true"))
        .andExpect(jsonPath("artifact[0].fileName").value("test.pdf"))
        .andExpect(jsonPath("artifact[0].url").value(docUrl.toString()))
        .andExpect(jsonPath("artifact[0].signedUrl").value(signedUrl.toString()));
    UploadSupportingDocumentsForm form =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    assertThat(form.getJourneyArtifacts()).containsOnly(journeyArtifact);
  }

  @Test
  public void ajaxSubmit_givenSuccessMultipleUpload_thenArtifactInSession() throws Exception {
    JourneyArtifact journeyArtifact1 =
        testArtifactBuilder().fileName("test1.pdf").signedUrl(signedUrl).build();
    JourneyArtifact journeyArtifact2 =
        testArtifactBuilder().fileName("test2.pdf").signedUrl(signedUrl).build();
    when(artifactServiceMock.upload(any(), any())).thenReturn(journeyArtifact1, journeyArtifact2);

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile1 =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());
    MockMultipartFile mockMultifile2 =
        new MockMultipartFile("document", "originalFile2.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-supporting-documents-ajax")
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
    UploadSupportingDocumentsForm form =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    assertThat(form.getJourneyArtifacts()).containsExactly(journeyArtifact1, journeyArtifact2);
  }

  @Test
  public void ajaxSubmit_givenSuccessUpload_andExistingArtifact_thenAdditionalArtifactInSession()
      throws Exception {
    JourneyArtifact existingArtifact = addArtifactToJourney("test.jpg");
    JourneyArtifact journeyArtifact =
        testArtifactBuilder().fileName("test.pdf").type("file").signedUrl(signedUrl).build();
    when(artifactServiceMock.upload(any(), any())).thenReturn(journeyArtifact);
    UploadSupportingDocumentsForm form =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    form.setHasDocuments(true);

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-supporting-documents-ajax")
                .file(mockMultifile)
                .sessionAttr("JOURNEY", journey)
                .param("hasDocuments", "true"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("success").value("true"))
        .andExpect(jsonPath("artifact[0].fileName").value("test.pdf"))
        .andExpect(jsonPath("artifact[0].url").value(journeyArtifact.getUrl().toString()))
        .andExpect(jsonPath("artifact[0].signedUrl").value(signedUrl.toString()));
    UploadSupportingDocumentsForm newForm =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    assertThat(newForm.getJourneyArtifacts()).containsOnly(existingArtifact, journeyArtifact);
  }

  @Test
  public void
      ajaxSubmit_givenSuccessUpload_andExistingArtifact_whenClearRequested_thenOnlyNewArtifactInSession()
          throws Exception {
    addArtifactToJourney("test.jpg");
    JourneyArtifact journeyArtifact =
        testArtifactBuilder().fileName("test.pdf").type("file").signedUrl(signedUrl).build();
    when(artifactServiceMock.upload(any(), any())).thenReturn(journeyArtifact);
    UploadSupportingDocumentsForm form =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    form.setHasDocuments(true);

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-supporting-documents-ajax")
                .file(mockMultifile)
                .param("clear", "true")
                .param("hasDocuments", "true")
                .sessionAttr("JOURNEY", journey))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("success").value("true"))
        .andExpect(jsonPath("artifact[0].fileName").value("test.pdf"))
        .andExpect(jsonPath("artifact[0].url").value(journeyArtifact.getUrl().toString()))
        .andExpect(jsonPath("artifact[0].signedUrl").value(signedUrl.toString()));
    UploadSupportingDocumentsForm newForm =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    assertThat(newForm.getJourneyArtifacts()).containsOnly(journeyArtifact);
  }

  @Test
  public void ajaxSubmit_givenFailedUpload_thenErrorResponse() throws Exception {
    when(artifactServiceMock.upload(any(), any())).thenThrow(new RuntimeException("Test"));

    String testUpload = "Some thing to upload";
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", testUpload.getBytes());

    mockMvc
        .perform(
            multipart("/upload-supporting-documents-ajax")
                .file(mockMultifile)
                .param("hasDocuments", "true"))
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
        UploadSupportingDocumentsForm.builder()
            .hasDocuments(true)
            .journeyArtifacts(Lists.newArrayList(journeyArtifact))
            .build());

    mockMvc
        .perform(
            multipart("/upload-supporting-documents")
                .sessionAttr("JOURNEY", journey)
                .param("hasDocuments", "false"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    UploadSupportingDocumentsForm form =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    assertThat(form.getJourneyArtifacts()).isEmpty();
  }

  @Test
  @Ignore
  public void
      submit_GivenAlreadyUploadedDoc_whenSubmittedWithNewDoc_thenShouldDisplayRedirectToSuccessAndHaveNewArtifact()
          throws Exception {
    URL testUrl = new URL("http://test");
    JourneyArtifact journeyArtifact =
        JourneyArtifact.builder().fileName("test.jpg").type("image").url(testUrl).build();
    journey.setFormForStep(
        UploadSupportingDocumentsForm.builder()
            .journeyArtifacts(Lists.newArrayList(journeyArtifact))
            .build());
    MockMultipartFile mockMultifile =
        new MockMultipartFile("document", "originalFile.jpg", "text/plain", "test".getBytes());

    URL replacementUrl = new URL("http://test");
    JourneyArtifact replacingArtifact =
        JourneyArtifact.builder()
            .fileName("originalFile2.jpg")
            .type("image")
            .url(replacementUrl)
            .build();
    when(artifactServiceMock.upload(mockMultifile, IMAGE_PDF_MIME_TYPES))
        .thenReturn(replacingArtifact);

    mockMvc
        .perform(
            multipart("/upload-supporting-documents")
                .file(mockMultifile)
                .param("hasDocuments", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    UploadSupportingDocumentsForm form = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
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
    when(artifactServiceMock.upload(mockMultifile, IMAGE_PDF_MIME_TYPES))
        .thenReturn(replacingArtifact);

    mockMvc
        .perform(
            multipart("/upload-supporting-documents")
                .file(mockMultifile)
                .sessionAttr("JOURNEY", journey)
                .param("hasDocuments", "true"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    UploadSupportingDocumentsForm form =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    List<JourneyArtifact> sessionArtifacts = form.getJourneyArtifacts();
    assertThat(sessionArtifacts).containsOnlyOnce(replacingArtifact);
    JourneyArtifact sessionArtifact = sessionArtifacts.get(0);
    assertThat(sessionArtifact.getUrl()).isEqualTo(replacementUrl);
    assertThat(sessionArtifact.getType()).isEqualTo("image");
    assertThat(sessionArtifact.getFileName()).isEqualTo("originalFile2.jpg");
  }

  @Test
  public void submit_givenNoSessionDoc_whenSubmitWithNoNewDoc_thenValidationError()
      throws Exception {
    MockMultipartFile mockMultifile = new MockMultipartFile("document", "", "", (byte[]) null);

    mockMvc
        .perform(
            multipart("/upload-supporting-documents")
                .file(mockMultifile)
                .sessionAttr("JOURNEY", journey)
                .param("hasDocuments", "true"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(ERROR_URL));

    UploadSupportingDocumentsForm form =
        journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
    assertThat(form.getJourneyArtifact()).isNull();
  }

  private JourneyArtifact.JourneyArtifactBuilder testArtifactBuilder()
      throws MalformedURLException {
    return JourneyArtifact.builder()
        .fileName("abc.jpg")
        .type("image")
        .url(new URL("http://test/abc"));
  }

  private JourneyArtifact addArtifactToJourney(String fileName) throws MalformedURLException {
    JourneyArtifact journeyArtifact = testArtifactBuilder().fileName(fileName).build();
    journey.setFormForStep(
        UploadSupportingDocumentsForm.builder()
            .journeyArtifacts(Lists.newArrayList(journeyArtifact))
            .build());
    return journeyArtifact;
  }
}

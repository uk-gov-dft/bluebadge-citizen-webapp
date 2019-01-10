package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class SupportingDocumentsPage {

  public static final String PAGE_URL = "/upload-supporting-documents";
  public static final String PAGE_TITLE = "Supporting documents";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "Supporting documents";

  public static final String HEADER = "Supporting documents";
  public static final String HEADER_SOMEONE_ELSE = "Supporting documents";

  public static final String SUPPORTING_DOCUMENTS_YES_OPTION = "hasDocuments.yes";
  public static final String SUPPORTING_DOCUMENTS_NO_OPTION = "hasDocuments.no";

  public static final String VALIDATION_MESSAGE_FOR_NO_OPTION =
      "Select whether there are supporting documents to upload";

  public static final String VALIDATION_MESSAGE_FOR_YES_BUT_NO_UPLOAD =
      "Supporting documents are required if you answer yes";

  public static final String VALIDATION_MESSAGE_FOR_INVALID_UPLOAD =
      "You can upload a maximum of 15 files. Files needs to be less than 10MB and either a .JPEG, .PNG, .PDF or .GIF.";
}

package uk.gov.dft.bluebadge.webapp.citizen.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class FileUploader {
  @NonNull private final String fieldName;
  private String fieldLabel;

  private Boolean allowMultipleFileUploads;
  private String allowedFileTypes;
  private String maxFileSize;
  @NonNull private final String ajaxRequestUrl;

  private String errorTitleMessageKey;
  private String errorMessageKey;
  private String rejectErrorMessageKey;

  private String uploadButtonMobileMessageKey;
  private String uploadButtonDesktopMessageKey;
  private String multiFileCaptionMessageKey;
  private String previewTitleMessageKey;
  private String previewResetButtonMessageKey;
  private String addFileMessageKey;
  private String loadingMessageKey;

  public static class FileUploaderBuilder {
    private String fieldLabel = "fileUploader.field.label";
    private Boolean allowMultipleFileUploads = false;
    private String errorTitleMessageKey = "fileUploader.error.title";
    private String errorMessageKey = "fileUploader.error.content";
    private String rejectErrorMessageKey = "fileUploader.rejected.content";

    private String uploadButtonMobileMessageKey = "fileUploader.label.mobile";
    private String uploadButtonDesktopMessageKey = "fileUploader.label.desktop";
    private String multiFileCaptionMessageKey = "fileUploader.label.multiFile.caption";
    private String previewTitleMessageKey = "fileUploader.preview.title";
    private String previewResetButtonMessageKey = "fileUploader.preview.resetBtn";
    private String addFileMessageKey = "fileUploader.preview.addFileBtn";
    private String loadingMessageKey = "fileUploader.loaderText";
  }
}

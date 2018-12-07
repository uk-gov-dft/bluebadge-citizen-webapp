package uk.gov.dft.bluebadge.webapp.citizen.model;

import lombok.Builder;

@Builder
public class FileUploader {
  private String name;
  private String label;
  private Boolean allowMultipleFileUploads = false;
  private String allowedFileTypes;
  private String maxFileSize;

  private String generalErrorMessageKey = "";
  private String rejectErrorMessageKey = "";

  private String uploadButtonMobileMessageKey = "fileUploader.label.mobile";
  private String uploadButtonDesktopMessageKey = "fileUploader.label.desktop";
  private String multiFileCaptionMessageKey = "fileUploader.label.multiFile.caption";
  private String previewTitleMessageKey = "fileUploader.preview.title";
  private String previewResetButtonMessageKey = "fileUploader.preview.resetBtn";
  private String addFileMessageKey = "fileUploader.preview.addFileBtn";
  private String loadingMessageKey = "fileUploader.loaderText";
}

package uk.gov.dft.bluebadge.webapp.citizen.client.common;

import java.util.stream.Collectors;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

public class ClientApiException extends RuntimeException {
  final transient CommonResponse commonResponse;

  public ClientApiException(CommonResponse commonResponse) {
    super(extractMessage(commonResponse));
    this.commonResponse = commonResponse;
  }

  public CommonResponse getCommonResponse() {
    return commonResponse;
  }

  private static String extractMessage(CommonResponse cr){
    if(null == cr || null == cr.getError() || null == cr.getError().getErrors()){
      return null;
    }

    String result = null == cr.getError().getMessage() ? "" : cr.getError().getMessage() + ": ";
    result = cr.getError().getErrors().stream()
        .map(e -> e.getMessage())
        .collect(Collectors.joining(", ", result, ""));
    return result;
  }
}

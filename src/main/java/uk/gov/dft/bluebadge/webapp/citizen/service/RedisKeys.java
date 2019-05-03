package uk.gov.dft.bluebadge.webapp.citizen.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;

public enum RedisKeys {
  JOURNEY("%s:citizen-save-and-return-journey"),
  EMAIL_TRIES("%s:citizen-save-and-return-email-post-count"),
  CODE_TRIES("%s:citizen-save-and-return-code-post-count"),
  CODE("%s:citizen-save-and-return-journey-code");

  private String pattern;

  RedisKeys(String pattern) {

    this.pattern = pattern;
  }

  String getKey(String emailAddress) {
    return String.format(pattern, hashEmailAddress(emailAddress));
  }

  public static String hashEmailAddress(String emailAddress) {

    Mac hasher;
    try {
      hasher = Mac.getInstance("HmacSHA256");
    } catch (NoSuchAlgorithmException e) {
      throw new InternalServerException(e);
    }
    try {
      hasher.init(new SecretKeySpec(emailAddress.toLowerCase().trim().getBytes(), "HmacSHA256"));
    } catch (InvalidKeyException e) {
      throw new InternalServerException(e);
    }

    // to lowercase hexits
    return DatatypeConverter.printHexBinary(hasher.doFinal(emailAddress.getBytes()));
  }
}

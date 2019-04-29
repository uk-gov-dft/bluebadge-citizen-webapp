package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
@Service
public class RedisService {

  Random random = new Random();
  static String JOURNEY_SAVE_FOR_RETURN_PATTERN = "%s:citizen-save-and-return-journey";
  static String JOURNEY_SAVE_FOR_RETURN_CODE_PATTERN = "%s:citizen-save-and-return-journey-code";
  static String JOURNEY_SAVE_FOR_RETURN_EMAIL_POST_COUNT_PATTERN = "%s:citizen-save-and-return-email-post-count";
  static String JOURNEY_SAVE_FOR_RETURN_CODE_POST_COUNT_PATTERN = "%s:citizen-save-and-return-codel-post-count";
  private Jedis jedis;
  private RedisSessionConfig redisSessionConfig;

  @Autowired
  public RedisService(Jedis jedis, RedisSessionConfig redisSessionConfig) {
    this.jedis = jedis;
    this.redisSessionConfig = redisSessionConfig;
  }

  String getJourneySaveForReturnKey(String emailAddress) {
    return String.format(JOURNEY_SAVE_FOR_RETURN_PATTERN, hashEmailAddress(emailAddress));
  }

  String getCodeSaveForForReturnKey(String emailAddress) {
    return String.format(JOURNEY_SAVE_FOR_RETURN_CODE_PATTERN, hashEmailAddress(emailAddress));
  }

  String getEmailSubmitCountKey(String emailAddress){
    return String.format(JOURNEY_SAVE_FOR_RETURN_EMAIL_POST_COUNT_PATTERN, hashEmailAddress(emailAddress));
  }

  String getCodeSubmitCountKey(String emailAddress){
    return String.format(JOURNEY_SAVE_FOR_RETURN_CODE_POST_COUNT_PATTERN, hashEmailAddress(emailAddress));
  }

  String hashEmailAddress(String emailAddress){

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

  public void setEncryptedJourneyForReturn(String emailAddress, String cipherText) {
    jedis.set(getJourneySaveForReturnKey(emailAddress), cipherText);
    jedis.expire(getJourneySaveForReturnKey(emailAddress), redisSessionConfig.getSaveSessionDurationHours() * 60 * 60);
  }

  public String getEncryptedJourneyOnReturn(String emailAddress) {
    return jedis.get(getJourneySaveForReturnKey(emailAddress));
  }

  public boolean journeyExistsForEmail(String email){
    return jedis.exists(getJourneySaveForReturnKey(email));
  }

  public boolean securityCodeExistsForEmail(String email){
    return jedis.exists(getCodeSaveForForReturnKey(email));
  }

  public String setCodeForReturn(String emailAddress) {
    String code = String.valueOf(random.nextInt(9999));
    jedis.set(getCodeSaveForForReturnKey(emailAddress), code);
    jedis.expire(getCodeSaveForForReturnKey(emailAddress), redisSessionConfig.getSaveSessionCodeDurationMins() * 60);
    return code;
  }

  public String getCodeOnReturn(String emailAddress) {
    return jedis.get(getJourneySaveForReturnKey(emailAddress));
  }

  public void incrementEmailPostCount(String emailAddress){
    String key = getEmailSubmitCountKey(emailAddress);
    boolean exists = jedis.exists(key);
    jedis.incr(key);

    // If new expiry, then set window.
    if(!exists){
      jedis.expire(key, redisSessionConfig.getSaveSubmitThrottleTimeMins() * 60);
    }
  }

  public boolean emailPostLimitExceeded(String emailAddress){
    // TODO !!!! get Long
    return jedis.get(getEmailSubmitCountKey(emailAddress))
  }

  public int createOrIncrementSecurityCodePostCount(String emailAddress){

  }
}

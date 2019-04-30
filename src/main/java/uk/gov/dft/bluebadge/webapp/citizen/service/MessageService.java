package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {

  public void sendReturnToApplicationCodeEmail(String code, String emailAddress, String expiryDate){
    // TODO
    log.info("Return to application code, {}, NOT emailed.  Expires on {}", code, expiryDate);
  }
}

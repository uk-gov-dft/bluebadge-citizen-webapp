package uk.gov.dft.bluebadge.webapp.citizen.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import uk.gov.dft.bluebadge.common.esapi.EsapiFilter;
import uk.gov.dft.bluebadge.common.logging.JwtMdcFilter;

@Configuration
public class GeneralConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  /** Instantiates an EsapiFilter that filters requests for potential xss attacks. */
  @Bean
  @Order(500)
  public EsapiFilter getEsapiFilter() {
    return new EsapiFilter();
  }

  @Bean
  public JwtMdcFilter getJwtMdcFilter() {
    return new JwtMdcFilter();
  }
}

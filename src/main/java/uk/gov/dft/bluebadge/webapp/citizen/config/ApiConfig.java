package uk.gov.dft.bluebadge.webapp.citizen.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.DefaultUriBuilderFactory;
import uk.gov.dft.bluebadge.common.api.common.ServiceConfiguration;
import uk.gov.dft.bluebadge.webapp.citizen.client.CommonResponseErrorHandler;

@Configuration
public class ApiConfig {

  private OAuth2ClientContext oauth2ClientContext;
  @Autowired private ObjectMapper objectMapper;

  @Autowired
  public ApiConfig(OAuth2ClientContext oauth2ClientContext) {
    this.oauth2ClientContext = oauth2ClientContext;
  }

  @Bean
  public CommonResponseErrorHandler commonResponseErrorHandler() {
    return new CommonResponseErrorHandler(objectMapper);
  }

  @Validated
  @ConfigurationProperties("blue-badge.referencedataservice.servicehost")
  @Bean
  public ServiceConfiguration referenceDataManagementApiConfig() {
    return new ServiceConfiguration();
  }

  @Bean("referenceDataRestTemplate")
  OAuth2RestTemplate referenceDataRestTemplate(
      ClientCredentialsResourceDetails resourceDetails,
      ServiceConfiguration referenceDataManagementApiConfig) {
    return createOAuthRestTemplate(resourceDetails, referenceDataManagementApiConfig);
  }

  @Validated
  @ConfigurationProperties("blue-badge.applicationmanagementservice.servicehost")
  @Bean
  public ServiceConfiguration applicationManagementServiceApiConfig() {
    return new ServiceConfiguration();
  }

  @Bean("applicationManagementServiceRestTemplate")
  OAuth2RestTemplate applicationManagementServiceRestTemplate(
      ClientCredentialsResourceDetails resourceDetails,
      ServiceConfiguration applicationManagementServiceApiConfig) {
    return createOAuthRestTemplate(resourceDetails, applicationManagementServiceApiConfig);
  }

  private OAuth2RestTemplate createOAuthRestTemplate(
      ClientCredentialsResourceDetails resourceDetails, ServiceConfiguration apiConfig) {
    OAuth2RestTemplate oAuth2RestTemplate =
        new OAuth2RestTemplate(resourceDetails, oauth2ClientContext);
    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    oAuth2RestTemplate.setRequestFactory(requestFactory);
    oAuth2RestTemplate.setUriTemplateHandler(
        new DefaultUriBuilderFactory(apiConfig.getUrlPrefix()));
    oAuth2RestTemplate.setErrorHandler(commonResponseErrorHandler());
    return oAuth2RestTemplate;
  }
}

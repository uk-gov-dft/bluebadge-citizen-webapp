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
import uk.gov.dft.bluebadge.common.api.common.VersionHeaderRestTemplateInterceptor;
import uk.gov.dft.bluebadge.common.logging.LoggingAspect;
import uk.gov.dft.bluebadge.webapp.citizen.client.CommonResponseErrorHandler;

@Configuration
public class ApiConfig {

  private OAuth2ClientContext oauth2ClientContext;

  @SuppressWarnings("squid:S3305")
  @Autowired
  private ObjectMapper objectMapper;

  @SuppressWarnings("unused")
  @Autowired
  public ApiConfig(OAuth2ClientContext oauth2ClientContext) {
    this.oauth2ClientContext = oauth2ClientContext;
  }

  @SuppressWarnings("unused")
  @Bean
  public CommonResponseErrorHandler commonResponseErrorHandler() {
    return new CommonResponseErrorHandler(objectMapper);
  }

  @SuppressWarnings("unused")
  @Validated
  @ConfigurationProperties("blue-badge.referencedataservice.servicehost")
  @Bean
  public ServiceConfiguration referenceDataManagementApiConfig() {
    return new ServiceConfiguration();
  }

  @SuppressWarnings("unused")
  @Bean("referenceDataRestTemplate")
  OAuth2RestTemplate referenceDataRestTemplate(
      ClientCredentialsResourceDetails resourceDetails,
      ServiceConfiguration referenceDataManagementApiConfig) {
    return createOAuthRestTemplate(resourceDetails, referenceDataManagementApiConfig);
  }

  @SuppressWarnings("unused")
  @Validated
  @ConfigurationProperties("blue-badge.messageservice.servicehost")
  @Bean
  public ServiceConfiguration messageServiceApiConfig() {
    return new ServiceConfiguration();
  }

  @SuppressWarnings("unused")
  @Bean("messageServiceRestTemplate")
  OAuth2RestTemplate messageServiceRestTemplate(
      ClientCredentialsResourceDetails resourceDetails,
      ServiceConfiguration messageServiceApiConfig) {
    return createOAuthRestTemplate(resourceDetails, messageServiceApiConfig);
  }

  @SuppressWarnings("unused")
  @Validated
  @ConfigurationProperties("blue-badge.applicationmanagementservice.servicehost")
  @Bean
  public ServiceConfiguration applicationManagementServiceApiConfig() {
    return new ServiceConfiguration();
  }

  @SuppressWarnings("unused")
  @Bean("applicationManagementServiceRestTemplate")
  OAuth2RestTemplate applicationManagementServiceRestTemplate(
      ClientCredentialsResourceDetails resourceDetails,
      ServiceConfiguration applicationManagementServiceApiConfig) {
    return createOAuthRestTemplate(resourceDetails, applicationManagementServiceApiConfig);
  }

  @SuppressWarnings("unused")
  @Validated
  @ConfigurationProperties("blue-badge.paymentservice.servicehost")
  @Bean
  public ServiceConfiguration paymentServiceApiConfig() {
    return new ServiceConfiguration();
  }

  @SuppressWarnings("unused")
  @Bean("paymentServiceRestTemplate")
  OAuth2RestTemplate paymentServiceRestTemplate(
      ClientCredentialsResourceDetails resourceDetails,
      ServiceConfiguration paymentServiceApiConfig) {
    return createOAuthRestTemplate(resourceDetails, paymentServiceApiConfig);
  }

  @SuppressWarnings("unused")
  @Validated
  @ConfigurationProperties("blue-badge.cryptoservice.servicehost")
  @Bean
  public ServiceConfiguration cryptoServiceApiConfig() {
    return new ServiceConfiguration();
  }

  @SuppressWarnings("unused")
  @Bean("cryptoServiceRestTemplate")
  OAuth2RestTemplate cryptoServiceRestTemplate(
      ClientCredentialsResourceDetails resourceDetails,
      ServiceConfiguration cryptoServiceApiConfig) {
    return createOAuthRestTemplate(resourceDetails, cryptoServiceApiConfig);
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
    oAuth2RestTemplate
        .getInterceptors()
        .add(new VersionHeaderRestTemplateInterceptor(apiConfig.getVersionaccept()));
    return oAuth2RestTemplate;
  }

  @SuppressWarnings("unused")
  @Bean
  LoggingAspect getControllerLoggingAspect() {
    return new LoggingAspect();
  }
}

package uk.gov.dft.bluebadge.webapp.citizen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.session.InvalidSessionAccessDeniedHandler;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;

@Configuration
@EnableOAuth2Client
@Order(52)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Value("${blue-badge.auth-server.url}")
  private String authServerUrl;

  @Value("${blue-badge.auth-server.client-id}")
  private String clientId;

  @Value("${blue-badge.auth-server.client-secret}")
  private String clientSecret;

  private OAuth2ClientContext oauth2ClientContext;

  @Autowired
  public SecurityConfig(OAuth2ClientContext oauth2ClientContext) {
    this.oauth2ClientContext = oauth2ClientContext;
  }

  @Bean
  @Primary
  OAuth2RestTemplate restTemplate(ClientCredentialsResourceDetails resourceDetails) {
    return new OAuth2RestTemplate(resourceDetails, oauth2ClientContext);
  }

  @ConfigurationProperties("security.oauth2.client")
  @Bean
  public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
    return new ClientCredentialsResourceDetails();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest()
        .permitAll()
        .and()
        .sessionManagement()
        .invalidSessionUrl(Mappings.URL_ROOT)
        .and()
        .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler());
  }

  AccessDeniedHandler accessDeniedHandler() {
    return new InvalidSessionAccessDeniedHandler(
        new SimpleRedirectInvalidSessionStrategy(Mappings.URL_ROOT));
  }
}

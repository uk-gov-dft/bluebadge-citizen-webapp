package uk.gov.dft.bluebadge.webapp.citizen.config;

import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.FORWARD;
import static javax.servlet.DispatcherType.INCLUDE;
import static javax.servlet.DispatcherType.REQUEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import uk.gov.dft.bluebadge.common.esapi.EsapiFilter;
import uk.gov.dft.bluebadge.common.logging.ExceptionLoggingFilter;
import uk.gov.dft.bluebadge.common.logging.JwtMdcFilter;
import uk.gov.dft.bluebadge.common.logging.VersionLoggingFilter;

@Configuration
public class GeneralConfig {
  @Autowired(required = false)
  BuildProperties buildProperties;

  @Value("${blue-badge.api.version}")
  private String apiVersion;

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

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public VersionLoggingFilter versionLoggingFilter() {
    return new VersionLoggingFilter(apiVersion, buildProperties);
  }

  @Bean
  public FilterRegistrationBean<ExceptionLoggingFilter> exceptionLoggingFilter(
      Map<ExceptionLoggingFilter.ClassMessageKey, String> exceptionLoggingFilterConfig) {
    ExceptionLoggingFilter filter = new ExceptionLoggingFilter(exceptionLoggingFilterConfig);
    FilterRegistrationBean<ExceptionLoggingFilter> filterRegistrationBean =
        new FilterRegistrationBean<>(filter);
    filterRegistrationBean.setName("BBExceptionLoggingFilter");
    filterRegistrationBean.setDispatcherTypes(ASYNC, ERROR, FORWARD, INCLUDE, REQUEST);
    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 6);
    return filterRegistrationBean;
  }
}

package uk.gov.dft.bluebadge.webapp.citizen.filters;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EsapiFilterTest {

  public static final String NASTY_STRING = "<script>console.log('fred');</script>";
  public static final String GOOD_STRING = "nothing bad in here";

  @LocalServerPort
  private int port;

  CloseableHttpClient client;
  CookieStore cookieStore;
  HttpContext httpContext;

  @Before
  public void setUp() {
    client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
    cookieStore = new BasicCookieStore();
    httpContext = new BasicHttpContext();
    httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
  }

  @Test
  public void shouldNotCleanGoodValue() throws IOException {

    String contentAsString = postData(GOOD_STRING, client, httpContext);
    assertEquals(GOOD_STRING, contentAsString);
  }

  @Test
  public void shouldCleanPostValue() throws IOException {

    String moreContentAsString = postData(NASTY_STRING, client, httpContext);
    assertEquals("", moreContentAsString);
    client.close();

  }

  private String postData(String dataContent, CloseableHttpClient client, HttpContext httpContext) throws IOException {
    HttpPost httpPost = new HttpPost("http://localhost:" + port + "/test-bounce");
    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("data", dataContent));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    CloseableHttpResponse filteredResponse = client.execute(httpPost, httpContext);

    ByteArrayOutputStream writer = new ByteArrayOutputStream();
    filteredResponse.getEntity().writeTo(writer);
    return new String(writer.toByteArray());
  }

  @TestConfiguration
  @EnableWebSecurity
  @Order(10)
  public static class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

      // Set security configurations
      http.csrf().disable();
      http.csrf().requireCsrfProtectionMatcher(request -> false);
    }

  }
}

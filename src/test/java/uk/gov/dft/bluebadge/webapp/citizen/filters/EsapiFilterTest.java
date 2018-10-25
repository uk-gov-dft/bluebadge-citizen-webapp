package uk.gov.dft.bluebadge.webapp.citizen.filters;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EsapiFilterTest {

  public static final String IMG_SRC = "<img src=\"C:\\Documents and Settings\\screenshots\\Image01.png\"/>";
  public static final String PERCENT_ENCODED_STRING = "%2520";
  public static final String PERCENT_STRING = "25%";
  public static final String DOUBLE_BACKSLASH = "Hello\\\\ there";
  public static final String URL_STRING = "https://www.host.com:8080/evidence?x=y&%2520";
  public static final String URL_NASTY_STRING = "http://bobssite.org?q=puppies%3Cscript%2520src%3D%22http%3A%2F%2Fmallorysevilsite.com%2Fauthstealer.js%22%3E%3C%2Fscript%3E";
  public static final String NASTY_STRING = "<script>console.log('fred');</script>But this is ok";
  public static final String GOOD_STRING = "nothing bad in here";

  @LocalServerPort private int port;

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
    assertEquals("But this is ok", moreContentAsString);
    client.close();
  }

  @Test
  public void shouldCleanImageSrc() throws IOException {

    String moreContentAsString = postData(IMG_SRC, client, httpContext);
    assertEquals("<img />", moreContentAsString);
    client.close();
  }

  @Test
  public void shouldNotCleanPercent() throws IOException {

    String moreContentAsString = postData(PERCENT_STRING, client, httpContext);
    assertEquals("25%", moreContentAsString);
    client.close();
  }


  @Test
  public void shouldCleanEncoded() throws IOException {

    String moreContentAsString = postData(PERCENT_ENCODED_STRING, client, httpContext);
    assertEquals("", moreContentAsString);
    client.close();
  }

  @Test
  public void shouldNotCleanUrl() throws IOException {

    String moreContentAsString = postData(URL_STRING, client, httpContext);
    assertEquals("", moreContentAsString);
    client.close();
  }


  @Test
  public void shouldCleanEncodedUrl() throws IOException {

    String moreContentAsString = postData(URL_NASTY_STRING, client, httpContext);
    assertEquals("", moreContentAsString);
    client.close();
  }


  @Test
  public void shouldCleanDoubleBackslash() throws IOException {

    String moreContentAsString = postData(DOUBLE_BACKSLASH, client, httpContext);
    assertEquals("", moreContentAsString);
    client.close();
  }

  private String postData(String dataContent, CloseableHttpClient client, HttpContext httpContext)
      throws IOException {
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

package uk.gov.dft.bluebadge.webapp.citizen;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(999)
public class CookieFilter implements Filter {

  public static final String COOKIE_BANNER_KEY = "cookie_banner_seen";
  public static final String COOKIE_BANNER_VALUE = "yes";
  public static final int SECONDS_IN_MONTH = 2628000;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    Cookie[] cookies = req.getCookies();
    Stream<Cookie> stream = Objects.nonNull(cookies) ? Arrays.stream(cookies) : Stream.empty();
    Optional<Cookie> cookie = stream.filter(c -> COOKIE_BANNER_KEY.equals(c.getName())).findFirst();

    if (!cookie.isPresent()) {
      Cookie newCookie = new Cookie(COOKIE_BANNER_KEY, COOKIE_BANNER_VALUE);
      newCookie.setMaxAge(SECONDS_IN_MONTH);
      res.addCookie(newCookie);
    }

    chain.doFilter(request, res);
  }

  @Override
  public void destroy() {}
}

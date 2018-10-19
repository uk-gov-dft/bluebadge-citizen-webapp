package uk.gov.dft.bluebadge.webapp.citizen.filters;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/test-bounce")
public class EsapiTestController {

  @PostMapping
  public ResponseEntity<String> doPost(@ModelAttribute EsapiTestForm esapiTestForm) {
    return ResponseEntity.ok(esapiTestForm.getData());
  }

  @GetMapping
  public ResponseEntity<String> doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    return ResponseEntity.ok("fred");
  }

  @Data
  public static class EsapiTestForm {
    private String data;
  }

}

package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import org.springframework.test.web.servlet.ResultMatcher;

public class ControllerTestFixture {
  public static ResultMatcher formRequestFlashAttributeHasFieldErrorCode(String fieldName, String error) {
    return flash()
        .attribute(
            "org.springframework.validation.BindingResult.formRequest",
            hasProperty(
                "fieldErrors",
                hasItem(
                    allOf(
                        hasProperty("field", equalTo(fieldName)),
                        hasProperty("code", equalTo(error))))));
  }
}

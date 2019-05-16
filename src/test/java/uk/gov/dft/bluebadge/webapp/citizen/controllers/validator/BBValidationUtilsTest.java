package uk.gov.dft.bluebadge.webapp.citizen.controllers.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

class BBValidationUtilsTest {

  @Test
  void rejectIfTooLong_tooLong() {
    WhatMakesWalkingDifficultForm form =
        WhatMakesWalkingDifficultForm.builder()
            .balanceDescription("Some long bit of text that will fail validation")
            .build();
    BindingResult errors = new BeanPropertyBindingResult(form, "testForm");

    BBValidationUtils.rejectIfTooLong(errors, "balanceDescription", "testCode", 20);

    assertThat(errors.getErrorCount()).isEqualTo(1);
    assertThat(errors.getFieldError("balanceDescription")).isNotNull();
    assertThat(errors.getFieldError("balanceDescription").getCode()).isEqualTo("testCode");
  }

  @Test
  void rejectIfTooLong_ok() {
    WhatMakesWalkingDifficultForm form =
        WhatMakesWalkingDifficultForm.builder().balanceDescription("1234567890123456789").build();
    BindingResult errors = new BeanPropertyBindingResult(form, "testForm");

    BBValidationUtils.rejectIfTooLong(errors, "balanceDescription", "testCode", 20);

    assertThat(errors.getErrorCount()).isEqualTo(0);
  }
}

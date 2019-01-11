package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.junit.Assert.*;

import org.junit.Test;

public class MobilityAidAddFormTest {

  @Test
  public void getDescriptionTest() {
    MobilityAidAddForm form = new MobilityAidAddForm();
    assertNull(form.getAidType());
    form.setAidType("Wheel chair");
    assertEquals("Wheel chair", form.getAidType());
    form.setAidType("Walking aid");
    assertEquals("Walking aid", form.getAidType());
  }
}

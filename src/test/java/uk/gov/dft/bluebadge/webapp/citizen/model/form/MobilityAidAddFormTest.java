package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.junit.Assert.*;

import org.junit.Test;

public class MobilityAidAddFormTest {

  @Test
  public void getDescriptionTest() {
    MobilityAidAddForm form = MobilityAidAddForm.builder().build();
    assertEquals("", form.getAidTypeDescription());
    form.setAidType(MobilityAidAddForm.AidType.WHEELCHAIR);
    assertEquals(MobilityAidAddForm.AidType.WHEELCHAIR.getType(), form.getAidTypeDescription());
    form.setAidType(MobilityAidAddForm.AidType.WALKING_AID);
    form.setCustomAidName("Custom");
    assertEquals("Custom", form.getAidTypeDescription());
  }
}

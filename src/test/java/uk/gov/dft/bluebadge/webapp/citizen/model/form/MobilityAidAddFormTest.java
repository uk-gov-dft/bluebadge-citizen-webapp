package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import org.junit.Test;

import static org.junit.Assert.*;

public class MobilityAidAddFormTest {

  @Test
  public void getDescriptionTest() {
    MobilityAidAddForm form = new MobilityAidAddForm();
    assertEquals("", form.getAidTypeDescription());
    form.setAidType(MobilityAidAddForm.AidType.WHEELCHAIR);
    assertEquals(MobilityAidAddForm.AidType.WHEELCHAIR.getType(), form.getAidTypeDescription());
    form.setAidType(MobilityAidAddForm.AidType.WALKING_AID);
    form.setCustomAidName("Custom");
    assertEquals("Custom", form.getAidTypeDescription());
  }

}
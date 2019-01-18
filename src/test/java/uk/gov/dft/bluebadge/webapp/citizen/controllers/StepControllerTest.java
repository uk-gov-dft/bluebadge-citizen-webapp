package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;

public class StepControllerTest {

  private StepController stepController;

  @Before
  public void setup() {
    stepController =
        new StepController() {
          @Override
          public StepDefinition getStepDefinition() {
            return StepDefinition.ADDRESS;
          }
        };
  }

  @Test
  public void getFieldsOrder_shouldReturnFieldOrder_WhenThereIsAFormRequestInModel() {
    Model model =
        new Model() {
          @Override
          public Model addAttribute(String attributeName, Object attributeValue) {
            return null;
          }

          @Override
          public Model addAttribute(Object attributeValue) {
            return null;
          }

          @Override
          public Model addAllAttributes(Collection<?> attributeValues) {
            return null;
          }

          @Override
          public Model addAllAttributes(Map<String, ?> attributes) {
            return null;
          }

          @Override
          public Model mergeAttributes(Map<String, ?> attributes) {
            return null;
          }

          @Override
          public boolean containsAttribute(String attributeName) {
            return false;
          }

          @Override
          public Map<String, Object> asMap() {
            Map<String, Object> modelMap = new HashMap();
            EnterAddressForm formRequest = EnterAddressForm.builder().build();
            modelMap.put("formRequest", formRequest);
            return modelMap;
          }
        };
    List<String> fieldOrder = stepController.getFieldsOrder(model);
    List<String> expectedFieldOrder =
        Lists.newArrayList("buildingAndStreet", "optionalAddress", "townOrCity", "postcode");
    assertThat(fieldOrder).isEqualTo(expectedFieldOrder);
  }

  @Test
  public void getFieldsOrder_shouldReturnEmptyList_WhenThereIsNoFormRequestInModel() {
    Model model =
        new Model() {
          @Override
          public Model addAttribute(String attributeName, Object attributeValue) {
            return null;
          }

          @Override
          public Model addAttribute(Object attributeValue) {
            return null;
          }

          @Override
          public Model addAllAttributes(Collection<?> attributeValues) {
            return null;
          }

          @Override
          public Model addAllAttributes(Map<String, ?> attributes) {
            return null;
          }

          @Override
          public Model mergeAttributes(Map<String, ?> attributes) {
            return null;
          }

          @Override
          public boolean containsAttribute(String attributeName) {
            return false;
          }

          @Override
          public Map<String, Object> asMap() {
            Map<String, Object> modelMap = new HashMap();
            return modelMap;
          }
        };
    List<String> fieldOrder = stepController.getFieldsOrder(model);
    assertThat(fieldOrder).isEqualTo(Lists.newArrayList());
  }
}

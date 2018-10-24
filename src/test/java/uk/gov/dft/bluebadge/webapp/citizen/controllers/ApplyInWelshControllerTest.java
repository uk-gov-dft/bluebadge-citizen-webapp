package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;

public class ApplyInWelshControllerTest {

  private MockMvc mockMvc;
  private ApplyInWelshController controller;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new ApplyInWelshController();
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_ShouldDisplayApplyInWelshTemplate() throws Exception {
    mockMvc
        .perform(get("/apply-in-inWales"))
        .andExpect(status().isOk())
        .andExpect(view().name("apply-in-inWales"));
  }
}

package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
        .perform(get("/apply-in-welsh"))
        .andExpect(status().isOk())
        .andExpect(view().name("apply-in-welsh"));
  }
}

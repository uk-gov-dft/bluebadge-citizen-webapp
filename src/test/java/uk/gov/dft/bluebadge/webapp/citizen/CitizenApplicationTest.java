package uk.gov.dft.bluebadge.webapp.citizen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"management.server.port=0"})
@ActiveProfiles({"test", "dev"})
public class CitizenApplicationTest {
  @Test
  public void contextLoads() {}
}

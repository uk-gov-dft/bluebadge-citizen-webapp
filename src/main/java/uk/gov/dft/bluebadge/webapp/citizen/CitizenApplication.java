package uk.gov.dft.bluebadge.webapp.citizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"uk.gov.dft.bluebadge"})
public class CitizenApplication {

  public static void main(String[] args) {
    SpringApplication.run(CitizenApplication.class, args);
  }
}

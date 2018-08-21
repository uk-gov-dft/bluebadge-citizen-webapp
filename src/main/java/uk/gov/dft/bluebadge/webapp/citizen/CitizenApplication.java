package uk.gov.dft.bluebadge.webapp.citizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class CitizenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitizenApplication.class, args);
    }
}
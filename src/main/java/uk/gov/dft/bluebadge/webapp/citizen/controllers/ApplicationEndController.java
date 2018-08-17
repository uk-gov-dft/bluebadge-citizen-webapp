package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationEndController {

    public static final String URL_DECLARATION = "/apply-for-a-badge/declaration";
    public static final String TEMPLTE_DECLARATION = "application-end/declaration";

    @GetMapping(URL_DECLARATION)
    public String showDeclaration() {
        return TEMPLTE_DECLARATION;
    }
}

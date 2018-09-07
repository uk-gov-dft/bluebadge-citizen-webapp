package uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ErrorController {

    public static final String URL_500_ERROR = "/something-went-wrong";
    public static final String TEMPLATE_500_ERROR = "error/500";

    public static final String URL_400_ERROR = "/page-not-found";
    public static final String TEMPLATE_400_ERROR = "error/400";

    @GetMapping(URL_500_ERROR)
    public String show500(Model model) {
        return TEMPLATE_500_ERROR;
    }

    @GetMapping(URL_400_ERROR)
    public String show400() {
        return TEMPLATE_400_ERROR;
    }
}

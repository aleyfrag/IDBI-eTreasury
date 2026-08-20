package in.co.idbibank.etreasury.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TreasuryPageController {

    private static final Logger log = LoggerFactory.getLogger(TreasuryPageController.class);

    @GetMapping("/")
    String root() {
        log.debug("Routing application root to the treasury dashboard");
        return "redirect:/login";
    }

    @GetMapping("/login")
    String login() {
        log.debug("Rendering LDAP login page");
        return "login";
    }

    @GetMapping("/home")
    String home() {
        log.debug("Rendering treasury dashboard");
        return "home";
    }

    @GetMapping("/deals/new")
    String newDeal() {
        log.debug("Rendering new forex deal page");
        return "deal-form";
    }

    @GetMapping("/deals")
    String deals() {
        log.debug("Rendering forex deal list page");
        return "deal-list";
    }
}

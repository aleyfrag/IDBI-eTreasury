package in.co.idbibank.etreasury.idbiforex.controller;

import in.co.idbibank.etreasury.idbiforex.service.IdbiForexPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IdbiForexController {

    private final IdbiForexPageService pageService;

    public IdbiForexController(IdbiForexPageService pageService) {
        this.pageService = pageService;
    }

    /** Delegates the IDBIForex home-page request to the module page service. */
    @GetMapping("/idbiforex/home")
    public String home(HttpServletRequest request, HttpSession session, Model model) {
        return pageService.showHome(request, session, model);
    }

    /** Delegates the IDBIForex deal-list request to the module page service. */
    @GetMapping("/idbiforex/deals")
    public String deals(
            @RequestParam(required = false) String transactionDate,
            HttpServletRequest request,
            HttpSession session,
            Model model) {
        return pageService.showDeals(transactionDate, request, session, model);
    }
}

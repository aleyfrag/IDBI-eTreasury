package in.co.idbibank.etreasury.idbiforex.service;

import in.co.idbibank.etreasury.core.model.MenuItem;
import in.co.idbibank.etreasury.core.model.SubMenuItem;
import in.co.idbibank.etreasury.core.model.TreasuryUserDetails;
import in.co.idbibank.etreasury.idbiforex.exception.IdbiForexDataException;
import in.co.idbibank.etreasury.idbiforex.model.IdbiForexDeal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class IdbiForexPageService {

    private static final Logger log = LoggerFactory.getLogger(IdbiForexPageService.class);
    private static final String IDBI_FOREX_MENU = "IDBIFOREX";

    private final IdbiForexDealService dealService;

    public IdbiForexPageService(IdbiForexDealService dealService) {
        this.dealService = dealService;
    }

    /**
     * Authorizes the IDBIForex main menu, sets its selected state and returns the home JSP.
     */
    public String showHome(HttpServletRequest request, HttpSession session, Model model) {
        MenuItem idbiForexMenu = findIdbiForexMenu(session.getAttribute("userMenus"));
        if (idbiForexMenu == null) {
            log.warn("IDBIForex home access rejected: sessionId={}", session.getId());
            return "redirect:/home?menuUnavailable";
        }

        addActiveNavigation(model, idbiForexMenu, request);
        log.info("Rendering IDBIForex home page: sessionId={}", session.getId());
        return "idbiforex/home";
    }

    /**
     * Validates the session and menu, loads deals and prepares every value used by deal-list.jsp.
     */
    public String showDeals(
            String transactionDate,
            HttpServletRequest request,
            HttpSession session,
            Model model) {
        MenuItem idbiForexMenu = findIdbiForexMenu(session.getAttribute("userMenus"));
        if (idbiForexMenu == null) {
            log.warn("IDBIForex deal-list access rejected: sessionId={}", session.getId());
            return "redirect:/home?menuUnavailable";
        }

        Object sessionUser = session.getAttribute("treasuryUser");
        if (!(sessionUser instanceof TreasuryUserDetails user)) {
            log.warn("IDBIForex deal-list session has no TreasuryUserDetails: sessionId={}", session.getId());
            return "redirect:/session-expired";
        }

        LocalDate effectiveDate = LocalDate.now();
        addActiveNavigation(model, idbiForexMenu, request);
        initializeDealListModel(model, effectiveDate);

        try {
            effectiveDate = parseTransactionDate(transactionDate);
            model.addAttribute("transactionDate", effectiveDate);

            List<IdbiForexDeal> deals = dealService.loadDeals(user, effectiveDate);
            BigDecimal totalAmount = deals.stream()
                    .map(IdbiForexDeal::amount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("deals", deals);
            model.addAttribute("dealCount", deals.size());
            model.addAttribute("totalAmount", totalAmount);
            addDealResultMessage(model, deals.size());
            log.info("Rendering IDBIForex deal list: ein={} solId={} date={} count={}",
                    user.einNumber(), user.solId(), effectiveDate, deals.size());
        } catch (DateTimeParseException exception) {
            log.warn("Invalid IDBIForex transaction date: ein={} suppliedDate={}",
                    user.einNumber(), transactionDate);
            addFrontendMessage(model, "danger",
                    "Please select a valid transaction date in YYYY-MM-DD format.");
        } catch (IdbiForexDataException exception) {
            log.error("IDBIForex deal list could not be loaded: ein={} solId={} date={}",
                    user.einNumber(), user.solId(), effectiveDate, exception);
            addFrontendMessage(model, "danger",
                    "Deal information is temporarily unavailable. Please retry or contact Treasury support.");
        } catch (RuntimeException exception) {
            log.error("Unexpected IDBIForex deal-list failure: ein={} solId={} date={}",
                    user.einNumber(), user.solId(), effectiveDate, exception);
            addFrontendMessage(model, "danger",
                    "The deal request could not be completed. Please retry or contact Treasury support.");
        }

        return "idbiforex/deal-list";
    }

    /** Creates safe defaults so the JSP remains renderable after validation or Oracle failures. */
    private void initializeDealListModel(Model model, LocalDate transactionDate) {
        model.addAttribute("transactionDate", transactionDate);
        model.addAttribute("deals", List.of());
        model.addAttribute("dealCount", 0);
        model.addAttribute("totalAmount", BigDecimal.ZERO);
    }

    /** Uses today when no date was supplied and otherwise parses the HTML ISO date value. */
    private LocalDate parseTransactionDate(String transactionDate) {
        return transactionDate == null || transactionDate.isBlank()
                ? LocalDate.now()
                : LocalDate.parse(transactionDate.trim());
    }

    /** Selects the success or no-record message shown after a completed procedure call. */
    private void addDealResultMessage(Model model, int dealCount) {
        if (dealCount == 0) {
            addFrontendMessage(model, "info",
                    "No IDBIForex deals were found for the selected transaction date.");
            return;
        }

        addFrontendMessage(model, "success",
                dealCount + (dealCount == 1 ? " deal" : " deals") + " loaded successfully.");
    }

    /** Adds a safe alert type and message consumed by the JSP. */
    private void addFrontendMessage(Model model, String type, String message) {
        model.addAttribute("dealMessageType", type);
        model.addAttribute("dealMessage", message);
    }

    /** Adds the assigned main menu and URL-matched submenu IDs used by sidebar.js. */
    private void addActiveNavigation(Model model, MenuItem menu, HttpServletRequest request) {
        SubMenuItem activeSubMenu = findSubMenuByRequest(menu, request);
        model.addAttribute("activeMenuId", menu.menuId());
        model.addAttribute("activeSubMenuId", activeSubMenu == null ? null : activeSubMenu.subMenuId());
    }

    /** Finds only the IDBIForex module; the separate IDBIFx module does not grant access. */
    private MenuItem findIdbiForexMenu(Object sessionMenus) {
        if (!(sessionMenus instanceof List<?> menus)) {
            return null;
        }

        return menus.stream()
                .filter(MenuItem.class::isInstance)
                .map(MenuItem.class::cast)
                .filter(menu -> IDBI_FOREX_MENU.equals(normalizeMenuName(menu.menuName())))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds the active submenu by matching the current controller path with the page URL
     * returned by the menu procedure. This works for every assigned submenu without
     * hardcoding a submenu name or numeric ID.
     */
    private SubMenuItem findSubMenuByRequest(MenuItem menu, HttpServletRequest request) {
        if (menu == null || menu.subMenus() == null || request == null) {
            return null;
        }

        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isBlank() && requestPath.startsWith(contextPath)) {
            requestPath = requestPath.substring(contextPath.length());
        }
        String activePath = normalizePageUrl(requestPath);

        return menu.subMenus().stream()
                .filter(Objects::nonNull)
                .filter(subMenu -> activePath.equals(normalizePageUrl(subMenu.pageUrl())))
                .findFirst()
                .orElse(null);
    }

    /** Normalizes internal controller paths before comparing them with database menu URLs. */
    private String normalizePageUrl(String pageUrl) {
        if (pageUrl == null || pageUrl.isBlank() || "#".equals(pageUrl.trim())) {
            return "";
        }
        String normalized = pageUrl.trim();
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        return normalized.length() > 1 && normalized.endsWith("/")
                ? normalized.substring(0, normalized.length() - 1)
                : normalized;
    }

    /** Normalizes database display names without treating IDBIForex and IDBIFx as aliases. */
    private String normalizeMenuName(String menuName) {
        return menuName == null
                ? ""
                : menuName.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
    }
}

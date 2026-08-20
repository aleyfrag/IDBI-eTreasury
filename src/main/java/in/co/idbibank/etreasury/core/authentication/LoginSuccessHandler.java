package in.co.idbibank.etreasury.core.authentication;

import in.co.idbibank.etreasury.core.exception.CoreDataException;
import in.co.idbibank.etreasury.core.exception.UserNotFoundException;
import in.co.idbibank.etreasury.core.model.MenuItem;
import in.co.idbibank.etreasury.core.model.TreasuryUserDetails;
import in.co.idbibank.etreasury.core.repository.CoreRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(LoginSuccessHandler.class);

    private final CoreRepository coreRepository;

    /**
     * Receives the core repository used to load application user and menu data
     * after Spring Security has completed LDAP authentication successfully.
     */
    public LoginSuccessHandler(CoreRepository coreRepository) {
        this.coreRepository = coreRepository;
        log.info("LoginSuccessHandler initialized");
    }

    /**
     * Runs immediately after successful LDAP authentication. This method loads the
     * corresponding application user, verifies that the user is approved, loads the
     * assigned menus, stores the data in the HTTP session and redirects to the first
     * database-configured menu page. Known failures return a message code to login.jsp.
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String username = authentication.getName();
        log.info("LoginSuccessHandler invoked: username={} authenticated={}",
                username, authentication.isAuthenticated());

        try {
            // Call GET_USER_DETAILS through CoreRepository for the LDAP username/EIN.
            log.info("Loading user details after successful LDAP login: username={}", username);
            TreasuryUserDetails userDetails = coreRepository.getUserDetails(username);

            // Convert database user/status problems into a specific login-page message.
            String rejectionCode = rejectionCode(userDetails);
            if (rejectionCode != null) {
                log.warn("Login rejected after LDAP authentication: username={} reason={} status={} adminStatus={}",
                        username,
                        rejectionCode,
                        userDetails == null ? null : userDetails.status(),
                        userDetails == null ? null : userDetails.adminStatus());
                invalidateSession(request);
                response.sendRedirect(request.getContextPath() + "/login?" + rejectionCode);
                return;
            }

            // Call GET_USER_MENU through CoreRepository and build the user's menu hierarchy.
            log.info("Loading menu details after successful LDAP login: username={}", username);
            List<MenuItem> menus = coreRepository.getMenuDetails(username);

            // Stop login when the application database has assigned no menu access.
            if (menus.isEmpty()) {
                log.warn("No menu access configured for username={}", username);
                invalidateSession(request);
                response.sendRedirect(request.getContextPath() + "/login?menuAccessDenied");
                return;
            }

            // Keep user and menu information in this authenticated user's HTTP session.
            HttpSession session = request.getSession();
            session.setAttribute("treasuryUser", userDetails);
            session.setAttribute("userMenus", menus);

            // Resolve and open the first assigned main menu/home submenu URL from the database.
            String landingPage = resolveLandingPage(menus);
            log.info("Login initialization completed: username={} menuCount={} landingPage={}",
                    username, menus.size(), landingPage);
            response.sendRedirect(request.getContextPath() + landingPage);
        } catch (UserNotFoundException exception) {
            log.warn("Login rejected because LDAP user is absent from application database: username={}",
                    username);
            invalidateSession(request);
            response.sendRedirect(request.getContextPath() + "/login?userNotRegistered");
        } catch (CoreDataException exception) {
            log.error("Login initialization failed for username={}", username, exception);
            invalidateSession(request);
            response.sendRedirect(request.getContextPath() + "/login?menuLoadError");
        }
    }

    /**
     * Returns true only when a database status value is the approved code A.
     * Null, blank and every other status value are treated as unapproved.
     */
    private boolean isApproved(String status) {
        return "A".equalsIgnoreCase(status == null ? null : status.trim());
    }

    /**
     * Returns the login-page query flag for an invalid application user.
     * A null record means LDAP succeeded but the EIN is not registered in e-Treasury.
     * D and U in either STATUS or ADM_STATUS mean dormant/deactivated. Every other
     * non-A value is treated as not active or not approved.
     */
    String rejectionCode(TreasuryUserDetails userDetails) {
        if (userDetails == null) {
            return "userNotRegistered";
        }
        if (isDormant(userDetails.status()) || isDormant(userDetails.adminStatus())) {
            return "dormant";
        }
        if (!isApproved(userDetails.status()) || !isApproved(userDetails.adminStatus())) {
            return "deactivated";
        }
        return null;
    }

    /** Returns true for the database dormant/deactivated codes D and U. */
    private boolean isDormant(String status) {
        String normalizedStatus = status == null ? "" : status.trim();
        return "D".equalsIgnoreCase(normalizedStatus) || "U".equalsIgnoreCase(normalizedStatus);
    }

    /**
     * Returns the database-provided landing URL of the user's first assigned main menu.
     * It uses the main-menu home URL when SUB_MENU is blank, or the first submenu URL
     * when Home is stored as a submenu. Treasury overview is only a safe fallback.
     */
    private String resolveLandingPage(List<MenuItem> menus) {
        MenuItem firstMenu = menus.getFirst();
        if (isValidInternalUrl(firstMenu.pageUrl())) {
            return firstMenu.pageUrl();
        }

        if (!firstMenu.subMenus().isEmpty()) {
            String firstSubMenuUrl = firstMenu.subMenus().getFirst().pageUrl();
            if (isValidInternalUrl(firstSubMenuUrl)) {
                return firstSubMenuUrl;
            }
        }

        log.warn("First assigned menu has no valid landing URL: menu={} mainUrl={}",
                firstMenu.menuName(), firstMenu.pageUrl());
        return "/home";
    }

    /**
     * Accepts only application-relative controller paths and rejects anchors or external URLs.
     */
    private boolean isValidInternalUrl(String url) {
        return url != null
                && !url.isBlank()
                && !"#".equals(url)
                && url.startsWith("/")
                && !url.startsWith("//");
    }

    /**
     * Clears Spring Security authentication and invalidates the current HTTP session
     * so rejected users cannot retain partially initialized authenticated data.
     */
    private void invalidateSession(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

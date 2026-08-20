package in.co.idbibank.etreasury.admin.exception;

import in.co.idbibank.etreasury.admin.controller.AdminController;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Handles expected Admin controller failures without exposing internal details. */
@ControllerAdvice(assignableTypes = AdminController.class)
public class AdminExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AdminExceptionHandler.class);

    /** Shows the shared safe error page when the Admin menu/session is unavailable. */
    @ExceptionHandler(AdminPageException.class)
    public String handleAdminPageException(
            AdminPageException exception,
            HttpServletRequest request,
            Model model) {
        String reference = Long.toHexString(System.currentTimeMillis()).toUpperCase();
        log.warn("Admin page unavailable: reference={} path={} reason={}",
                reference, request.getRequestURI(), exception.getMessage());

        model.addAttribute("errorTitle", "Admin page unavailable");
        model.addAttribute("errorMessage", "The requested Admin menu is not assigned to your session.");
        model.addAttribute("errorReference", reference);
        return "error";
    }
}

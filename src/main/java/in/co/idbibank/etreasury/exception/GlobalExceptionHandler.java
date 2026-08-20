package in.co.idbibank.etreasury.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class)
    Object handleMissingResource(
            NoResourceFoundException exception,
            HttpServletRequest request) {
        if (isAuthenticated(request)) {
            log.warn("Static resource not found for authenticated user; redirecting to overview: "
                            + "method={} path={} user={}",
                    request.getMethod(), request.getRequestURI(), request.getUserPrincipal().getName());
            return new ModelAndView("redirect:/home");
        }

        log.debug("Static resource not found: method={} path={}",
                request.getMethod(), request.getRequestURI());
        return ResponseEntity.notFound().build();
    }

    /**
     * Returns true only for a fully authenticated user. Anonymous requests must retain the
     * original 404 response so missing public assets cannot create a redirect loop.
     */
    private boolean isAuthenticated(HttpServletRequest request) {
        return request.getUserPrincipal() instanceof Authentication authentication
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String handleUnexpectedException(Exception exception, HttpServletRequest request, Model model) {
        String reference = Long.toHexString(System.currentTimeMillis()).toUpperCase();
        log.error("Unhandled request failure: reference={} method={} path={}",
                reference, request.getMethod(), request.getRequestURI(), exception);

        model.addAttribute("errorTitle", "Unable to complete your request");
        model.addAttribute("errorMessage", "Please try again or contact Treasury support.");
        model.addAttribute("errorReference", reference);
        return "error";
    }
}

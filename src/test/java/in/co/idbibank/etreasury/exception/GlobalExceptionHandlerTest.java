package in.co.idbibank.etreasury.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void missingResourceRedirectsAuthenticatedUserToOverview() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/assets/missing.js");
        request.setUserPrincipal(
                UsernamePasswordAuthenticationToken.authenticated("int12991", "N/A", java.util.List.of()));

        Object result = handler.handleMissingResource(missingResource(), request);

        assertThat(result).isInstanceOf(ModelAndView.class);
        assertThat(((ModelAndView) result).getViewName()).isEqualTo("redirect:/home");
    }

    @Test
    void missingResourceRemainsNotFoundForAnonymousRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/assets/missing.js");

        Object result = handler.handleMissingResource(missingResource(), request);

        assertThat(result).isInstanceOf(ResponseEntity.class);
        assertThat(((ResponseEntity<?>) result).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private NoResourceFoundException missingResource() {
        return new NoResourceFoundException(
                HttpMethod.GET, "assets/missing.js", "classpath:/static/");
    }
}

package in.co.idbibank.etreasury;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import in.co.idbibank.etreasury.config.LdapAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "app.ldap.port=0")
class AuthenticationIntegrationTest {

    @Autowired
    LdapAuthenticationProvider authenticationProvider;

    @Test
    void seededLdapUserCanSignIn() {
        var result = authenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated("int12991", "treasury123"));
        assertThat(result.isAuthenticated()).isTrue();
        assertThat(result.getName()).isEqualTo("int12991");
    }

    @Test
    void invalidPasswordIsRejected() {
        var request = UsernamePasswordAuthenticationToken.unauthenticated("int12991", "wrong-password");
        assertThatThrownBy(() -> authenticationProvider.authenticate(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}

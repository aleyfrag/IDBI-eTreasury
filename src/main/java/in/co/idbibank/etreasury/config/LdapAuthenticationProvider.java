package in.co.idbibank.etreasury.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(LdapAuthenticationProvider.class);

    private final LdapTemplate ldapTemplate;

    public LdapAuthenticationProvider(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (!username.matches("[A-Za-z0-9._-]+") || password.isBlank()) {
            throw new BadCredentialsException("Invalid employee ID or password");
        }

        String filter = new EqualsFilter("uid", username).encode();
        log.info("LDAP authentication started: username={}", username);
        try {
            boolean authenticated = ldapTemplate.authenticate("ou=people", filter, password);
            if (!authenticated) {
                throw new BadCredentialsException("Invalid employee ID or password");
            }
            log.info("LDAP authentication succeeded: username={}", username);
            return UsernamePasswordAuthenticationToken.authenticated(
                    username, null, List.of(new SimpleGrantedAuthority("ROLE_TREASURY_USER")));
        } catch (org.springframework.ldap.AuthenticationException exception) {
            log.warn("LDAP authentication failed: username={}", username);
            throw new BadCredentialsException("Invalid employee ID or password", exception);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

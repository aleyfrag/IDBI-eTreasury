package in.co.idbibank.etreasury.config;

import in.co.idbibank.etreasury.core.authentication.LoginSuccessHandler;
import jakarta.servlet.DispatcherType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

@Configuration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LdapAuthenticationProvider ldapAuthenticationProvider,
            SessionRegistry sessionRegistry,
            LoginSuccessHandler loginSuccessHandler,
            AuthenticationFailureHandler authenticationFailureHandler) throws Exception {
        log.info("Configuring Spring Security with successHandler={}",
                loginSuccessHandler.getClass().getName());
        return http
                .authenticationProvider(ldapAuthenticationProvider)
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(
                                "/login", "/session-expired", "/assets/**", "/favicon.ico",
                                "/.well-known/appspecific/com.chrome.devtools.json", "/error")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionFixation(fixation -> fixation.changeSessionId())
                        .invalidSessionUrl("/session-expired")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/session-expired")
                        .sessionRegistry(sessionRegistry))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .build();
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String username = request.getParameter("username");
            if (exception instanceof SessionAuthenticationException) {
                log.warn("Concurrent login rejected: username={}", username);
                response.sendRedirect(request.getContextPath() + "/login?concurrent");
                return;
            }
            log.warn("Login rejected: username={} exception={}", username, exception.getClass().getSimpleName());
            response.sendRedirect(request.getContextPath() + "/login?error");
        };
    }
}

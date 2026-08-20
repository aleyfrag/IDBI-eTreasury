package in.co.idbibank.etreasury.config;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldif.LDIFReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import java.io.InputStream;

@Configuration
public class EmbeddedLdapConfig {

    @Bean(destroyMethod = "shutDown")
    InMemoryDirectoryServer directoryServer(
            @Value("${app.ldap.port:8389}") int port,
            @Value("${app.ldap.base-dn:dc=idbi,dc=bank}") String baseDn) throws Exception {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(baseDn);
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("ldap", port));
        config.setSchema(null);

        InMemoryDirectoryServer server = new InMemoryDirectoryServer(config);
        try (InputStream input = getClass().getResourceAsStream("/ldap/users.ldif")) {
            if (input == null) {
                throw new IllegalStateException("LDAP seed file /ldap/users.ldif is missing");
            }
            server.importFromLDIF(true, new LDIFReader(input));
        }
        server.startListening();
        return server;
    }

    @Bean
    LdapTemplate ldapTemplate(
            InMemoryDirectoryServer directoryServer,
            @Value("${app.ldap.base-dn:dc=idbi,dc=bank}") String baseDn) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://127.0.0.1:" + directoryServer.getListenPort());
        contextSource.setBase(baseDn);
        contextSource.afterPropertiesSet();
        return new LdapTemplate(contextSource);
    }
}

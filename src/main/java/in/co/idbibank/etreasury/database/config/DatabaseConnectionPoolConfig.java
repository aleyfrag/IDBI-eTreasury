package in.co.idbibank.etreasury.database.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import in.co.idbibank.etreasury.database.security.EncryptDecryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConnectionPoolConfig {

    private static final Logger log =
            LoggerFactory.getLogger(DatabaseConnectionPoolConfig.class);

    @Bean
    EncryptDecryptUtil encryptDecryptUtil() {
        return new EncryptDecryptUtil();
    }

    @Bean
    HikariConfig oracleHikariConfig(
            EncryptDecryptUtil encryptDecryptUtil,
            @Value("${app.datasource.driver-class-name}") String driverClassName,
            @Value("${app.datasource.jdbc-url}") String jdbcUrl,
            @Value("${app.datasource.username}") String encryptedUsername,
            @Value("${app.datasource.password}") String encryptedPassword,
            @Value("${app.datasource.encryption-key}") String encryptionKey,
            @Value("${app.datasource.pool-name}") String poolName,
            @Value("${app.datasource.maximum-pool-size}") int maximumPoolSize,
            @Value("${app.datasource.minimum-idle}") int minimumIdle,
            @Value("${app.datasource.connection-timeout}") long connectionTimeout,
            @Value("${app.datasource.idle-timeout}") long idleTimeout,
            @Value("${app.datasource.max-lifetime}") long maxLifetime,
            @Value("${app.datasource.validation-timeout}") long validationTimeout) {

        var config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        // Decrypt credentials only in memory. Never write them to application logs.
        config.setUsername(encryptDecryptUtil.decrypt(
                encryptedUsername.trim(),
                encryptionKey.trim()
        ));
        config.setPassword(encryptDecryptUtil.decrypt(
                encryptedPassword.trim(),
                encryptionKey.trim()
        ));
        config.setPoolName(poolName);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setValidationTimeout(validationTimeout);
        return config;
    }

    @Bean(destroyMethod = "close")
    DataSource oracleDataSource(HikariConfig oracleHikariConfig) {
        log.info(
                "Creating Oracle connection pool: poolName={}, jdbcUrl={}",
                oracleHikariConfig.getPoolName(),
                oracleHikariConfig.getJdbcUrl()
        );
        return new HikariDataSource(oracleHikariConfig);
    }
}

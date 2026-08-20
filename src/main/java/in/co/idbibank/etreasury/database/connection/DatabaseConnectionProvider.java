package in.co.idbibank.etreasury.database.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionProvider {

    private static final Logger log =
            LoggerFactory.getLogger(DatabaseConnectionProvider.class);

    private final DataSource dataSource;

    public DatabaseConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        log.debug("Borrowing an Oracle connection from the connection pool");
        return dataSource.getConnection();
    }
}

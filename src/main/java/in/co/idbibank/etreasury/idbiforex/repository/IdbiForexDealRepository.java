package in.co.idbibank.etreasury.idbiforex.repository;

import in.co.idbibank.etreasury.idbiforex.exception.IdbiForexDataException;
import in.co.idbibank.etreasury.idbiforex.model.IdbiForexDeal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Repository
public class IdbiForexDealRepository {

    private static final Logger log = LoggerFactory.getLogger(IdbiForexDealRepository.class);

    private static final String PACKAGE_NAME = "IFXPRO_DEALING";
    private static final String PROCEDURE_NAME = "DEALS_FOR_SAME_SOL_SP";
    private static final String USER_ID_PARAMETER = "USR_ID";
    private static final String ACTION_PARAMETER = "ACT";
    private static final String APPLICATION_DATE_PARAMETER = "APP_DATE";
    private static final String SOL_ID_PARAMETER = "I_SOL";
    private static final String CURSOR_PARAMETER = "DEAL_FOR_SAME_SOL";

    private final SimpleJdbcCall dealListCall;

    /**
     * Declares the legacy five-position Oracle package call without metadata lookup.
     */
    public IdbiForexDealRepository(JdbcTemplate jdbcTemplate) {
        this.dealListCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(PACKAGE_NAME)
                .withProcedureName(PROCEDURE_NAME)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(USER_ID_PARAMETER, Types.VARCHAR),
                        new SqlParameter(ACTION_PARAMETER, Types.VARCHAR),
                        new SqlParameter(APPLICATION_DATE_PARAMETER, Types.DATE),
                        new SqlParameter(SOL_ID_PARAMETER, Types.VARCHAR),
                        new SqlOutParameter(CURSOR_PARAMETER, Types.REF_CURSOR,
                                IdbiForexDealRepository::mapDeal)
                );
    }

    /**
     * Fetches deals visible to one user/SOL for the selected transaction date.
     */
    public List<IdbiForexDeal> findDeals(
            String userId,
            String action,
            LocalDate applicationDate,
            String solId) {
        validateInput(userId, "User ID");
        validateInput(action, "Action");
        if (applicationDate == null) {
            throw new IdbiForexDataException("Application date is required");
        }
        validateInput(solId, "SOL ID");

        try {
            Map<String, Object> result = dealListCall.execute(new MapSqlParameterSource()
                    .addValue(USER_ID_PARAMETER, userId.trim())
                    .addValue(ACTION_PARAMETER, action.trim())
                    .addValue(APPLICATION_DATE_PARAMETER, Date.valueOf(applicationDate), Types.DATE)
                    .addValue(SOL_ID_PARAMETER, solId.trim()));

            Object cursor = result.get(CURSOR_PARAMETER);
            if (!(cursor instanceof List<?> rows)) {
                throw new IdbiForexDataException("Deal procedure did not return a cursor");
            }

            List<IdbiForexDeal> deals;
            try {
                deals = rows.stream()
                        .filter(IdbiForexDeal.class::isInstance)
                        .map(IdbiForexDeal.class::cast)
                        .toList();
            } catch (RuntimeException exception) {
                throw new IdbiForexDataException("Unexpected deal record returned by Oracle", exception);
            }

            log.info("Loaded {} IDBIForex deals for userId={} action={} solId={} applicationDate={}",
                    deals.size(), userId, action, solId, applicationDate);
            return deals;
        } catch (DataAccessException exception) {
            if (isOracleNoDataFound(exception)) {
                log.info("No IDBIForex deals returned by {}.{} for userId={} action={} solId={} applicationDate={}",
                        PACKAGE_NAME, PROCEDURE_NAME, userId, action, solId, applicationDate);
                return List.of();
            }

            log.error("Unable to call {}.{} for userId={} action={} solId={} applicationDate={}",
                    PACKAGE_NAME, PROCEDURE_NAME, userId, action, solId, applicationDate, exception);
            throw new IdbiForexDataException("Unable to load IDBIForex deals", exception);
        }
    }

    /**
     * Identifies Oracle ORA-01403 anywhere in Spring's nested JDBC exception chain.
     * The legacy procedure raises this when its internal lookup finds no deal rows.
     */
    static boolean isOracleNoDataFound(Throwable failure) {
        for (Throwable current = failure; current != null; current = current.getCause()) {
            if (current instanceof SQLException sqlException && sqlException.getErrorCode() == 1403) {
                return true;
            }
            String message = current.getMessage();
            if (message != null && message.contains("ORA-01403")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Maps one cursor row. Nullable Oracle columns remain null rather than causing conversion errors.
     */
    static IdbiForexDeal mapDeal(ResultSet resultSet, int rowNumber) throws SQLException {
        Set<String> columns = cursorColumns(resultSet);
        String cardRates = optionalString(resultSet, columns, "CARD_RATES");
        return new IdbiForexDeal(
                optionalString(resultSet, columns, "DEAL_NO"),
                optionalString(resultSet, columns, "APP_SYSDATE"),
                optionalString(resultSet, columns, "DEAL_TYPE"),
                optionalString(resultSet, columns, "CUST_ID"),
                optionalString(resultSet, columns, "CUST_NAME"),
                cardRates,
                optionalString(resultSet, columns, "BRANCH_CODE", "BRNACH_CODE"),
                optionalString(resultSet, columns, "TRANSACTION_TYPE"),
                optionalString(resultSet, columns, "CURRENCY1"),
                optionalString(resultSet, columns, "CURRENCY2"),
                optionalDecimal(resultSet, columns, "AMOUNT"),
                optionalString(resultSet, columns, "VALUE_DATE1"),
                optionalString(resultSet, columns, "VALUE_DATE2"),
                optionalString(resultSet, columns, "RATE"),
                optionalString(resultSet, columns, "TYPE"),
                optionalString(resultSet, columns, "NOSTRO_ACCOUNT"),
                optionalString(resultSet, columns, "ORIGINAL_RATE"),
                optionalString(resultSet, columns, "IS_PREF_CUST"),
                optionalString(resultSet, columns, "ACT_HFLOOR"),
                optionalString(resultSet, columns, "ACT_LFLOOR"),
                optionalString(resultSet, columns, "CUST_FLOOR"),
                optionalString(resultSet, columns, "PROFIT_LOSS"),
                optionalString(resultSet, columns, "REMARKS"),
                optionalString(resultSet, columns, "SPOT_RATE"),
                optionalString(resultSet, columns, "FORWARD_PREMIA"),
                optionalString(resultSet, columns, "MARGIN"),
                optionalString(resultSet, columns, "NET_RATE"),
                optionalString(resultSet, columns, "BOOKED_BY"),
                optionalString(resultSet, columns, "BOOKING_DATE"),
                optionalString(resultSet, columns, "CONFIRMED_BY"),
                optionalString(resultSet, columns, "CONFIRMATION_DATE"),
                optionalString(resultSet, columns, "STATUS"),
                optionalString(resultSet, columns, "TRADE_DATE"),
                optionalString(resultSet, columns, "SPOT_DATE"),
                optionalString(resultSet, columns, "MATURITY_DATE"),
                optionalString(resultSet, columns, "SOL_ID"),
                optionalString(resultSet, columns, "OPTION_DATE"),
                optionalString(resultSet, columns, "TRSY_DEALER"),
                optionalString(resultSet, columns, "TRSY_TRANSFER_DATE"),
                optionalString(resultSet, columns, "TRANSMITTED_Y_N"),
                optionalString(resultSet, columns, "MARGINID"),
                optionalString(resultSet, columns, "DEALREQID"),
                optionalString(resultSet, columns, "SWAPCHARGES"),
                optionalString(resultSet, columns, "FWDCONTNUM"),
                optionalString(resultSet, columns, "DUMMY"),
                optionalString(resultSet, columns, "USER_SOL"),
                optionalString(resultSet, columns, "VERTCODE"),
                optionalString(resultSet, columns, "UDR"),
                optionalString(resultSet, columns, "FUND"),
                optionalString(resultSet, columns, "UC_TYPE")
        );
    }

    /** Reads cursor labels once so absent legacy/optional columns do not cause ORA-17006. */
    private static Set<String> cursorColumns(ResultSet resultSet) throws SQLException {
        Set<String> columns = new HashSet<>();
        var metadata = resultSet.getMetaData();
        for (int columnIndex = 1; columnIndex <= metadata.getColumnCount(); columnIndex++) {
            String label = metadata.getColumnLabel(columnIndex);
            String name = metadata.getColumnName(columnIndex);
            if (label != null) {
                columns.add(label.toUpperCase(Locale.ROOT));
            }
            if (name != null) {
                columns.add(name.toUpperCase(Locale.ROOT));
            }
        }
        return columns;
    }

    /** Reads the first column name that is actually present in the returned cursor. */
    private static String optionalString(
            ResultSet resultSet,
            Set<String> availableColumns,
            String... columnNames) throws SQLException {
        for (String columnName : columnNames) {
            if (availableColumns.contains(columnName.toUpperCase(Locale.ROOT))) {
                String value = resultSet.getString(columnName);
                return value == null ? null : value.trim();
            }
        }
        return null;
    }

    /** Reads a numeric value only when the cursor contains that column. */
    private static BigDecimal optionalDecimal(
            ResultSet resultSet,
            Set<String> availableColumns,
            String columnName) throws SQLException {
        return availableColumns.contains(columnName.toUpperCase(Locale.ROOT))
                ? resultSet.getBigDecimal(columnName)
                : null;
    }

    private void validateInput(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new IdbiForexDataException(fieldName + " is required");
        }
    }
}

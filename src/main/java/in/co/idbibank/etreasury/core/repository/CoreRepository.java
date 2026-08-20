package in.co.idbibank.etreasury.core.repository;

import in.co.idbibank.etreasury.admin.menu.AdminMenuRoutes;
import in.co.idbibank.etreasury.core.exception.CoreDataException;
import in.co.idbibank.etreasury.core.exception.UserNotFoundException;
import in.co.idbibank.etreasury.core.model.MenuItem;
import in.co.idbibank.etreasury.core.model.SubMenuItem;
import in.co.idbibank.etreasury.core.model.TreasuryUserDetails;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class CoreRepository {

    private static final Logger log = LoggerFactory.getLogger(CoreRepository.class);

    private static final String PACKAGE_NAME = "EB_CORE_SLAB_NEW";
    private static final String USER_DETAILS_PROCEDURE = "GET_USER_DETAILS";
    private static final String USER_MENU_PROCEDURE = "GET_USER_MENU";
    private static final String EIN_PARAMETER = "P_EIN_NO";
    private static final String CURSOR_PARAMETER = "P_CURSOR";
    private static final String STATUS_PARAMETER = "P_STATUS";
    private static final String MESSAGE_PARAMETER = "P_MESSAGE";

    private final SimpleJdbcCall userDetailsCall;
    private final SimpleJdbcCall userMenuCall;

    /**
     * Configures reusable Oracle stored-procedure calls for user and menu details.
     * Procedure metadata lookup is disabled because all parameters are declared explicitly.
     */
    public CoreRepository(JdbcTemplate jdbcTemplate) {
        this.userDetailsCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(PACKAGE_NAME)
                .withProcedureName(USER_DETAILS_PROCEDURE)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(EIN_PARAMETER, Types.VARCHAR),
                        new SqlOutParameter(CURSOR_PARAMETER, Types.REF_CURSOR,
                                this::mapUserDetails),
                        new SqlOutParameter(STATUS_PARAMETER, Types.VARCHAR),
                        new SqlOutParameter(MESSAGE_PARAMETER, Types.VARCHAR)
                );

        this.userMenuCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(PACKAGE_NAME)
                .withProcedureName(USER_MENU_PROCEDURE)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(EIN_PARAMETER, Types.VARCHAR),
                        new SqlOutParameter(CURSOR_PARAMETER, Types.REF_CURSOR,
                                this::mapMenuRow),
                        new SqlOutParameter(STATUS_PARAMETER, Types.VARCHAR),
                        new SqlOutParameter(MESSAGE_PARAMETER, Types.VARCHAR)
                );

        log.info("CoreRepository initialized: package={} procedures=[{}, {}]",
                PACKAGE_NAME, USER_DETAILS_PROCEDURE, USER_MENU_PROCEDURE);
    }

    /**
     * Calls GET_USER_DETAILS for the supplied EIN and returns exactly one mapped user record.
     */
    public TreasuryUserDetails getUserDetails(String einNumber) {
        log.info("Calling {}.{} for EIN={}", PACKAGE_NAME, USER_DETAILS_PROCEDURE, einNumber);

        try {
            Map<String, Object> result = userDetailsCall.execute(inputParameters(einNumber));
            validateProcedureResult(result, USER_DETAILS_PROCEDURE);

            List<TreasuryUserDetails> records = readCursor(result, TreasuryUserDetails.class);
            records.forEach(record -> log.info("User detail record: {}", record));

            if (records.isEmpty()) {
                throw new CoreDataException("User details procedure returned no record");
            }
            if (records.size() > 1) {
                throw new CoreDataException("User details procedure returned multiple records");
            }

            return records.getFirst();
        } catch (DataAccessException exception) {
            log.error("Unable to load user details for EIN={}", einNumber, exception);
            throw new CoreDataException("Unable to load user details", exception);
        }
    }

    /**
     * Calls GET_USER_MENU for the supplied EIN and converts its flat cursor rows into
     * a main-menu and submenu hierarchy for the frontend.
     */
    public List<MenuItem> getMenuDetails(String einNumber) {
        log.info("Calling {}.{} for EIN={}", PACKAGE_NAME, USER_MENU_PROCEDURE, einNumber);

        try {
            Map<String, Object> result = userMenuCall.execute(inputParameters(einNumber));
            validateProcedureResult(result, USER_MENU_PROCEDURE);

            List<MenuRow> rows = readCursor(result, MenuRow.class);
            rows.forEach(row -> log.info("Menu cursor record: {}", row));

            List<MenuItem> menus = createMenuTree(rows);
            log.info("Final menus for EIN={}: {}", einNumber, menus);
            return List.copyOf(menus);
        } catch (DataAccessException exception) {
            log.error("Unable to load menus for EIN={}", einNumber, exception);
            throw new CoreDataException("Unable to load menu details", exception);
        }
    }

    /**
     * Validates, trims and normalizes an EIN before passing it to an Oracle procedure.
     */
    private MapSqlParameterSource inputParameters(String einNumber) {
        if (!StringUtils.hasText(einNumber)) {
            throw new CoreDataException("EIN number is required");
        }
        return new MapSqlParameterSource(
                EIN_PARAMETER,
                einNumber.trim().toUpperCase(Locale.ROOT)
        );
    }

    /**
     * Reads P_STATUS and P_MESSAGE and converts unsuccessful procedure results into
     * application exceptions. A missing user receives the specific user-not-found exception.
     */
    private void validateProcedureResult(Map<String, Object> result, String procedureName) {
        String status = result.get(STATUS_PARAMETER) == null
                ? null
                : result.get(STATUS_PARAMETER).toString();
        String message = result.get(MESSAGE_PARAMETER) == null
                ? "No message returned"
                : result.get(MESSAGE_PARAMETER).toString();

        log.info("Procedure {} returned status={} message={}", procedureName, status, message);
        if (!"SUCCESS".equalsIgnoreCase(status)) {
            if (USER_DETAILS_PROCEDURE.equals(procedureName)
                    && "FAIL".equalsIgnoreCase(status)) {
                throw new UserNotFoundException(message);
            }
            throw new CoreDataException(procedureName + " failed: " + message);
        }
    }

    /**
     * Extracts the mapped P_CURSOR list and verifies that every entry has the expected type.
     * This method does not execute another database call.
     */
    private <T> List<T> readCursor(Map<String, Object> result, Class<T> recordType) {
        Object cursorResult = result.get(CURSOR_PARAMETER);
        if (!(cursorResult instanceof List<?> rows)) {
            throw new CoreDataException(
                    "Procedure did not return cursor parameter " + CURSOR_PARAMETER
            );
        }

        try {
            return rows.stream().map(recordType::cast).toList();
        } catch (ClassCastException exception) {
            throw new CoreDataException("Unexpected record type returned by procedure", exception);
        }
    }

    /**
     * Maps one GET_USER_DETAILS cursor row into an immutable TreasuryUserDetails record.
     * Spring invokes this callback once for each row returned by Oracle.
     */
    private TreasuryUserDetails mapUserDetails(ResultSet resultSet, int rowNumber)
            throws SQLException {
        return new TreasuryUserDetails(
                resultSet.getLong("USER_ID"),
                resultSet.getString("USER_CODE"),
                resultSet.getString("EIN_NO"),
                resultSet.getString("SOL_ID"),
                resultSet.getString("ROLE"),
                resultSet.getString("STATUS"),
                resultSet.getString("RIGHTS"),
                resultSet.getString("ISBULK_UPLOAD"),
                resultSet.getString("ADM_STATUS"),
                resultSet.getString("CREATED_BY"),
                getLocalDateTime(resultSet, "CREATION_TIME"),
                resultSet.getString("MODIFIED_BY"),
                getLocalDateTime(resultSet, "MODIFIED_ON"),
                resultSet.getString("APPR_RJCT_BY"),
                getLocalDateTime(resultSet, "APPR_RJCT_ON")
        );
    }

    /**
     * Maps one GET_USER_MENU cursor row. ID and MENU_ID are numeric keys, while
     * APP_ID and ROLE_ID are application and role codes such as IDBIForex and MCD.
     */
    private MenuRow mapMenuRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new MenuRow(
                resultSet.getLong("ID"),
                resultSet.getString("APP_ID"),
                resultSet.getString("ROLE_ID"),
                resultSet.getLong("MENU_ID"),
                resultSet.getString("MAIN_MENU"),
                resultSet.getString("SUB_MENU"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getString("URL")
        );
    }

    /**
     * Converts a nullable SQL timestamp column into a nullable Java LocalDateTime.
     */
    private LocalDateTime getLocalDateTime(ResultSet resultSet, String columnName)
            throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    /**
     * Groups flat menu cursor rows by numeric ID, assigns ROLE_ID to each main menu,
     * treats a blank SUB_MENU row as the main-menu home page, adds non-blank submenus
     * and removes duplicate submenu rows using MENU_ID.
     */
    private List<MenuItem> createMenuTree(List<MenuRow> rows) {
        Map<Long, MutableMenu> menuMap = new LinkedHashMap<>();

        for (MenuRow row : rows) {
            if (row.id() <= 0) {
                throw new CoreDataException("Menu cursor returned an invalid ID");
            }
            if (!StringUtils.hasText(row.mainMenu())) {
                throw new CoreDataException("Menu cursor returned a row without MAIN_MENU");
            }

            String mainMenuName = displayNameForMainMenu(row.mainMenu());
            MutableMenu menu = menuMap.get(row.id());
            if (menu == null) {
                menu = new MutableMenu(row.id(), mainMenuName, row.roleId());
                menuMap.put(row.id(), menu);
            } else if (!StringUtils.hasText(menu.roleId) && StringUtils.hasText(row.roleId())) {
                menu.roleId = row.roleId().trim();
            }

            if (!StringUtils.hasText(row.subMenu())) {
                menu.pageUrl = toPageUrl(row.url());
                continue;
            }

            String subMenuName = row.subMenu().trim();
            if (row.menuId() <= 0) {
                throw new CoreDataException("Menu cursor returned an invalid MENU_ID");
            }
            if (menu.subMenuIds.add(row.menuId())) {
                menu.subMenus.add(new SubMenuItem(
                        row.menuId(),
                        subMenuName,
                        iconForNavigationItem(subMenuName),
                        AdminMenuRoutes.resolveSubMenuUrl(
                                mainMenuName,
                                subMenuName,
                                toPageUrl(row.url())
                        )
                ));
            }
        }

        return menuMap.values().stream().map(MutableMenu::toMenuItem).toList();
    }

    /**
     * Normalizes an internal menu URL and returns # when Oracle supplies no URL.
     */
    private String toPageUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return "#";
        }
        String normalizedUrl = url.trim();
        if (normalizedUrl.startsWith("<")) {
            log.warn("Ignoring legacy HTML stored in menu URL column");
            return "#";
        }
        if (normalizedUrl.startsWith("/") || normalizedUrl.startsWith("#")) {
            return normalizedUrl;
        }
        return "/" + normalizedUrl;
    }

    /**
     * Selects an offline Lucide icon for a known menu name and returns a default
     * grid icon for menu names that are not yet configured.
     */
    private static String iconForNavigationItem(String itemName) {
        String normalizedName = itemName == null
                ? ""
                : itemName.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);

        return switch (normalizedName) {
            case "ADMIN" -> "settings";
            case "IDBIFX" -> "circle-dollar-sign";
            case "IDBIFXDEALS" -> "handshake";
            case "IDBIFXDEALLIST", "DEALLIST",
                    "IDBIFXREPORTEDDEALSLIST", "REPORTEDDEALSLIST" -> "list-checks";
            case "IDBIFXLIMITS", "LIMITS" -> "gauge";
            case "CARRYFORWARD" -> "calendar-sync";
            case "FUNDTRANSFER" -> "arrow-left-right";
            case "EXPFWDOUTFLOW", "EXPORTFORWARDOUTFLOW" -> "trending-down";
            case "MERCHANTDEALS", "MERCHENTDEALS" -> "store";
            case "INTERBANKDEALS" -> "landmark";
            default -> "layout-grid";
        };
    }

    /**
     * Keeps database menu identifiers unchanged while presenting every admin-named
     * main menu (for example FXAdminNew) consistently as Admin in the frontend.
     */
    private static String displayNameForMainMenu(String mainMenuName) {
        String trimmedName = mainMenuName.trim();
        return trimmedName.toLowerCase(Locale.ROOT).contains("admin")
                ? "Admin"
                : trimmedName;
    }

    private record MenuRow(
            long id,
            String appId,
            String roleId,
            long menuId,
            String mainMenu,
            String subMenu,
            String description,
            String url) {
    }

    private static final class MutableMenu {

        private final long menuId;
        private final String menuName;
        private String roleId;
        private String pageUrl = "#";
        private final List<SubMenuItem> subMenus = new ArrayList<>();
        private final Set<Long> subMenuIds = new LinkedHashSet<>();

        /**
         * Creates a temporary mutable main menu while cursor rows are being grouped.
         */
        private MutableMenu(long menuId, String menuName, String roleId) {
            this.menuId = menuId;
            this.menuName = menuName;
            this.roleId = StringUtils.hasText(roleId) ? roleId.trim() : null;
        }

        /**
         * Converts the completed mutable menu into the immutable MenuItem stored in session.
         */
        private MenuItem toMenuItem() {
            return new MenuItem(
                    menuId,
                    menuName,
                    iconForNavigationItem(menuName),
                    roleId,
                    pageUrl,
                    subMenus
            );
        }
    }
}

package in.co.idbibank.etreasury.admin.menu;

import java.util.Locale;

/** Contains the small set of application routes owned by the Admin module. */
public final class AdminMenuRoutes {

    public static final String USER_MASTER_URL = "/admin/user-master";

    private AdminMenuRoutes() {
        // Utility class; it must not be instantiated.
    }

    /**
     * Replaces only the Admin > User Master database URL. All other menu URLs
     * continue to use the value returned by the stored procedure.
     */
    public static String resolveSubMenuUrl(
            String mainMenuName,
            String subMenuName,
            String databaseUrl) {
        return isAdmin(mainMenuName) && normalized(subMenuName).equals("USERMASTER")
                ? USER_MASTER_URL
                : databaseUrl;
    }

    /** Returns true for the normalized Admin display name. */
    public static boolean isAdmin(String menuName) {
        return normalized(menuName).contains("ADMIN");
    }

    /** Removes punctuation and spaces so database naming differences are harmless. */
    private static String normalized(String value) {
        return value == null
                ? ""
                : value.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
    }
}

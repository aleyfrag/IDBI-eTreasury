package in.co.idbibank.etreasury.admin.security;

import in.co.idbibank.etreasury.core.model.MenuItem;
import in.co.idbibank.etreasury.core.model.TreasuryUserDetails;

import java.util.List;

/** Applies the Admin authorization rule in one place. */
public final class AdminAccessPolicy {

    private AdminAccessPolicy() {
    }

    public static boolean canAccess(TreasuryUserDetails user, List<MenuItem> menus) {
        if (user == null || menus == null) {
            return false;
        }

        return menus.stream().anyMatch(menu ->
                equalsCode(menu.menuName(), "eBlotter")
                        && equalsCode(menu.roleId(), "MCD"));
    }

    private static boolean equalsCode(String actual, String expected) {
        return actual != null
                && expected.equalsIgnoreCase(actual.trim().replaceAll("[^A-Za-z0-9]", ""));
    }
}

package in.co.idbibank.etreasury.admin.menu;

import in.co.idbibank.etreasury.admin.model.AdminTab;

import java.util.List;

/**
 * Single place for maintaining User Master tabs.
 * To add a future tab, add one AdminTab entry to TABS and its matching JSP panel.
 */
public final class AdminUserMasterTabs {

    private static final List<AdminTab> TABS = List.of(
            new AdminTab("user-master", "User Master", "users"),
            new AdminTab("deal-type-master", "Deal Type Master", "handshake"),
            new AdminTab("nostro-master", "Nostro Master", "landmark"),
            new AdminTab("currency-master", "Currency Master", "coins"),
            new AdminTab("reporting-sol", "Reporting SOL", "building-2"),
            new AdminTab("user-bulk-upload", "User Bulk Upload", "file-up")
    );

    private AdminUserMasterTabs() {
        // Utility class; it must not be instantiated.
    }

    /** Returns an immutable tab list for the controller and JSP. */
    public static List<AdminTab> all() {
        return TABS;
    }
}

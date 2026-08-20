package in.co.idbibank.etreasury.admin.controller;

import in.co.idbibank.etreasury.admin.exception.AdminPageException;
import in.co.idbibank.etreasury.admin.menu.AdminMenuRoutes;
import in.co.idbibank.etreasury.admin.menu.AdminUserMasterTabs;
import in.co.idbibank.etreasury.admin.usermanagement.service.UserManagementService;
import in.co.idbibank.etreasury.core.model.MenuItem;
import in.co.idbibank.etreasury.core.model.SubMenuItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

/** Handles pages belonging to the Admin main menu. */
@Controller
public class AdminController {
    private final UserManagementService userManagementService;

    public AdminController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /** Displays the first Admin feature: the hardcoded User Master tab workspace. */
    @GetMapping(AdminMenuRoutes.USER_MASTER_URL)
    public String userMaster(HttpSession session, Model model) {
        MenuLocation location = findUserMasterMenu(session);

        // These IDs keep Admin and User Master selected in the shared sidebar.
        model.addAttribute("activeMenuId", location.mainMenuId());
        model.addAttribute("activeSubMenuId", location.subMenuId());

        // The JSP renders these hardcoded items as Chrome-style tabs.
        model.addAttribute("adminTabs", AdminUserMasterTabs.all());
        model.addAttribute("solOptions", userManagementService.sols());
        model.addAttribute("userRecords", userManagementService.users());
        return "admin/user-master";
    }

    /** Finds Admin > User Master in the menus saved during login. */
    private MenuLocation findUserMasterMenu(HttpSession session) {
        for (MenuItem menu : sessionMenus(session)) {
            if (!AdminMenuRoutes.isAdmin(menu.menuName())) {
                continue;
            }
            for (SubMenuItem subMenu : menu.subMenus()) {
                if (AdminMenuRoutes.USER_MASTER_URL.equals(subMenu.pageUrl())) {
                    return new MenuLocation(menu.menuId(), subMenu.subMenuId());
                }
            }
        }
        throw new AdminPageException("Admin User Master menu is not assigned to this user");
    }

    /** Safely reads the menu list from the authenticated HTTP session. */
    private List<MenuItem> sessionMenus(HttpSession session) {
        Object menus = session.getAttribute("userMenus");
        if (!(menus instanceof List<?> values)) {
            throw new AdminPageException("User menu information is missing from the session");
        }
        return values.stream()
                .filter(MenuItem.class::isInstance)
                .map(MenuItem.class::cast)
                .toList();
    }

    /** Stores the main-menu and submenu IDs needed by the shared sidebar. */
    private record MenuLocation(long mainMenuId, long subMenuId) {
    }
}

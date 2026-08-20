package in.co.idbibank.etreasury.core.model;

import java.util.List;

public record MenuItem(
        long menuId,
        String menuName,
        String iconName,
        String roleId,
        String pageUrl,
        List<SubMenuItem> subMenus) {

    public MenuItem {
        subMenus = List.copyOf(subMenus);
    }
}

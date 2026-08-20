async function loadSharedComponents() {
  const includes = [...document.querySelectorAll("[data-include]")];

  await Promise.all(includes.map(async (target) => {
    const response = await fetch(target.dataset.include);
    if (!response.ok) throw new Error(`Unable to load ${target.dataset.include}`);
    target.outerHTML = await response.text();
  }));
}

function initializeNavigation() {
  const currentPage = document.body.dataset.page;
  document.querySelector(`[data-nav="${currentPage}"]`)?.classList.add("active");
  if (currentPage === "deal-form" || currentPage === "deal-list") {
    document.querySelector('[data-nav-parent="forex"]')?.classList.add("active");
  }

  document.querySelectorAll("[data-sidebar-toggle]").forEach((button) => {
    button.addEventListener("click", () => {
      const desktop = window.matchMedia("(min-width: 992px)").matches;
      const sidebar = document.querySelector(".sidebar");
      if (desktop) document.body.classList.toggle("sidebar-collapsed");
      else sidebar?.classList.toggle("show");
      button.setAttribute("aria-expanded", desktop
        ? String(!document.body.classList.contains("sidebar-collapsed"))
        : String(sidebar?.classList.contains("show")));
    });
  });
}

function initializeCoreMenus() {
  const menuButtons = [...document.querySelectorAll("[data-core-menu-target]")];
  const submenuPanels = [...document.querySelectorAll("[data-core-submenu-panel]")];
  const submenuLinks = [...document.querySelectorAll("[data-core-submenu-link]")];
  if (!menuButtons.length) return;

  const availableMenuIds = new Set(menuButtons.map((button) => button.dataset.coreMenuTarget));
  const requestedMenuId = document.body.dataset.activeMenuId;
  const initialMenuId = availableMenuIds.has(requestedMenuId)
    ? requestedMenuId
    : null;

  const activateMenu = (menuId, preferredSubMenuId) => {
    menuButtons.forEach((button) => {
      const selected = button.dataset.coreMenuTarget === menuId;
      button.classList.toggle("active", selected);
      button.setAttribute("aria-expanded", String(selected));
      if (selected) button.setAttribute("aria-current", "page");
      else button.removeAttribute("aria-current");
    });
    submenuPanels.forEach((panel) => {
      panel.classList.toggle("d-none", panel.dataset.coreSubmenuPanel !== menuId);
    });

    const assignedSubMenus = submenuLinks.filter(
      (link) => link.dataset.coreSubmenuLink === menuId
    );
    const selectedSubMenuId = assignedSubMenus.some(
      (link) => link.dataset.coreSubmenuId === preferredSubMenuId
    )
      ? preferredSubMenuId
      : assignedSubMenus[0]?.dataset.coreSubmenuId;
    submenuLinks.forEach((link) => {
      link.classList.toggle(
        "active",
        link.dataset.coreSubmenuLink === menuId
          && link.dataset.coreSubmenuId === selectedSubMenuId
      );
    });

    sessionStorage.setItem("idbiActiveMenuId", menuId);
    if (selectedSubMenuId) {
      sessionStorage.setItem("idbiActiveSubMenuId", selectedSubMenuId);
    }
  };

  // Closes an already-open main menu without changing the current page.
  const collapseMenu = (menuId) => {
    menuButtons.forEach((button) => {
      if (button.dataset.coreMenuTarget !== menuId) return;
      button.classList.remove("active");
      button.setAttribute("aria-expanded", "false");
      button.removeAttribute("aria-current");
    });
    submenuPanels.forEach((panel) => {
      if (panel.dataset.coreSubmenuPanel === menuId) panel.classList.add("d-none");
    });
  };

  menuButtons.forEach((button) => {
    button.addEventListener("click", (event) => {
      // Main-menu clicks control expansion; submenu links perform navigation.
      event.preventDefault();
      const menuId = button.dataset.coreMenuTarget;
      const isExpanded = button.getAttribute("aria-expanded") === "true";
      sessionStorage.removeItem("idbiActiveSubMenuId");
      if (isExpanded) collapseMenu(menuId);
      else activateMenu(menuId);
    });
  });

  submenuLinks.forEach((link) => {
    link.addEventListener("click", () => {
      sessionStorage.setItem("idbiActiveMenuId", link.dataset.coreSubmenuLink);
      sessionStorage.setItem("idbiActiveSubMenuId", link.dataset.coreSubmenuId);
    });
  });

  const requestedSubMenuId = document.body.dataset.activeSubmenuId;
  // Start with main menus only. Open a group automatically only when the page
  // explicitly identifies its active main menu.
  if (initialMenuId) activateMenu(initialMenuId, requestedSubMenuId);
}

function initializeReferenceFooter() {
  document.querySelectorAll("[data-reference-footer]").forEach((footer) => {
    const toggle = footer.querySelector("[data-reference-footer-toggle]");
    const content = footer.querySelector("[data-reference-footer-content]");
    const label = footer.querySelector("[data-reference-footer-label]");
    if (!toggle || !content) return;

    const setExpanded = (expanded) => {
      footer.classList.toggle("is-expanded", expanded);
      toggle.setAttribute("aria-expanded", String(expanded));
      content.setAttribute("aria-hidden", String(!expanded));
      if (label) label.textContent = expanded ? "Hide links" : "Show links";
    };

    toggle.addEventListener("click", () => {
      setExpanded(!footer.classList.contains("is-expanded"));
    });

    footer.addEventListener("click", (event) => {
      if (event.target.closest("button, a")) return;
      setExpanded(!footer.classList.contains("is-expanded"));
    });

    setExpanded(false);
  });
}

async function initializeApp() {
  try {
    await loadSharedComponents();
  } catch (error) {
    console.error(error);
  }

  initializeNavigation();
  initializeCoreMenus();
  initializeReferenceFooter();
  if (window.lucide) window.lucide.createIcons();

  document.querySelectorAll("[data-password-toggle]").forEach((button) => {
    button.addEventListener("click", () => {
      const input = document.getElementById(button.dataset.passwordToggle);
      if (!input) return;
      input.type = input.type === "password" ? "text" : "password";
      button.innerHTML = `<i data-lucide="${input.type === "password" ? "eye" : "eye-off"}" class="icon-sm"></i>`;
      window.lucide?.createIcons();
    });
  });
}

if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", initializeApp);
else initializeApp();

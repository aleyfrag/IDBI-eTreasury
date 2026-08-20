<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="topbar d-flex align-items-center px-3 px-lg-4">
  <button class="btn btn-light me-2" type="button" data-sidebar-toggle aria-label="Toggle navigation" aria-expanded="true">
    <i data-lucide="menu"></i>
  </button>
  <a class="brand-mark" href="/home">
    <span class="brand-icon"></span>
    <span>
      <span class="brand-name"><span class="text-idbi">IDBI</span> <span class="text-teal">e-Treasury</span></span>
      <small class="brand-subtitle d-none d-sm-block">Treasury Application Gallery</small>
    </span>
  </a>
  <div class="ms-auto d-flex align-items-center gap-2 gap-md-3">
    <div class="topbar-business-date d-none d-md-block">
      <small>Business Date</small>
      <strong>18 July 2026</strong>
    </div>
    <button class="btn btn-light p-2" type="button" aria-label="Notifications"><i data-lucide="bell" width="18"></i></button>
    <div class="vr"></div>
    <div class="topbar-user-details d-none d-sm-block text-end">
      <div class="topbar-user-identity">
        <strong><c:out value="${sessionScope.treasuryUser.userCode}" /></strong>
        <strong class="topbar-ein">(<c:out value="${sessionScope.treasuryUser.einNumber}" />)</strong>
        <span class="topbar-identity-separator" aria-hidden="true">|</span>
        <strong>SOL: <c:out value="${sessionScope.treasuryUser.solId}" /></strong>
      </div>
      <div class="topbar-user-meta text-secondary" title="Assigned menu roles">
        <c:forEach var="menu" items="${sessionScope.userMenus}" varStatus="status">
          <c:if test="${not status.first}">
            <span class="topbar-meta-separator" aria-hidden="true">|</span>
          </c:if>
          <strong><c:out value="${menu.menuName}" />:<c:out value="${menu.roleId}" /></strong>
        </c:forEach>
      </div>
    </div>
    <form class="topbar-logout-form" action="${pageContext.request.contextPath}/logout" method="post">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
      <button class="topbar-logout-button"
              type="submit"
              aria-label="Sign out"
              title="Sign out">
        <i data-lucide="log-out" width="18"></i>
      </button>
    </form>
  </div>
</header>

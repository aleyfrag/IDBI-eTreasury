<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:forEach var="assignedMenu" items="${sessionScope.userMenus}">
  <c:if test="${assignedMenu.menuId == param.menuId}">
    <c:set var="selectedMenu" value="${assignedMenu}" />
  </c:if>
</c:forEach>
<c:if test="${empty selectedMenu}">
  <c:redirect url="/home?menuUnavailable" />
</c:if>
<c:if test="${not empty selectedMenu.subMenus}">
  <c:set var="selectedSubMenu" value="${selectedMenu.subMenus[0]}" />
</c:if>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" sizes="any">
  <title><c:out value="${selectedMenu.menuName}" /> | IDBI e-Treasury</title>
  <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/treasury-confluence.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/sidebar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/footer.css">
</head>
<body data-page="menu-placeholder"
      data-active-menu-id="${selectedMenu.menuId}"
      data-active-submenu-id="${selectedSubMenu.subMenuId}">
<div class="app-shell">
  <%@ include file="WEB-INF/jsp/partials/header.jsp" %>
  <%@ include file="WEB-INF/jsp/partials/sidebar.jsp" %>
  <main class="main-content">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb small mb-2">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
        <li class="breadcrumb-item active"><c:out value="${selectedMenu.menuName}" /></li>
      </ol>
    </nav>
    <section class="panel">
      <div class="panel-header">
        <h1 class="page-title mb-1"><c:out value="${selectedMenu.menuName}" /></h1>
        <p class="text-secondary small mb-0">
          Selected role ID: <c:out value="${selectedMenu.roleId}" />
        </p>
      </div>
      <div class="p-4">
        <span class="badge badge-soft-success mb-3">Dummy JSP page</span>
        <c:choose>
          <c:when test="${not empty selectedSubMenu}">
            <p class="mb-0">Default submenu: <strong><c:out value="${selectedSubMenu.subMenuName}" /></strong></p>
          </c:when>
          <c:otherwise>
            <p class="mb-0">No submenu is assigned to this main menu.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </section>
    <%@ include file="WEB-INF/jsp/partials/footer.jsp" %>
  </main>
</div>
<script src="${pageContext.request.contextPath}/assets/vendor/lucide/lucide.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

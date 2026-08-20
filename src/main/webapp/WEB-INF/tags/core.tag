<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ attribute name="pageTitle" required="true" rtexprvalue="true" %>
<%@ attribute name="pageName" required="false" rtexprvalue="true" %>
<%@ attribute name="activeMenuId" required="false" rtexprvalue="true" %>
<%@ attribute name="activeSubMenuId" required="false" rtexprvalue="true" %>
<%@ attribute name="headContent" required="false" fragment="true" %>
<%@ attribute name="scriptContent" required="false" fragment="true" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" sizes="any">
  <title>${pageTitle}</title>
  <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/treasury-confluence.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/sidebar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/footer.css">
  <jsp:invoke fragment="headContent" />
</head>
<body data-page="${pageName}"
      data-active-menu-id="${activeMenuId}"
      data-active-submenu-id="${activeSubMenuId}">
  <div class="app-shell">
    <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />
    <jsp:include page="/WEB-INF/jsp/partials/sidebar.jsp" />

    <main class="main-content">
      <jsp:doBody />
      <jsp:include page="/WEB-INF/jsp/partials/footer.jsp" />
    </main>
  </div>

  <script src="${pageContext.request.contextPath}/assets/vendor/lucide/lucide.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
  <jsp:invoke fragment="scriptContent" />
</body>
</html>

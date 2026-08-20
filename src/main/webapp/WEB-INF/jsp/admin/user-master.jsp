<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<layout:core pageTitle="User Master | IDBI e-Treasury"
             pageName="admin-user-master"
             activeMenuId="${activeMenuId}"
             activeSubMenuId="${activeSubMenuId}">
  <jsp:attribute name="headContent">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/user-master.css">
  </jsp:attribute>

  <jsp:attribute name="scriptContent">
    <script src="${pageContext.request.contextPath}/assets/js/admin/user-master.js"></script>
  </jsp:attribute>

  <jsp:body>
    <%-- Page heading for the selected database submenu. --%>
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb small mb-3">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
        <li class="breadcrumb-item">Admin</li>
        <li class="breadcrumb-item active">User Master</li>
      </ol>
    </nav>

    <section class="panel admin-workspace" data-admin-tabs>
      <div class="admin-workspace-heading">
        <div>
          <div class="page-kicker">ADMINISTRATION</div>
          <h1 class="page-title">User Master</h1>
          <p class="text-secondary small mb-0">Select a master to view or maintain its records.</p>
        </div>
        <i data-lucide="settings" width="26" aria-hidden="true"></i>
      </div>

      <%--
        Chrome-style tab strip. The tab list comes from AdminUserMasterTabs.java.
        Adding a new Java tab entry automatically adds its button here.
      --%>
      <div class="admin-tab-strip" role="tablist" aria-label="User Master options">
        <c:forEach var="tab" items="${adminTabs}" varStatus="status">
          <button class="admin-tab${status.first ? ' active' : ''}"
                  id="admin-tab-${tab.id}"
                  type="button"
                  role="tab"
                  data-admin-tab="${tab.id}"
                  aria-controls="admin-panel-${tab.id}"
                  aria-selected="${status.first}">
            <i data-lucide="${tab.iconName}" width="15" aria-hidden="true"></i>
            <c:out value="${tab.label}" />
          </button>
        </c:forEach>
      </div>

      <div class="admin-tab-content">
        <%-- First feature implemented now: User Master. --%>
        <section id="admin-panel-user-master"
                 class="admin-tab-panel"
                 role="tabpanel"
                 aria-labelledby="admin-tab-user-master"
                 data-admin-panel="user-master">
          <c:set var="activeCount" value="0" />
          <c:set var="adminCount" value="0" />
          <c:set var="inactiveCount" value="0" />
          <c:forEach var="summaryUser" items="${userRecords}">
            <c:if test="${summaryUser.status eq 'A'}"><c:set var="activeCount" value="${activeCount + 1}" /></c:if>
            <c:if test="${summaryUser.status eq 'D'}"><c:set var="inactiveCount" value="${inactiveCount + 1}" /></c:if>
            <c:if test="${summaryUser.rights eq 'ADM'}"><c:set var="adminCount" value="${adminCount + 1}" /></c:if>
          </c:forEach>

          <div class="row g-3 mb-3 user-summary-grid">
            <div class="col-sm-6 col-xl-3"><div class="user-summary-card"><div><span class="metric-label">TOTAL USERS</span><strong>${userRecords.size()}</strong><small>Registered users</small></div><span class="metric-icon"><i data-lucide="users" width="19"></i></span></div></div>
            <div class="col-sm-6 col-xl-3"><div class="user-summary-card"><div><span class="metric-label">ACTIVE USERS</span><strong>${activeCount}</strong><small>Currently enabled</small></div><span class="metric-icon"><i data-lucide="user-check" width="19"></i></span></div></div>
            <div class="col-sm-6 col-xl-3"><div class="user-summary-card"><div><span class="metric-label">ADMIN USERS</span><strong>${adminCount}</strong><small>Administration rights</small></div><span class="metric-icon"><i data-lucide="shield-check" width="19"></i></span></div></div>
            <div class="col-sm-6 col-xl-3"><div class="user-summary-card"><div><span class="metric-label">DE-ACTIVE</span><strong>${inactiveCount}</strong><small>Disabled users</small></div><span class="metric-icon"><i data-lucide="user-x" width="19"></i></span></div></div>
          </div>

          <section class="user-section-panel mb-3">
            <div class="user-section-header"><div><h2>Maintain user</h2><p>Enter an EIN to load employee details or maintain an existing user.</p></div><i data-lucide="user-cog" width="21"></i></div>
            <form class="user-master-form" data-user-master-form action="${pageContext.request.contextPath}/admin/user-master/action" method="post">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
              <div class="user-master-message d-none" data-user-master-message role="status"></div>
              <div class="row g-3 align-items-end">
                <div class="col-md-6 col-xl"><label class="form-label" for="einNumber">EIN Number</label><input class="form-control" id="einNumber" name="einNumber" required data-user-lookup-input><div class="form-text">Details load when you leave this field.</div></div>
                <div class="col-md-6 col-xl"><label class="form-label" for="userCode">User Code</label><input class="form-control" id="userCode" name="userCode" required></div>
                <div class="col-md-6 col-xl-3"><label class="form-label" for="solId">SOL ID</label><select class="form-select" id="solId" name="solId" required><option value="">Select SOL</option><c:forEach var="sol" items="${solOptions}"><option value="${sol.solId}"><c:out value="${sol.solId} - ${sol.solDescription}"/></option></c:forEach></select></div>
                <div class="col-md-4 col-xl"><label class="form-label" for="status">Status</label><select class="form-select" id="status" name="status"><option value="A">Active</option><option value="D">De-active</option></select></div>
                <div class="col-md-2 col-xl-auto"><div class="form-check admin-check"><input class="form-check-input" id="admin" name="admin" type="checkbox" value="true"><label class="form-check-label" for="admin">Is Admin</label></div></div>
              </div>
              <div class="user-master-actions"><button class="btn btn-primary btn-sm" type="submit" value="SAVE">Save</button><button class="btn btn-outline-primary btn-sm" type="submit" value="UPDATE">Update</button><button class="btn btn-outline-danger btn-sm" type="submit" value="DELETE">Delete</button><button class="btn btn-outline-warning btn-sm" type="submit" value="REJECT">Reject</button><button class="btn btn-outline-secondary btn-sm" type="reset">Reset</button></div>
            </form>
          </section>

          <section class="user-section-panel">
            <div class="user-section-header"><div><h2>User directory</h2><p>Select a row to load that user into the maintenance form.</p></div><span class="record-count">${userRecords.size()} records</span></div>
            <div class="table-responsive"><table class="table table-hover align-middle user-master-table"><thead><tr><th>User Code</th><th>EIN No.</th><th>SOL ID</th><th>Role</th><th>Status</th><th>Rights</th><th>Bulk Upload</th><th>Created By</th><th>Creation Time</th><th>Modified By</th><th>Modified On</th><th>Appr/Rjct On</th><th>Appr/Rjct By</th></tr></thead><tbody>
              <c:forEach var="user" items="${userRecords}"><tr data-user-row data-search="${user.einNumber}"><td class="fw-semibold"><c:out value="${user.userCode}"/></td><td><c:out value="${user.einNumber}"/></td><td><c:out value="${user.solId}"/></td><td><c:out value="${user.role}"/></td><td><span class="status-badge ${user.status eq 'A' ? 'is-active' : 'is-inactive'}">${user.status eq 'A' ? 'Active' : 'De-active'}</span></td><td><c:out value="${user.rights}"/></td><td><c:out value="${user.bulkUpload}"/></td><td><c:out value="${user.createdBy}"/></td><td><c:out value="${user.creationTime}"/></td><td><c:out value="${user.modifiedBy}"/></td><td><c:out value="${user.modifiedOn}"/></td><td><c:out value="${user.approvedRejectedOn}"/></td><td><c:out value="${user.approvedRejectedBy}"/></td></tr></c:forEach>
              <c:if test="${empty userRecords}"><tr><td colspan="13" class="text-center text-secondary py-5">No users found.</td></tr></c:if>
            </tbody></table></div>
          </section>
        </section>

        <%-- Remaining hardcoded tabs are ready for their features one by one. --%>
        <c:forEach var="tab" items="${adminTabs}">
          <c:if test="${tab.id ne 'user-master'}">
            <section id="admin-panel-${tab.id}"
                     class="admin-tab-panel d-none"
                     role="tabpanel"
                     aria-labelledby="admin-tab-${tab.id}"
                     data-admin-panel="${tab.id}">
              <div class="admin-empty-state">
                <i data-lucide="${tab.iconName}" width="30" aria-hidden="true"></i>
                <h2><c:out value="${tab.label}" /></h2>
                <p>This tab is ready for its next implementation.</p>
              </div>
            </section>
          </c:if>
        </c:forEach>
      </div>
    </section>
  </jsp:body>
</layout:core>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" sizes="any">
  <title>IDBIForex Deal List | IDBI e-Treasury</title>
  <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/treasury-confluence.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/sidebar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/footer.css">
  <style>
    .deal-page-heading {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      justify-content: space-between;
      gap: 1rem;
      margin-bottom: 1.25rem;
    }

    .deal-page-heading .page-title {
      color: #003968;
    }

    .deal-date-chip {
      display: inline-flex;
      align-items: center;
      gap: .55rem;
      padding: .65rem .8rem;
      color: #003968;
      background: #fff;
      border: 1px solid #cfdae3;
      border-radius: 4px;
      font-size: .78rem;
      font-weight: 700;
      box-shadow: 0 1px 2px rgba(0,57,104,.04);
    }

    .deal-metric-card {
      position: relative;
      overflow: hidden;
    }

    .deal-metric-card::after {
      content: "";
      position: absolute;
      right: 0;
      bottom: 0;
      width: 58px;
      height: 3px;
      background: #bed600;
    }

    .deal-metric-card .metric-value {
      color: #003968;
    }

    .deal-metric-card .metric-note {
      color: #587187;
      font-size: .74rem;
    }

    .deal-list-toolbar {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      justify-content: space-between;
      gap: 1rem;
    }

    .deal-list-filter {
      display: flex;
      flex-wrap: wrap;
      align-items: end;
      gap: .65rem;
    }

    .deal-list-filter .form-control {
      min-width: 190px;
    }

    .deal-list-filter .form-label {
      color: #587187;
      font-size: .7rem;
      font-weight: 700;
      letter-spacing: .035em;
      text-transform: uppercase;
    }

    .deal-table-wrap {
      overflow: auto;
      max-height: 56vh;
      border-top: 1px solid #cfdae3;
      scrollbar-color: #8ca0ae #edf3f6;
      scrollbar-width: thin;
    }

    .deal-table {
      min-width: 6800px;
      margin: 0;
      color: #304f63;
      font-size: .75rem;
      white-space: nowrap;
    }

    .deal-table thead {
      position: sticky;
      top: 0;
      z-index: 3;
    }

    .deal-table th,
    .deal-table td {
      padding: .72rem .8rem;
      vertical-align: middle;
      border-color: #dde5eb;
    }

    .deal-table th {
      color: #587187;
      background: #f3f7fa;
      font-size: .64rem;
      letter-spacing: .04em;
      text-transform: uppercase;
    }

    .deal-table tbody tr:hover td {
      background: #f7fadf;
    }

    .deal-table th:first-child,
    .deal-table td:first-child {
      position: sticky;
      left: 0;
      z-index: 2;
      background: #fff;
      box-shadow: 1px 0 0 #dde5eb;
    }

    .deal-table th:first-child {
      z-index: 5;
      color: #003968;
      background: #edf4f7;
    }

    .deal-table tbody tr:hover td:first-child {
      background: #f7fadf;
    }

    .deal-number {
      color: #007d89;
      font-weight: 700;
      text-decoration: none;
    }

    .deal-number:hover,
    .deal-number:focus-visible {
      color: #003968;
      text-decoration: underline;
    }

    .deal-status {
      display: inline-flex;
      align-items: center;
      min-width: 74px;
      justify-content: center;
      padding: .32rem .58rem;
      border-radius: 999px;
      font-size: .66rem;
      font-weight: 800;
      letter-spacing: .02em;
      text-transform: capitalize;
    }

    .deal-status-success { color: #116343; background: #e3f3eb; }
    .deal-status-warning { color: #865d11; background: #fbf1d9; }
    .deal-status-danger { color: #9e252d; background: #fbe7e9; }
    .deal-status-neutral { color: #34556a; background: #eaf0f4; }

    .deal-search-bar {
      background: #fbfcfd;
    }

    .deal-search-bar .input-group-text {
      color: #587187;
      background: #fff;
      border-right: 0;
    }

    .deal-search-bar .form-control {
      border-left: 0;
    }

    .deal-search-bar .form-control:focus {
      border-left: 0;
      box-shadow: none;
    }

    .deal-empty-state {
      padding: 3.5rem 1rem;
      color: #587187;
      text-align: center;
    }

    .deal-empty-state i {
      display: block;
      margin: 0 auto .7rem;
      color: #8ca0ae;
    }

    .deal-list-footer {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      justify-content: space-between;
      gap: .75rem;
      padding: .7rem 1rem;
      color: #587187;
      background: #f8fafb;
      border-top: 1px solid #cfdae3;
      font-size: .74rem;
    }

    .alert {
      border-width: 1px;
      border-radius: 4px;
      font-size: .84rem;
      box-shadow: 0 1px 2px rgba(0,57,104,.03);
    }

    @media (max-width: 767.98px) {
      .deal-list-toolbar,
      .deal-list-filter {
        align-items: stretch;
      }

      .deal-list-filter,
      .deal-list-filter .form-control,
      .deal-list-filter .btn {
        width: 100%;
      }

      .deal-date-chip {
        width: 100%;
      }
    }
  </style>
</head>
<body data-page="idbiforex-deals"
      data-active-menu-id="${activeMenuId}"
      data-active-submenu-id="${activeSubMenuId}">
<div class="app-shell">
  <%@ include file="../partials/header.jsp" %>
  <%@ include file="../partials/sidebar.jsp" %>

  <main class="main-content">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb small mb-3">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/idbiforex/home">IDBIForex</a></li>
        <li class="breadcrumb-item active">Deal List</li>
      </ol>
    </nav>

    <div class="deal-page-heading">
      <div>
        <div class="page-kicker mb-1">IDBIFOREX · DEAL OPERATIONS</div>
        <h1 class="page-title mb-1">IDBIForex Deals</h1>
        <p class="text-secondary small mb-0">Deals available for your assigned SOL and transaction date.</p>
      </div>
      <div class="deal-date-chip" aria-label="Selected transaction date">
        <i data-lucide="calendar-days" width="17"></i>
        <span>Transaction date: <c:out value="${transactionDate}" /></span>
      </div>
    </div>

    <c:if test="${not empty dealMessage}">
      <div class="alert alert-${dealMessageType} d-flex align-items-start gap-2" role="alert" aria-live="polite">
        <i data-lucide="${dealMessageType eq 'success' ? 'circle-check' : dealMessageType eq 'danger' ? 'circle-alert' : 'info'}" width="18"></i>
        <div><c:out value="${dealMessage}" /></div>
      </div>
    </c:if>

    <div class="row g-3 mb-3" aria-label="Deal summary">
      <div class="col-sm-6 col-xl-4">
        <div class="panel metric-card deal-metric-card h-100">
          <div class="d-flex justify-content-between">
            <span class="metric-label">DEAL RECORDS</span>
            <span class="metric-icon"><i data-lucide="list-checks" width="19"></i></span>
          </div>
          <div class="metric-value"><c:out value="${dealCount}" /></div>
          <span class="metric-note">Available for the selected date</span>
        </div>
      </div>
      <div class="col-sm-6 col-xl-4">
        <div class="panel metric-card deal-metric-card h-100">
          <div class="d-flex justify-content-between">
            <span class="metric-label">TOTAL AMOUNT</span>
            <span class="metric-icon"><i data-lucide="indian-rupee" width="19"></i></span>
          </div>
          <div class="metric-value"><fmt:formatNumber value="${totalAmount}" minFractionDigits="2" maxFractionDigits="2" /></div>
          <span class="metric-note">Across all displayed currencies</span>
        </div>
      </div>
      <div class="col-sm-12 col-xl-4">
        <div class="panel metric-card deal-metric-card h-100">
          <div class="d-flex justify-content-between">
            <span class="metric-label">DATA STATUS</span>
            <span class="metric-icon"><i data-lucide="database" width="19"></i></span>
          </div>
          <div class="metric-value">${empty deals ? 'No data' : 'Loaded'}</div>
          <span class="metric-note"><span class="status-dot"></span>IFXPRO deal records</span>
        </div>
      </div>
    </div>

    <section class="panel overflow-hidden" aria-labelledby="deal-list-title">
      <div class="panel-header deal-list-toolbar">
        <div>
          <h2 class="panel-title">IDBIForex deal register</h2>
          <small class="text-secondary">Search, review, and export deals returned by IFXPRO.</small>
        </div>
        <form class="deal-list-filter" action="${pageContext.request.contextPath}/idbiforex/deals" method="get">
          <div>
            <label class="form-label" for="transactionDate">Transaction date</label>
            <input class="form-control form-control-sm" id="transactionDate" name="transactionDate" type="date"
                   value="${transactionDate}" required>
          </div>
          <button class="btn btn-sm btn-idbi" type="submit">
            <i data-lucide="search" width="16" class="me-1"></i>Load deals
          </button>
        </form>
      </div>

      <div class="deal-search-bar p-3 border-bottom d-flex flex-wrap justify-content-between align-items-center gap-2">
        <div class="input-group input-group-sm" style="max-width:320px">
          <span class="input-group-text"><i data-lucide="search" width="15"></i></span>
          <input class="form-control" id="dealTableSearch" type="search"
                 placeholder="Search all deal columns" aria-label="Search all deal columns">
        </div>
        <button class="btn btn-sm btn-outline-success ${empty deals ? 'disabled' : ''}"
                id="exportDeals" type="button" aria-disabled="${empty deals}">
          <i data-lucide="file-spreadsheet" width="15" class="me-1"></i>Download CSV
        </button>
      </div>

      <c:choose>
        <c:when test="${empty deals}">
          <div class="deal-empty-state">
            <i data-lucide="inbox" width="34"></i>
            <strong class="d-block mb-1">No deal records to display</strong>
            <span>Choose a transaction date and select Load deals.</span>
          </div>
        </c:when>
        <c:otherwise>
          <div class="deal-table-wrap">
            <table class="table table-hover deal-table" id="dealTable">
              <caption class="visually-hidden" id="deal-list-title">IDBIForex deals for the selected date</caption>
              <thead>
              <tr>
                <th>Deal no.</th>
                <th>Application date</th>
                <th>Deal type</th>
                <th>Customer ID</th>
                <th>Customer name</th>
                <th>Card rates</th>
                <th>Branch code</th>
                <th>Transaction type</th>
                <th>Currency 1</th>
                <th>Currency 2</th>
                <th class="text-end">Amount</th>
                <th>Value date 1</th>
                <th>Value date 2</th>
                <th class="text-end">Rate</th>
                <th>Type</th>
                <th>Nostro account</th>
                <th>Original rate</th>
                <th>Preferred customer</th>
                <th>Actual high floor</th>
                <th>Actual low floor</th>
                <th>Customer floor</th>
                <th>Profit/loss</th>
                <th>Remarks</th>
                <th>Spot rate</th>
                <th>Forward premia</th>
                <th>Margin</th>
                <th>Net rate</th>
                <th>Booked by</th>
                <th>Booking date</th>
                <th>Confirmed by</th>
                <th>Confirmation date</th>
                <th>Status</th>
                <th>Trade date</th>
                <th>Spot date</th>
                <th>Maturity date</th>
                <th>SOL ID</th>
                <th>Option date</th>
                <th>Treasury dealer</th>
                <th>Treasury transfer date</th>
                <th>Transmitted Y/N</th>
                <th>Margin ID</th>
                <th>Deal request ID</th>
                <th>Swap charges</th>
                <th>Forward contract no.</th>
                <th>Dummy</th>
                <th>User SOL</th>
                <th>Vert code</th>
                <th>UDR</th>
                <th>Fund</th>
                <th>UC type</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="deal" items="${deals}">
                <tr>
                  <td><span class="deal-number"><c:out value="${empty deal.dealNumber ? '-' : deal.dealNumber}" /></span></td>
                  <td><c:out value="${empty deal.applicationSystemDate ? '-' : deal.applicationSystemDate}" /></td>
                  <td><c:out value="${empty deal.dealType ? '-' : deal.dealType}" /></td>
                  <td><c:out value="${empty deal.customerId ? '-' : deal.customerId}" /></td>
                  <td><c:out value="${empty deal.customerName ? '-' : deal.customerName}" /></td>
                  <td><c:out value="${empty deal.cardRates ? '-' : deal.cardRates}" /></td>
                  <td><c:out value="${empty deal.branchCode ? '-' : deal.branchCode}" /></td>
                  <td><c:out value="${empty deal.transactionType ? '-' : deal.transactionType}" /></td>
                  <td><span class="currency-chip"><c:out value="${empty deal.currencyOne ? '-' : deal.currencyOne}" /></span></td>
                  <td><span class="currency-chip"><c:out value="${empty deal.currencyTwo ? '-' : deal.currencyTwo}" /></span></td>
                  <td class="text-end"><fmt:formatNumber value="${deal.amount}" minFractionDigits="2" maxFractionDigits="2" /></td>
                  <td><c:out value="${empty deal.valueDateOne ? '-' : deal.valueDateOne}" /></td>
                  <td><c:out value="${empty deal.valueDateTwo ? '-' : deal.valueDateTwo}" /></td>
                  <td class="text-end"><c:out value="${empty deal.rate ? '-' : deal.rate}" /></td>
                  <td><c:out value="${empty deal.type ? '-' : deal.type}" /></td>
                  <td><c:out value="${empty deal.nostroAccount ? '-' : deal.nostroAccount}" /></td>
                  <td><c:out value="${empty deal.originalRate ? '-' : deal.originalRate}" /></td>
                  <td><c:out value="${empty deal.preferredCustomer ? '-' : deal.preferredCustomer}" /></td>
                  <td><c:out value="${empty deal.actualHighFloor ? '-' : deal.actualHighFloor}" /></td>
                  <td><c:out value="${empty deal.actualLowFloor ? '-' : deal.actualLowFloor}" /></td>
                  <td><c:out value="${empty deal.customerFloor ? '-' : deal.customerFloor}" /></td>
                  <td><c:out value="${empty deal.profitLoss ? '-' : deal.profitLoss}" /></td>
                  <td><c:out value="${empty deal.remarks ? '-' : deal.remarks}" /></td>
                  <td><c:out value="${empty deal.spotRate ? '-' : deal.spotRate}" /></td>
                  <td><c:out value="${empty deal.forwardPremia ? '-' : deal.forwardPremia}" /></td>
                  <td><c:out value="${empty deal.margin ? '-' : deal.margin}" /></td>
                  <td><c:out value="${empty deal.netRate ? '-' : deal.netRate}" /></td>
                  <td><c:out value="${empty deal.bookedBy ? '-' : deal.bookedBy}" /></td>
                  <td><c:out value="${empty deal.bookingDate ? '-' : deal.bookingDate}" /></td>
                  <td><c:out value="${empty deal.confirmedBy ? '-' : deal.confirmedBy}" /></td>
                  <td><c:out value="${empty deal.confirmationDate ? '-' : deal.confirmationDate}" /></td>
                  <td>
                    <c:choose>
                      <c:when test="${deal.status eq 'VERIFIED' or deal.status eq 'ACCEPTED'}">
                        <span class="deal-status deal-status-success"><c:out value="${deal.status}" /></span>
                      </c:when>
                      <c:when test="${deal.status eq 'FREEZED' or deal.status eq 'ENTERED' or deal.status eq 'PENDING'}">
                        <span class="deal-status deal-status-warning"><c:out value="${deal.status}" /></span>
                      </c:when>
                      <c:when test="${deal.status eq 'REJECTED' or deal.status eq 'DELETED'}">
                        <span class="deal-status deal-status-danger"><c:out value="${deal.status}" /></span>
                      </c:when>
                      <c:otherwise>
                        <span class="deal-status deal-status-neutral"><c:out value="${empty deal.status ? '-' : deal.status}" /></span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td><c:out value="${empty deal.tradeDate ? '-' : deal.tradeDate}" /></td>
                  <td><c:out value="${empty deal.spotDate ? '-' : deal.spotDate}" /></td>
                  <td><c:out value="${empty deal.maturityDate ? '-' : deal.maturityDate}" /></td>
                  <td><c:out value="${empty deal.solId ? '-' : deal.solId}" /></td>
                  <td><c:out value="${empty deal.optionDate ? '-' : deal.optionDate}" /></td>
                  <td><c:out value="${empty deal.treasuryDealer ? '-' : deal.treasuryDealer}" /></td>
                  <td><c:out value="${empty deal.treasuryTransferDate ? '-' : deal.treasuryTransferDate}" /></td>
                  <td><c:out value="${empty deal.transmitted ? '-' : deal.transmitted}" /></td>
                  <td><c:out value="${empty deal.marginId ? '-' : deal.marginId}" /></td>
                  <td><c:out value="${empty deal.dealRequestId ? '-' : deal.dealRequestId}" /></td>
                  <td><c:out value="${empty deal.swapCharges ? '-' : deal.swapCharges}" /></td>
                  <td><c:out value="${empty deal.forwardContractNumber ? '-' : deal.forwardContractNumber}" /></td>
                  <td><c:out value="${empty deal.dummy ? '-' : deal.dummy}" /></td>
                  <td><c:out value="${empty deal.userSol ? '-' : deal.userSol}" /></td>
                  <td><c:out value="${empty deal.vertCode ? '-' : deal.vertCode}" /></td>
                  <td><c:out value="${empty deal.udr ? '-' : deal.udr}" /></td>
                  <td><c:out value="${empty deal.fund ? '-' : deal.fund}" /></td>
                  <td><c:out value="${empty deal.ucType ? '-' : deal.ucType}" /></td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
          </div>
          <div class="deal-list-footer">
            <span id="visibleDealCount">Showing <c:out value="${dealCount}" /> of <c:out value="${dealCount}" /> records</span>
            <span>Use the horizontal scrollbar to view all deal fields.</span>
          </div>
        </c:otherwise>
      </c:choose>
    </section>

    <%@ include file="../partials/footer.jsp" %>
  </main>
</div>
<script src="${pageContext.request.contextPath}/assets/vendor/lucide/lucide.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script>
  (() => {
    const table = document.getElementById("dealTable");
    const search = document.getElementById("dealTableSearch");
    const visibleCount = document.getElementById("visibleDealCount");
    const exportButton = document.getElementById("exportDeals");
    if (!table) return;

    const rows = [...table.querySelectorAll("tbody tr")];
    const updateVisibleCount = () => {
      const count = rows.filter((row) => !row.hidden).length;
      if (visibleCount) visibleCount.textContent = "Showing " + count + " of " + rows.length + " records";
    };

    search?.addEventListener("input", (event) => {
      const query = event.target.value.trim().toLocaleLowerCase();
      rows.forEach((row) => {
        row.hidden = query.length > 0 && !row.textContent.toLocaleLowerCase().includes(query);
      });
      updateVisibleCount();
    });

    exportButton?.addEventListener("click", () => {
      const visibleRows = rows.filter((row) => !row.hidden);
      const csvValue = (cell) => {
        let value = cell.textContent.trim();
        if (/^[=+\-@]/.test(value)) value = "'" + value;
        return '"' + value.replaceAll('"', '""') + '"';
      };
      const csvRows = [table.querySelectorAll("thead th"), ...visibleRows.map((row) => row.querySelectorAll("td"))]
        .map((cells) => [...cells].map(csvValue).join(","));
      const blob = new Blob([csvRows.join("\r\n")], {type: "text/csv;charset=utf-8"});
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = "idbiforex-deals-" + document.getElementById("transactionDate").value + ".csv";
      link.click();
      URL.revokeObjectURL(link.href);
    });
  })();
</script>
</body>
</html>

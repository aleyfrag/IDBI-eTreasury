<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" href="/favicon.ico" sizes="any">
  <title>Forex Deal List | IDBI e-Treasury</title>
  <link href="/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="/assets/css/treasury-confluence.css">
  <link rel="stylesheet" href="/assets/css/components/header.css">
  <link rel="stylesheet" href="/assets/css/components/sidebar.css">
  <link rel="stylesheet" href="/assets/css/components/footer.css">
  <style>
    .filter-bar { padding: 1rem 1.15rem; background: #f8fafa; border-bottom: 1px solid #d9e2e1; }
    .filter-bar .form-control, .filter-bar .form-select { min-height: 38px; font-size: .8rem; }
    .deal-table { min-width: 1180px; }
    .deal-table thead th { white-space: nowrap; vertical-align: middle; }
    .deal-table .column-filter { min-width: 105px; min-height: 30px; padding: .25rem .45rem; border: 1px solid #d5dfde; border-radius: 4px; font-size: .68rem; font-weight: 500; text-transform: none; }
    .deal-table tbody td { padding-block: .75rem; white-space: nowrap; }
    .deal-link { color: #177052; font-weight: 800; text-decoration: none; }
    .deal-link:hover { text-decoration: underline; }
    .sort-icon { color: #a0adab; vertical-align: middle; }
    .summary-chip { padding: .55rem .75rem; background: #eef5f4; border: 1px solid #d9e6e4; border-radius: 5px; }
  </style>
</head>
<body data-page="deal-list"><div class="app-shell">
  <%@ include file="partials/header.jsp" %>
  <%@ include file="partials/sidebar.jsp" %>
  <main class="main-content">
    <nav aria-label="breadcrumb"><ol class="breadcrumb small mb-2"><li class="breadcrumb-item"><a href="/home" class="text-decoration-none">Home</a></li><li class="breadcrumb-item">IDBI Forex</li><li class="breadcrumb-item active">Deal list</li></ol></nav>
    <div class="d-flex flex-wrap justify-content-between align-items-end gap-3 mb-4"><div><h1 class="page-title mb-1">IDBI Forex deals</h1><p class="text-secondary small mb-0">Search, review and export customer forex transactions.</p></div><a class="btn btn-idbi" href="/deals/new"><i data-lucide="plus" width="17" class="me-1"></i>New deal</a></div>
    <section class="panel">
      <div class="panel-header d-flex flex-wrap justify-content-between align-items-center gap-2"><div><h2 class="panel-title">Deal register</h2><small class="text-secondary">Transactions for the selected business date</small></div><div class="d-flex gap-2"><button class="btn btn-sm btn-light border" type="button"><i data-lucide="sliders-horizontal" width="15" class="me-1"></i>Columns</button><button class="btn btn-sm btn-outline-success" type="button"><i data-lucide="file-spreadsheet" width="15" class="me-1"></i>Export Excel</button></div></div>
      <div class="filter-bar"><div class="row g-2 align-items-end"><div class="col-sm-6 col-lg-3"><label class="form-label" for="transactionDate">Transaction date</label><input class="form-control" id="transactionDate" type="date" value="2026-07-18"></div><div class="col-sm-6 col-lg-2"><label class="form-label" for="dealStatus">Status</label><select class="form-select" id="dealStatus"><option>All statuses</option><option>Completed</option><option>Pending</option><option>On hold</option></select></div><div class="col-lg-4 ms-lg-auto"><label class="form-label" for="globalSearch">Search all deals</label><div class="input-group"><span class="input-group-text"><i data-lucide="search" width="16"></i></span><input class="form-control" id="globalSearch" placeholder="Deal number, customer, currency..."></div></div><div class="col-auto"><button class="btn btn-teal px-3" type="button">Apply</button></div></div></div>
      <div class="table-responsive"><table class="table deal-table align-middle mb-0"><thead><tr><th class="ps-3">Blotter no. <i data-lucide="chevrons-up-down" width="12" class="sort-icon"></i><div><input class="column-filter" placeholder="Filter blotter"></div></th><th>SOL <i data-lucide="chevrons-up-down" width="12" class="sort-icon"></i><div><input class="column-filter" placeholder="Filter SOL"></div></th><th>Deal type <i data-lucide="chevrons-up-down" width="12" class="sort-icon"></i><div><input class="column-filter" placeholder="Filter type"></div></th><th>Customer ID <i data-lucide="chevrons-up-down" width="12" class="sort-icon"></i><div><input class="column-filter" placeholder="Filter ID"></div></th><th>Customer name <i data-lucide="chevrons-up-down" width="12" class="sort-icon"></i><div><input class="column-filter" placeholder="Filter customer"></div></th><th>Type <i data-lucide="chevrons-up-down" width="12" class="sort-icon"></i><div><input class="column-filter" placeholder="Filter type"></div></th><th>Currency pair <i data-lucide="chevrons-up-down" width="12" class="sort-icon"></i><div><input class="column-filter" placeholder="Filter CCY"></div></th><th class="text-end">Amount <i data-lucide="chevrons-up-down" width="12" class="sort-icon"></i><div><input class="column-filter" placeholder="Filter amount"></div></th><th>Value date</th><th>Status</th><th></th></tr></thead><tbody>
        <tr><td class="ps-3"><a class="deal-link" href="#">FX1807260001</a></td><td>080</td><td>TT-INWARD</td><td>100000002</td><td>Apex Exports Ltd.</td><td>PURC</td><td><span class="currency-chip"><span class="currency-flag">US</span>USD/INR</span></td><td class="text-end fw-bold">100,000.00</td><td>18-Jul-2026</td><td><span class="badge badge-soft-success">Completed</span></td><td><button class="btn btn-sm btn-light" aria-label="Open deal"><i data-lucide="chevron-right" width="15"></i></button></td></tr>
        <tr><td class="ps-3"><a class="deal-link" href="#">FX1807260002</a></td><td>375</td><td>TT-OUTWARD</td><td>100000184</td><td>Nova Engineering</td><td>SALE</td><td><span class="currency-chip"><span class="currency-flag">AU</span>AUD/INR</span></td><td class="text-end fw-bold">64,500.00</td><td>20-Jul-2026</td><td><span class="badge badge-soft-warning">Pending</span></td><td><button class="btn btn-sm btn-light" aria-label="Open deal"><i data-lucide="chevron-right" width="15"></i></button></td></tr>
        <tr><td class="ps-3"><a class="deal-link" href="#">FX1807260007</a></td><td>183</td><td>TT-INWARD</td><td>100000249</td><td>Western Textiles</td><td>PURC</td><td><span class="currency-chip"><span class="currency-flag">EU</span>EUR/INR</span></td><td class="text-end fw-bold">180,000.00</td><td>18-Jul-2026</td><td><span class="badge badge-soft-success">Completed</span></td><td><button class="btn btn-sm btn-light" aria-label="Open deal"><i data-lucide="chevron-right" width="15"></i></button></td></tr>
        <tr><td class="ps-3"><a class="deal-link" href="#">FX1807260008</a></td><td>096</td><td>BILL-SALE</td><td>100000310</td><td>Orion Pharma</td><td>SALE</td><td><span class="currency-chip"><span class="currency-flag">GB</span>GBP/INR</span></td><td class="text-end fw-bold">96,500.00</td><td>21-Jul-2026</td><td><span class="badge badge-soft-danger">On hold</span></td><td><button class="btn btn-sm btn-light" aria-label="Open deal"><i data-lucide="chevron-right" width="15"></i></button></td></tr>
      </tbody></table></div>
      <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 p-3 border-top"><div class="d-flex align-items-center gap-2"><span class="summary-chip small"><strong>4</strong> deals</span><span class="summary-chip small"><strong>₹ 4.41 Cr</strong> total value</span></div><nav aria-label="Deal list pages"><ul class="pagination pagination-sm mb-0"><li class="page-item disabled"><a class="page-link" href="#">Previous</a></li><li class="page-item active"><a class="page-link" href="#">1</a></li><li class="page-item"><a class="page-link" href="#">2</a></li><li class="page-item"><a class="page-link" href="#">Next</a></li></ul></nav></div>
    </section>
    <%@ include file="partials/footer.jsp" %>
  </main>
</div><script src="/assets/vendor/lucide/lucide.min.js"></script><script src="/assets/vendor/bootstrap/bootstrap.bundle.min.js"></script><script src="/assets/js/app.js"></script></body></html>

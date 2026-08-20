<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><link rel="icon" href="/favicon.ico" sizes="any"><title>Forex Deal Entry | IDBI e-Treasury</title><link href="/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet"><link rel="stylesheet" href="/assets/css/treasury-confluence.css"><link rel="stylesheet" href="/assets/css/components/header.css"><link rel="stylesheet" href="/assets/css/components/sidebar.css"><link rel="stylesheet" href="/assets/css/components/footer.css"><style>
  .deal-workspace > .deal-form-column { flex: 1 1 720px; width: auto; }
  .deal-workspace > .deal-rate-column { flex: 0 0 280px; width: 280px; }
  .deal-rate-feed .nav-tabs { padding: .5rem .5rem 0; background: #f3f6f6; }
  .deal-rate-feed .nav-link { padding: .55rem .45rem; color: #66777c; font-size: .66rem; font-weight: 600; white-space: nowrap; }
  .deal-rate-feed .nav-link.active { color: #177052; }
  .deal-rate-feed .table { margin: 0; font-variant-numeric: tabular-nums; }
  .deal-rate-feed .table th { padding: .45rem .5rem; font-size: .6rem; }
  .deal-rate-feed .table td { padding: .34rem .5rem; border-color: #e3e9e8; font-size: .72rem; }
  .deal-rate-feed .pair { color: #177052; font-weight: 700; }
  .deal-rate-feed .rate { color: #176d4e; text-align: right; font-weight: 700; }
  .deal-rate-feed .rate-alert { color: #c62f36; }
  .deal-rate-feed .feed-meta { padding: .55rem .6rem; color: #66777c; background: #f5f7f7; font-size: .6rem; }
  .deal-actions {
    background: #f3f7d3 !important;
    border-top: 2px solid #003968 !important;
  }
  .deal-actions .btn-outline-secondary {
    color: #003968;
    background: #fff;
    border-color: #7890a2;
  }
  .deal-actions .btn-light {
    color: #003968;
    background: #e4ebef;
    border-color: #b7c5ce !important;
  }
  @media (min-width: 1200px) and (min-height: 720px) {
    body[data-page="deal-form"] { overflow: hidden; }
    body[data-page="deal-form"] .main-content {
      height: calc(100vh - 68px);
      overflow: hidden;
      padding: .65rem 1rem;
    }
    body[data-page="deal-form"] .breadcrumb { display: none; }
    body[data-page="deal-form"] .main-content > .d-flex.mb-4 { margin-bottom: .5rem !important; }
    body[data-page="deal-form"] .main-content > .d-flex.mb-4 p,
    body[data-page="deal-form"] .deal-form-column .panel-header small,
    body[data-page="deal-form"] .settlement-grid small { display: none; }
    body[data-page="deal-form"] .page-title { font-size: 1.12rem; }
    body[data-page="deal-form"] .deal-workspace { --bs-gutter-y: .5rem; }
    body[data-page="deal-form"] .panel-header { padding: .5rem .75rem; }
    body[data-page="deal-form"] .deal-form-column .panel > .p-3 { padding: .6rem .75rem !important; }
    body[data-page="deal-form"] .section-heading {
      margin-bottom: .4rem;
      padding-bottom: .25rem;
      font-size: .72rem;
    }
    body[data-page="deal-form"] .deal-form-column .row {
      --bs-gutter-x: .65rem;
      --bs-gutter-y: .35rem;
      margin-bottom: .5rem !important;
    }
    body[data-page="deal-form"] .customer-grid > * { flex: 0 0 20%; width: 20%; }
    body[data-page="deal-form"] .form-label { margin-bottom: .15rem; font-size: .68rem; }
    body[data-page="deal-form"] .form-control,
    body[data-page="deal-form"] .form-select,
    body[data-page="deal-form"] .input-group-text,
    body[data-page="deal-form"] .readonly-field { min-height: 32px; padding-top: .25rem; padding-bottom: .25rem; font-size: .72rem; }
    body[data-page="deal-form"] .transaction-grid > * { flex: 0 0 20%; width: 20%; }
    body[data-page="deal-form"] .settlement-grid > :nth-child(1) { flex: 0 0 38%; width: 38%; }
    body[data-page="deal-form"] .settlement-grid > :nth-child(2) { flex: 0 0 24%; width: 24%; }
    body[data-page="deal-form"] .settlement-grid > :nth-child(3) { flex: 0 0 38%; width: 38%; }
    body[data-page="deal-form"] #remarks { height: 32px; min-height: 32px; resize: none; }
    body[data-page="deal-form"] .deal-form-column form > .border-top { padding: .45rem .75rem !important; }
    body[data-page="deal-form"] .deal-rate-feed .table th { padding: .28rem .4rem; }
    body[data-page="deal-form"] .deal-rate-feed .table td { padding: .2rem .4rem; font-size: .66rem; }
    body[data-page="deal-form"] .deal-rate-feed .rate-feed-header { padding: .55rem .7rem; }
    body[data-page="deal-form"] .deal-rate-feed .nav-tabs { padding-top: .25rem; }
    body[data-page="deal-form"] .deal-rate-feed .nav-link { padding: .38rem .3rem; }
    body[data-page="deal-form"] .reference-footer { margin-top: .5rem; padding: .45rem .75rem; }
  }
  @media (max-width: 1199.98px) {
    .deal-workspace > .deal-rate-column { flex: 1 1 100%; width: 100%; }
    .deal-rate-feed { position: static; max-width: 460px; }
  }
</style></head><body data-page="deal-form"><div class="app-shell">
<%@ include file="partials/header.jsp" %>
<%@ include file="partials/sidebar.jsp" %>
<main class="main-content"><nav aria-label="breadcrumb"><ol class="breadcrumb small mb-2"><li class="breadcrumb-item"><a href="/home" class="text-decoration-none">Home</a></li><li class="breadcrumb-item">IDBI Forex</li><li class="breadcrumb-item active">Deal entry</li></ol></nav><div class="d-flex flex-wrap justify-content-between align-items-end gap-3 mb-4"><div><h1 class="page-title mb-1">New forex deal</h1><p class="text-secondary small mb-0">Create and submit a customer foreign exchange transaction.</p></div><div class="stepper"><div class="step active"><span class="step-number">1</span><span>Deal details</span></div><span class="step-line"></span><div class="step"><span class="step-number">2</span><span>Review</span></div><span class="step-line"></span><div class="step"><span class="step-number">3</span><span>Confirmation</span></div></div></div>
<div class="row g-3 deal-workspace"><div class="deal-form-column"><form class="panel" action="#"><div class="panel-header d-flex justify-content-between"><div><h2 class="panel-title">IDBI Forex Deal Entry</h2><small class="text-secondary">Fields marked with * are mandatory</small></div><span class="badge text-bg-light align-self-start">Draft</span></div><div class="p-3 p-lg-4">
<h3 class="section-heading">Customer information</h3><div class="row g-3 mb-4 customer-grid"><div class="col-md-5"><label class="form-label required" for="customerId">Customer ID</label><div class="input-group"><input class="form-control" id="customerId" placeholder="Enter customer ID"><button class="btn btn-outline-secondary" type="button" aria-label="Search customer"><i data-lucide="search" width="17"></i></button></div></div><div class="col-md-5"><label class="form-label" for="customerName">Customer name</label><div class="readonly-field" id="customerName">Populated after customer search</div></div><div class="col-md-2"><label class="form-label" for="status">Status</label><input class="form-control" id="status" value="Pending" readonly></div><div class="col-md-3"><label class="form-label" for="blotterNumber">Blotter number</label><input class="form-control" id="blotterNumber" placeholder="Generated on save" readonly></div><div class="col-md-4"><label class="form-label required" for="branch">Reporting branch</label><select class="form-select" id="branch"><option selected disabled>Select branch</option><option>096 - Corporate Branch</option><option>005 - Mumbai Main</option></select></div><div class="col-md-4"><label class="form-label" for="branchId">Branch ID</label><input class="form-control" id="branchId" placeholder="Auto-filled" readonly></div><div class="col-md-4"><label class="form-label" for="branchName">Branch name</label><input class="form-control" id="branchName" placeholder="Auto-filled" readonly></div><div class="col-md-4"><label class="form-label" for="customerType">Customer type</label><select class="form-select" id="customerType"><option selected disabled>Select type</option><option>Corporate</option><option>Individual</option><option>Financial institution</option></select></div><div class="col-md-4"><label class="form-label" for="tradeFlag">Trade flag</label><select class="form-select" id="tradeFlag"><option selected>Regular</option><option>Priority</option></select></div><div class="col-md-4"><label class="form-label" for="lei">LEI flag (code)</label><input class="form-control" id="lei" placeholder="Enter LEI code"></div><div class="col-md-4"><label class="form-label" for="verticalCode">Vertical code</label><input class="form-control" id="verticalCode" placeholder="Enter vertical code"></div><div class="col-md-2 d-flex align-items-end"><div class="form-check mb-2"><input class="form-check-input" id="cardRateCheck" type="checkbox"><label class="form-check-label small" for="cardRateCheck">Card rate: Yes</label></div></div><div class="col-md-2 d-flex align-items-end"><div class="form-check mb-2"><input class="form-check-input" id="email" type="checkbox"><label class="form-check-label small" for="email">Send email</label></div></div></div>
<h3 class="section-heading">Transaction details</h3><div class="row g-3 mb-4 transaction-grid"><div class="col-md-4"><label class="form-label required" for="dealType">Deal type</label><select class="form-select" id="dealType"><option selected disabled>Select deal type</option><option>TT Inward / Outward</option><option>Bills Buy / Bill Sale</option></select></div><div class="col-md-4"><label class="form-label required" for="direction">Purchase / Sale</label><select class="form-select" id="direction"><option selected disabled>Select direction</option><option>Purchase</option><option>Sale</option></select></div><div class="col-md-4"><label class="form-label required" for="valueDate">Value date</label><input class="form-control" id="valueDate" type="date" value="2026-07-18"></div><div class="col-md-4"><label class="form-label required" for="ccy1">Currency pair</label><div class="input-group"><select class="form-select" id="ccy1"><option>USD</option><option>EUR</option><option>GBP</option></select><span class="input-group-text">/</span><select class="form-select" aria-label="Second currency"><option>INR</option></select></div></div><div class="col-md-4"><label class="form-label required" for="amount">Amount</label><div class="input-group"><span class="input-group-text">USD</span><input class="form-control" id="amount" inputmode="decimal" placeholder="0.00"></div></div><div class="col-md-4"><label class="form-label" for="netRate">Net rate</label><input class="form-control" id="netRate" inputmode="decimal" placeholder="0.0000"></div><div class="col-md-4"><label class="form-label" for="margin">Exchange margin</label><div class="input-group"><input class="form-control" id="margin" inputmode="decimal" placeholder="0.00"><span class="input-group-text">PAISA</span></div></div><div class="col-md-4"><label class="form-label" for="cardRate">Card rate</label><input class="form-control" id="cardRate" inputmode="decimal" placeholder="0.0000"></div><div class="col-md-4"><label class="form-label" for="funding">Funding required</label><select class="form-select" id="funding"><option>Yes</option><option>No</option></select></div></div>
<h3 class="section-heading">Settlement & remarks</h3><div class="row g-3 settlement-grid"><div class="col-md-6"><label class="form-label required" for="nostro">Nostro account</label><select class="form-select" id="nostro"><option selected disabled>Select Nostro account</option><option>USD - New York</option><option>EUR - Frankfurt</option></select><small class="text-secondary">Confirm the appropriate Nostro with the TF centre.</small></div><div class="col-md-6"><label class="form-label" for="marginRemark">Margin remark</label><input class="form-control" id="marginRemark" placeholder="Optional margin note"></div><div class="col-12"><label class="form-label" for="remarks">Remarks</label><textarea class="form-control" id="remarks" rows="1" placeholder="Add transaction notes"></textarea></div></div></div><div class="deal-actions d-flex flex-wrap justify-content-end gap-2 p-3 border-top"><a class="btn btn-outline-secondary" href="/home">Cancel</a><button class="btn btn-light border" type="button"><i data-lucide="save" width="16" class="me-1"></i>Save draft</button><button class="btn btn-idbi" type="submit">Review deal <i data-lucide="arrow-right" width="16" class="ms-1"></i></button></div></form></div>
<div class="deal-rate-column"><aside class="panel rate-feed deal-rate-feed"><div class="rate-feed-header d-flex justify-content-between align-items-center"><h2 class="panel-title">Rate feed</h2><span class="badge border border-light border-opacity-25"><span class="status-dot"></span>LIVE</span></div><ul class="nav nav-tabs nav-fill" id="dealRateTabs" role="tablist"><li class="nav-item" role="presentation"><button class="nav-link active" id="tt-tab" data-bs-toggle="tab" data-bs-target="#tt-rates" type="button" role="tab" aria-controls="tt-rates" aria-selected="true">TT Inward / Outward</button></li><li class="nav-item" role="presentation"><button class="nav-link" id="bills-tab" data-bs-toggle="tab" data-bs-target="#bill-rates" type="button" role="tab" aria-controls="bill-rates" aria-selected="false">Bills Buy / Bill Sale</button></li></ul><div class="tab-content"><div class="tab-pane fade show active" id="tt-rates" role="tabpanel" aria-labelledby="tt-tab"><div data-rate-table></div></div><div class="tab-pane fade" id="bill-rates" role="tabpanel" aria-labelledby="bills-tab"><div data-rate-table></div></div></div><div class="feed-meta d-flex justify-content-between"><span>Rate ID: 260715121930</span><span><i data-lucide="refresh-cw" width="11"></i> 16s</span></div></aside></div></div>
<%@ include file="partials/footer.jsp" %></main>
</div><script src="/assets/vendor/lucide/lucide.min.js"></script><script src="/assets/vendor/bootstrap/bootstrap.bundle.min.js"></script><script src="/assets/js/app.js"></script><script>
  document.addEventListener("DOMContentLoaded", () => {
    const rateRows = [["USD/INR","94.89","97.21"],["EUR/INR","105.51","109.35"],["JPY/INR","58.00","NA"],["GBP/INR","124.03","128.45"],["CHF/INR","116.80","121.54"],["AUD/INR","64.00","66.49"],["CAD/INR","67.60","70.35"],["SGD/INR","72.85","75.82"],["HKD/INR","12.01","NA"],["NOK/INR","9.65","10.11"],["AED/INR","25.50","NA"]];
    const table = `<div class="table-responsive"><table class="table align-middle"><thead><tr><th>Pair</th><th class="text-end">Inward</th><th class="text-end">Outward</th></tr></thead><tbody>${rateRows.map(([pair,inward,outward]) => `<tr><td class="pair">${pair}</td><td class="rate ${pair === "AUD/INR" ? "rate-alert" : ""}">${inward}</td><td class="rate ${pair === "AUD/INR" ? "rate-alert" : outward === "NA" ? "text-secondary" : ""}">${outward}</td></tr>`).join("")}</tbody></table></div>`;
    document.querySelectorAll("[data-rate-table]").forEach((target) => { target.innerHTML = table; });
  });
</script></body></html>

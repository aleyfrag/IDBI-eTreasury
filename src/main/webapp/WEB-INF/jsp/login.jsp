<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" href="/favicon.ico" sizes="any">
  <title>Login | IDBI e-Treasury</title>
  <link href="/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="/assets/css/treasury-confluence.css">
  <link rel="stylesheet" href="/assets/css/components/header.css">
  <style>
    .login-rate-feed { max-width: 660px; background: rgba(7,75,71,.9); color: #fff; border: 1px solid rgba(255,255,255,.3); border-radius: 7px; overflow: hidden; box-shadow: 0 18px 45px rgba(0,35,34,.28); backdrop-filter: blur(8px); }
    .login-rate-feed .feed-title { padding: .85rem 1rem; color: #fff; background: rgba(4,48,46,.72); border-bottom: 3px solid #c6530d; }
    .login-rate-feed .nav-tabs { padding: .6rem .75rem 0; border-bottom-color: rgba(255,255,255,.18); background: rgba(9,91,86,.68); }
    .login-rate-feed .nav-link { color: rgba(255,255,255,.68); border-color: transparent; border-radius: 4px 4px 0 0; font-size: .78rem; font-weight: 800; }
    .login-rate-feed .nav-link.active { color: #ffd2ae; background: rgba(255,255,255,.12); border-color: rgba(255,255,255,.18); border-bottom-color: transparent; }
    .login-rate-feed .table { --bs-table-bg: transparent; --bs-table-color: #fff; margin: 0; font-variant-numeric: tabular-nums; }
    .login-rate-feed .table th { padding: .5rem .75rem; color: rgba(255,255,255,.66); background: rgba(4,48,46,.18); border-color: rgba(255,255,255,.14); border-bottom-width: 1px; font-size: .66rem; }
    .login-rate-feed .table td { padding: .38rem .75rem; color: #fff; border-color: rgba(255,255,255,.12); font-size: .78rem; }
    .login-rate-feed .pair { color: #7ee0d5 !important; font-weight: 800; }
    .login-rate-feed .rate { color: #c9f3df !important; text-align: right; font-weight: 800; }
    .login-rate-feed .rate.text-secondary { color: rgba(255,255,255,.55) !important; }
    .login-rate-feed .rate-alert { color: #ff9a9f !important; }
    .login-rate-feed .feed-meta { padding: .6rem .9rem; color: rgba(255,255,255,.68); background: rgba(4,48,46,.4); font-size: .68rem; }
    @media (max-width: 575.98px) {
      .login-rate-feed .table th, .login-rate-feed .table td { padding-inline: .55rem; }
    }
    @media (min-width: 992px) and (max-height: 900px) {
      .login-rate-feed { max-width: 720px; }
      .login-rate-feed .feed-title { padding: .55rem .8rem; }
      .login-rate-feed .nav-tabs { padding-top: .3rem; }
      .login-rate-feed .nav-link { padding: .4rem .75rem; }
      .login-rate-feed .table th { padding: .32rem .65rem; }
      .login-rate-feed .table td { padding: .24rem .65rem; line-height: 1.15; }
      .login-rate-feed .feed-meta { padding: .42rem .75rem; }
    }
  </style>
</head>
<body>
  <main class="login-shell">
    <section class="login-panel">
      <a class="brand-mark" href="/login" aria-label="IDBI e-Treasury home">
        <span class="brand-icon" aria-hidden="true"></span>
        <span><span class="brand-name"><span class="text-idbi">IDBI</span> <span class="text-teal">e-Treasury</span></span><small class="brand-subtitle">Treasury Application Gallery</small></span>
      </a>
      <div class="login-form-wrap py-5">
        <div class="mb-4"><span class="badge rounded-pill badge-soft-success px-3 py-2 mb-3"><span class="status-dot"></span>Secure employee access</span><h1>Welcome back</h1><p class="text-secondary mb-0">Sign in with your EIN and network password.</p></div>
        <div id="login-message" class="alert d-none" role="alert"></div>
        <form action="/authenticate" method="post">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <div class="mb-3"><label class="form-label required" for="ein">Employee ID (EIN)</label><div class="input-group"><span class="input-group-text"><i data-lucide="user" width="17"></i></span><input class="form-control" id="ein" name="username" value="int12991" autocomplete="username" required></div></div>
          <div class="mb-2"><div class="d-flex justify-content-between"><label class="form-label required" for="password">Password</label><a class="small text-teal fw-semibold" href="#">Need help?</a></div><div class="input-group"><span class="input-group-text"><i data-lucide="lock-keyhole" width="17"></i></span><input class="form-control" id="password" name="password" type="password" autocomplete="current-password" required><button class="btn btn-outline-secondary" type="button" data-password-toggle="password" aria-label="Show password"><i data-lucide="eye" width="17"></i></button></div></div>
          <div class="form-check my-4"><input class="form-check-input" type="checkbox" id="device"><label class="form-check-label small" for="device">Remember this device</label></div>
          <button class="btn btn-idbi w-100 py-2" type="submit">Sign in securely <i data-lucide="arrow-right" width="17" class="ms-1"></i></button>
        </form>
        <div class="d-flex align-items-start gap-2 mt-4 p-3 bg-light border rounded-1"><i data-lucide="shield-check" width="18" class="text-teal flex-shrink-0 mt-1"></i><small class="text-secondary">Authorised IDBI Bank users only. Your access and activity may be monitored for security purposes.</small></div>
      </div>
      <footer class="small text-secondary">© 2026 IDBI Bank Ltd. Internal Treasury System <span class="mx-2">•</span> Version 5.0</footer>
    </section>
    <section class="login-visual" aria-label="Treasury market overview">
      <div><span class="badge border border-light border-opacity-25 text-white mb-4">IDBI Bank Treasury</span><h2 class="display-5 fw-bold col-lg-9">Banking on insight.<br>Trading with confidence.</h2><p class="lead col-lg-8 opacity-75">A unified workspace for forex deals, rates, positions and treasury operations.</p></div>
      <div class="login-rate-feed" aria-label="Foreign exchange rate feed">
        <div class="feed-title d-flex justify-content-between align-items-center"><strong><i data-lucide="radio-tower" width="17" class="me-2"></i>Rate Feed</strong><span class="badge border border-light border-opacity-25"><span class="status-dot"></span>LIVE</span></div>
        <ul class="nav nav-tabs" role="tablist"><li class="nav-item" role="presentation"><button class="nav-link active" type="button" role="tab" aria-selected="true">TT Inward / Outward</button></li><li class="nav-item" role="presentation"><button class="nav-link" type="button" role="tab" aria-selected="false">Bills Buy / Bill Sale</button></li></ul>
        <div class="table-responsive"><table class="table align-middle"><thead><tr><th>Currency pair</th><th class="text-end">Inward</th><th class="text-end">Outward</th></tr></thead><tbody>
          <tr><td class="pair">USD/INR</td><td class="rate">94.89</td><td class="rate">97.21</td></tr>
          <tr><td class="pair">EUR/INR</td><td class="rate">105.51</td><td class="rate">109.35</td></tr>
          <tr><td class="pair">JPY/INR</td><td class="rate">58.00</td><td class="rate text-secondary">NA</td></tr>
          <tr><td class="pair">GBP/INR</td><td class="rate">124.03</td><td class="rate">128.45</td></tr>
          <tr><td class="pair">CHF/INR</td><td class="rate">116.80</td><td class="rate">121.54</td></tr>
          <tr><td class="pair">AUD/INR</td><td class="rate rate-alert">64.00</td><td class="rate rate-alert">66.49</td></tr>
          <tr><td class="pair">CAD/INR</td><td class="rate">67.60</td><td class="rate">70.35</td></tr>
          <tr><td class="pair">SGD/INR</td><td class="rate">72.85</td><td class="rate">75.82</td></tr>
          <tr><td class="pair">HKD/INR</td><td class="rate">12.01</td><td class="rate text-secondary">NA</td></tr>
          <tr><td class="pair">NOK/INR</td><td class="rate">9.65</td><td class="rate">10.11</td></tr>
          <tr><td class="pair">AED/INR</td><td class="rate">25.50</td><td class="rate text-secondary">NA</td></tr>
        </tbody></table></div>
        <div class="feed-meta d-flex justify-content-between"><span>Rate ID: 260715121930</span><span><i data-lucide="refresh-cw" width="12" class="me-1"></i>Next refresh: 16s</span></div>
      </div>
    </section>
  </main>
  <script src="/assets/vendor/lucide/lucide.min.js"></script><script src="/assets/js/app.js"></script><script>
    const params = new URLSearchParams(location.search);
    const message = document.getElementById('login-message');
    const showMessage = (text, style) => {
      message.textContent = text;
      message.classList.add(style);
      message.classList.remove('d-none');
    };
    if (params.has('concurrent')) {
      showMessage('This employee ID already has an active session. Sign out there before trying again.', 'alert-warning');
    } else if (params.has('expired')) {
      showMessage('Your session expired. Please sign in again.', 'alert-warning');
    } else if (params.has('userNotRegistered') || params.has('userNotFound')) {
      showMessage('Your LDAP login is valid, but this employee ID is not registered in e-Treasury. Contact Treasury support.', 'alert-warning');
    } else if (params.has('dormant')) {
      showMessage('Your e-Treasury user is dormant or deactivated. Contact Treasury support.', 'alert-warning');
    } else if (params.has('deactivated')) {
      showMessage('Your e-Treasury user is not active or not approved. Contact Treasury support.', 'alert-warning');
    } else if (params.has('menuAccessDenied')) {
      showMessage('No application menu is assigned to this employee ID. Contact support.', 'alert-warning');
    } else if (params.has('menuLoadError')) {
      showMessage('Unable to load application data. Please try again or contact support.', 'alert-danger');
    } else if (params.has('error')) {
      showMessage('Invalid employee ID or password.', 'alert-danger');
    } else if (params.has('logout')) {
      showMessage('You have signed out securely.', 'alert-success');
    }
  </script>
</body></html>

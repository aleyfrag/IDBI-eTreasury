<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" href="/favicon.ico" sizes="any">
  <title>Error | IDBI e-Treasury</title>
  <link href="/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="/assets/css/treasury-confluence.css">
</head>
<body>
  <main class="container py-5">
    <section class="panel mx-auto p-4" style="max-width:680px">
      <span class="badge badge-soft-danger mb-3">Request failed</span>
      <h1 class="page-title">${errorTitle}</h1>
      <p class="text-secondary">${errorMessage}</p>
      <p class="small text-secondary">Reference: <strong>${errorReference}</strong></p>
      <a class="btn btn-idbi" href="/home">Return to dashboard</a>
    </section>
  </main>
</body>
</html>

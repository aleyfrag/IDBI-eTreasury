<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<layout:core pageTitle="IDBIForex | IDBI e-Treasury"
             pageName="idbiforex-home"
             activeMenuId="${activeMenuId}"
             activeSubMenuId="${activeSubMenuId}">
  <jsp:attribute name="headContent">
  <style>
    .idbiforex-titlebar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 1rem;
      border-bottom: 3px solid #bed600;
    }

    .idbiforex-module-icon {
      display: grid;
      width: 46px;
      height: 46px;
      flex: 0 0 auto;
      place-items: center;
      color: #fff;
      background: var(--idbi-red);
      border-radius: 8px;
    }

    .idbiforex-introduction {
      margin-bottom: 1.5rem;
      padding: 1rem 1.1rem;
      color: #334846;
      background: #f3f8f7;
      border-left: 4px solid #007d89;
      border-radius: 4px;
      font-size: .88rem;
      line-height: 1.65;
    }

    .idbiforex-process {
      margin-bottom: 1.5rem;
    }

    .idbiforex-process h2 {
      display: flex;
      align-items: center;
      gap: .5rem;
      margin-bottom: .2rem;
      color: #003968;
      font-size: 1rem;
      font-weight: 800;
    }

    .process-subtitle {
      margin: 0 0 .7rem 1.65rem;
      color: #62716f;
      font-size: .76rem;
      font-weight: 700;
    }

    .idbiforex-process ol {
      margin-bottom: 0;
      padding-left: 2.7rem;
      color: #354643;
      font-size: .84rem;
      line-height: 1.6;
    }

    .idbiforex-process li + li {
      margin-top: .45rem;
    }

    .idbiforex-links nav {
      display: grid;
      gap: .4rem;
    }

    .idbiforex-links a {
      display: flex;
      align-items: center;
      gap: .65rem;
      padding: .7rem .75rem;
      color: #185e56;
      background: #f6f9f8;
      border: 1px solid #e0e8e6;
      border-radius: 5px;
      font-size: .77rem;
      font-weight: 700;
      text-decoration: none;
    }

    .idbiforex-links a:hover,
    .idbiforex-links a:focus-visible {
      color: #003968;
      background: #edf5f3;
      border-color: #bed6d1;
    }

    @media (max-width: 575.98px) {
      .idbiforex-introduction {
        padding: .85rem;
      }

      .idbiforex-process ol {
        padding-left: 1.5rem;
      }

      .process-subtitle {
        margin-left: 0;
      }
    }
  </style>
  </jsp:attribute>
  <jsp:body>
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb small mb-3">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
        <li class="breadcrumb-item active">IDBIForex</li>
      </ol>
    </nav>

    <div class="row g-3 align-items-stretch">
      <div class="col-xl-9">
        <article class="panel idbiforex-guide h-100">
          <div class="panel-header idbiforex-titlebar">
            <div>
              <div class="page-kicker mb-1">FOREIGN EXCHANGE MODULE</div>
              <h1 class="page-title mb-1">IDBIForex</h1>
              <p class="text-secondary small mb-0">Branch guidance for inward and outward foreign-currency transactions.</p>
            </div>
            <span class="idbiforex-module-icon"><i data-lucide="circle-dollar-sign" width="24"></i></span>
          </div>

          <div class="p-3 p-lg-4">
            <p class="idbiforex-introduction">
              <strong>IDBIForex</strong>, a module under e-Treasury, is an online platform through which Retail and
              Trade Finance branches can cover foreign-exchange rates up to USD 100,000 or equivalent. Before
              covering a rate, branches must verify the documentation, transaction authenticity and applicable
              regulatory requirements. Transactions may carry financial and compliance implications.
            </p>

            <section class="idbiforex-process">
              <h2><i data-lucide="arrow-down-to-line" width="18"></i>For Inward</h2>
              <p class="process-subtitle">Customer sells foreign currency and converts it to INR</p>
              <ol>
                <li>After receiving communication from Trade Finance regarding an inward remittance, the branch informs the customer and requests the purpose of remittance and disposal instructions.</li>
                <li>The customer provides the required purpose and disposal instructions. The branch may cover the rate directly or through IDBIForex and must communicate the purpose, instructions, deal number and covered rate to Trade Finance.</li>
                <li>Before blocking a rate, the branch must confirm with Trade Finance that the transaction purpose complies with regulatory guidelines.</li>
              </ol>
            </section>

            <section class="idbiforex-process mb-0">
              <h2><i data-lucide="arrow-up-from-line" width="18"></i>For Outward</h2>
              <p class="process-subtitle">Customer buys foreign currency by providing INR</p>
              <ol>
                <li>The customer approaches the branch to make an outward payment.</li>
                <li>The branch requests the remittance application, FEMA declaration and any other documents required for the stated purpose, in consultation with Trade Finance.</li>
                <li>The branch may request Trade Finance to take the rate after receiving the customer mandate, or block the rate in IDBIForex according to the customer’s instructions after ensuring sufficient account balance.</li>
              </ol>
            </section>
          </div>
        </article>
      </div>

      <div class="col-xl-3">
        <aside class="panel idbiforex-links h-100">
          <div class="panel-header">
            <h2 class="panel-title"><i data-lucide="link" width="17" class="me-1"></i>Other Links</h2>
            <small class="text-secondary">Forms and reference documents</small>
          </div>
          <nav class="p-3" aria-label="IDBIForex reference documents">
            <a href="#"><i data-lucide="book-open" width="16"></i>IDBIForex User Manual</a>
            <a href="#"><i data-lucide="file-text" width="16"></i>Visa Nationality Form</a>
            <a href="#"><i data-lucide="graduation-cap" width="16"></i>Education Form</a>
            <a href="#"><i data-lucide="users" width="16"></i>Family Maintenance Form</a>
            <a href="#"><i data-lucide="gift" width="16"></i>Gift Form</a>
          </nav>
        </aside>
      </div>
    </div>

  </jsp:body>
</layout:core>

package in.co.idbibank.etreasury.idbiforex.controller;

import in.co.idbibank.etreasury.core.model.MenuItem;
import in.co.idbibank.etreasury.core.model.SubMenuItem;
import in.co.idbibank.etreasury.core.model.TreasuryUserDetails;
import in.co.idbibank.etreasury.idbiforex.exception.IdbiForexDataException;
import in.co.idbibank.etreasury.idbiforex.model.IdbiForexDeal;
import in.co.idbibank.etreasury.idbiforex.service.IdbiForexDealService;
import in.co.idbibank.etreasury.idbiforex.service.IdbiForexPageService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
class IdbiForexControllerTest {

    private final StubDealService dealService = new StubDealService();
    private final IdbiForexPageService pageService = new IdbiForexPageService(dealService);
    private final IdbiForexController controller = new IdbiForexController(pageService);

    @Test
    void successfulDealLoadReturnsModulePageAndFrontendMessage() {
        MockHttpSession session = authenticatedSession();
        ExtendedModelMap model = new ExtendedModelMap();
        LocalDate transactionDate = LocalDate.of(2026, 8, 7);
        dealService.result = List.of(deal());

        String view = controller.deals("2026-08-07", dealRequest(), session, model);

        assertThat(view).isEqualTo("idbiforex/deal-list");
        assertThat(model.get("dealMessageType")).isEqualTo("success");
        assertThat(model.get("dealMessage")).isEqualTo("1 deal loaded successfully.");
        assertThat(model.get("dealCount")).isEqualTo(1);
        assertThat(model.get("totalAmount")).isEqualTo(new BigDecimal("700"));
        assertThat(model.get("activeSubMenuId")).isEqualTo(38L);
    }

    @Test
    void invalidDateReturnsSafeFrontendErrorWithoutCallingDatabase() {
        MockHttpSession session = authenticatedSession();
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.deals("07-Aug-2026", dealRequest(), session, model);

        assertThat(view).isEqualTo("idbiforex/deal-list");
        assertThat(model.get("dealMessageType")).isEqualTo("danger");
        assertThat(model.get("dealMessage")).isEqualTo(
                "Please select a valid transaction date in YYYY-MM-DD format.");
        assertThat(model.get("deals")).isEqualTo(List.of());
        assertThat(dealService.callCount).isZero();
    }

    @Test
    void databaseFailureIsRenderedAsSafeFrontendMessage() {
        MockHttpSession session = authenticatedSession();
        ExtendedModelMap model = new ExtendedModelMap();
        dealService.failure = new IdbiForexDataException("ORA-17059 internal detail");

        String view = controller.deals("2026-08-07", dealRequest(), session, model);

        assertThat(view).isEqualTo("idbiforex/deal-list");
        assertThat(model.get("dealMessageType")).isEqualTo("danger");
        assertThat(model.get("dealMessage").toString()).doesNotContain("ORA-");
        assertThat(model.get("deals")).isEqualTo(List.of());
    }

    @Test
    void separateIdbiFxModuleDoesNotGrantIdbiForexAccess() {
        MockHttpSession session = authenticatedSession();
        session.setAttribute("userMenus", List.of(new MenuItem(
                1,
                "IDBIFx",
                "circle-dollar-sign",
                "TFO",
                "/idbifx/home",
                List.of()
        )));

        String view = controller.deals(
                "2026-08-07", dealRequest(), session, new ExtendedModelMap());

        assertThat(view).isEqualTo("redirect:/home?menuUnavailable");
        assertThat(dealService.callCount).isZero();
    }

    private MockHttpSession authenticatedSession() {
        MockHttpSession session = new MockHttpSession(null, "test-session");
        session.setAttribute("treasuryUser", user());
        session.setAttribute("userMenus", List.of(new MenuItem(
                24,
                "IDBIForex",
                "circle-dollar-sign",
                "MCD",
                "/idbiforex/home",
                List.of(
                        new SubMenuItem(
                                7,
                                "About IDBI-FX",
                                "home",
                                "/idbiforex/home"),
                        new SubMenuItem(
                                38,
                                "IDBI-FX Deal List",
                                "list-checks",
                                "/idbiforex/deals")
                )
        )));
        return session;
    }

    private MockHttpServletRequest dealRequest() {
        return new MockHttpServletRequest("GET", "/idbiforex/deals");
    }

    private TreasuryUserDetails user() {
        return new TreasuryUserDetails(
                183,
                "SUNIL.SAHU",
                "INT17228",
                "183",
                "MCD",
                "A",
                null,
                null,
                "A",
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private IdbiForexDeal deal() {
        return new IdbiForexDeal(
                "FX0508260007",
                "07-Aug-2026",
                "TT-INWARD",
                "100000002",
                "100000002",
                "No",
                "183",
                "PURC",
                "EUR",
                "INR",
                new BigDecimal("700"),
                "07-AUG-2026",
                null,
                "109.66",
                "R",
                null,
                null,
                "Y",
                "1.25",
                "0",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "SUNIL.SAHU",
                "05-Aug-2026",
                null,
                null,
                "VERIFIED",
                null,
                null,
                "05-Aug-2026",
                "183",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "183",
                null,
                null,
                null,
                null
        );
    }

    private static final class StubDealService extends IdbiForexDealService {

        private List<IdbiForexDeal> result = List.of();
        private int callCount;
        private RuntimeException failure;

        private StubDealService() {
            super(null);
        }

        @Override
        public List<IdbiForexDeal> loadDeals(
                TreasuryUserDetails user,
                LocalDate transactionDate) {
            callCount++;
            if (failure != null) {
                throw failure;
            }
            return result;
        }
    }
}

package in.co.idbibank.etreasury.idbiforex.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdbiForexDealServiceTest {

    @Test
    void usesRepoActionRequiredByStoredProcedure() {
        assertThat(IdbiForexDealService.REPORTED_DEAL_ACTION).isEqualTo("REPO");
    }
}

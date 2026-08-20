package in.co.idbibank.etreasury.core.authentication;

import in.co.idbibank.etreasury.core.model.TreasuryUserDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LoginSuccessHandlerStatusTest {

    private final LoginSuccessHandler handler = new LoginSuccessHandler(null);

    @Test
    void nullApplicationUserIsNotReportedAsExpiredSession() {
        assertEquals("userNotRegistered", handler.rejectionCode(null));
    }

    @Test
    void dormantStatusInEitherColumnIsRejectedWithDormantMessage() {
        assertEquals("dormant", handler.rejectionCode(user("D", "A")));
        assertEquals("dormant", handler.rejectionCode(user("A", "u")));
    }

    @Test
    void activeAndApprovedUserIsAccepted() {
        assertNull(handler.rejectionCode(user(" a ", "A")));
    }

    @Test
    void OtherNonActiveStatusIsRejectedAsDeactivated() {
        assertEquals("deactivated", handler.rejectionCode(user("P", "A")));
    }

    private TreasuryUserDetails user(String status, String adminStatus) {
        return new TreasuryUserDetails(
                1L, "USER", "INT0001", "001", "MCD", status, null, null,
                adminStatus, null, null, null, null, null, null);
    }
}

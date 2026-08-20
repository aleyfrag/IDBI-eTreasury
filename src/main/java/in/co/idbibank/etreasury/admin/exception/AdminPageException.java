package in.co.idbibank.etreasury.admin.exception;

/** Raised when the authenticated session does not contain the required Admin menu. */
public class AdminPageException extends RuntimeException {

    public AdminPageException(String message) {
        super(message);
    }
}

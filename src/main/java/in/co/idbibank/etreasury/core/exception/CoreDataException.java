package in.co.idbibank.etreasury.core.exception;

public class CoreDataException extends RuntimeException {

    public CoreDataException(String message) {
        super(message);
    }

    public CoreDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

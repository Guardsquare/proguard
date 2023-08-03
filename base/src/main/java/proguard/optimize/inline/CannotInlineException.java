package proguard.optimize.inline;

public class CannotInlineException extends RuntimeException {
    public CannotInlineException(String reason) {
        super(reason);
    }
}

package proguard.optimize.lambdainline;

/**
 * An exception thrown by the RecursiveInliner that is caught by the BaseLambdaInliner, this allows it to jump out of
 * the visitor it is currently in and abort the inlining process.
 */
public class CannotInlineException extends RuntimeException {
    public CannotInlineException(String reason) {
        super(reason);
    }
}

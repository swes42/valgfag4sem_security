package security.errorhandling;

public class MissingInputException extends Exception {
    public MissingInputException (String message) {
        super(message);
    }
}

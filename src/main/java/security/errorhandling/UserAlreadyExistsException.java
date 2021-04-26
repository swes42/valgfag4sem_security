package security.errorhandling;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException (String message) {
        super(message);
    }
}

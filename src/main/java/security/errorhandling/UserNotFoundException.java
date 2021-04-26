package security.errorhandling;

public class UserNotFoundException extends Exception {
    public UserNotFoundException (String message) {
        super(message);
    }
}


package errorhandling;

public class UserNotFound extends Exception {
    public UserNotFound(String message) {
        super(message);
    }
}


package Exception;

import java.lang.Exception;

public class InvalidTaskException extends Exception {
    public InvalidTaskException(String errorMessage) {
        super(errorMessage);
    }
}

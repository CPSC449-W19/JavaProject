package Exception;

import java.lang.Exception;

public class InvalidPenaltyException extends Exception {
    public InvalidPenaltyException(String errorMessage) {
        super(errorMessage);
    }
}

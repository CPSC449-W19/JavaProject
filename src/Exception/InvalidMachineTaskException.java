package Exception;

import java.lang.Exception;

public class InvalidMachineTaskException extends Exception {
    public InvalidMachineTaskException(String errorMessage) {
        super(errorMessage);
    }
}

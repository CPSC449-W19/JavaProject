package Exception;

import java.lang.Exception;

public class ParsingInputException extends Exception {
    public ParsingInputException(String errorMessage) {
        super(errorMessage);
    }
}

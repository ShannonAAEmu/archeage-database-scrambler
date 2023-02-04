package info.aaemu.shannon.tools.exception;

public class BadParameterException extends ApplicationException{

    public BadParameterException() {
        super();
    }

    public BadParameterException(String message) {
        super(message);
    }

    public BadParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadParameterException(Throwable cause) {
        super(cause);
    }
}

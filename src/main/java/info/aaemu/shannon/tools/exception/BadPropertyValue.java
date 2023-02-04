package info.aaemu.shannon.tools.exception;

public class BadPropertyValue extends ApplicationException {

    public BadPropertyValue() {
        super();
    }

    public BadPropertyValue(String message) {
        super(message);
    }

    public BadPropertyValue(String message, Throwable cause) {
        super(message, cause);
    }

    public BadPropertyValue(Throwable cause) {
        super(cause);
    }
}

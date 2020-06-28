package exceptions;

public class GigaException extends Exception {
    private final String message;
    private final boolean type;


    public GigaException(String message, boolean type) {
        this.message = message;
        //false means warning, true means error
        this.type = type;
    }


    public String getMessage() {
        return message;
    }
    public boolean getType() {
        return type;
    }
}

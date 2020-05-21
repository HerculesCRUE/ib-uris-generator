package es.um.asio.service.error;

public class CustomNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7718828512143293558L;

    public CustomNotFoundException(String message, Throwable cause) {
        super(message,cause);
    }

    public CustomNotFoundException(String message) {
        super(message);
    }

    public CustomNotFoundException() {
    }
}

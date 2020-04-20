package es.um.asio.back.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Not found")
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

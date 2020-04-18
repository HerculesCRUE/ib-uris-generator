package es.um.asio.back.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Not found")
public class CustomNotFoundException extends RuntimeException {
}

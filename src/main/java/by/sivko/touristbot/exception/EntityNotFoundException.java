package by.sivko.touristbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7663198376427589170L;

    private static final String ERROR_MESSAGE = "Entity [%s] with %s [%s] not found";
    private static final String DEFAULT_MESSAGE = "Entity not found";

    public EntityNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public EntityNotFoundException(String className, String propertyName, String propertyValue) {
        super(String.format(ERROR_MESSAGE, className, propertyName, propertyValue));
    }
}

package practice.server.practiceServer.exception;

import javax.validation.ValidationException;

public class LoadExceptionValidation extends ValidationException {
    public LoadExceptionValidation(String message) {
        super(message);
    }
}
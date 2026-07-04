package practice.server.practiceServer.exception;

import jakarta.validation.ValidationException;

public class LoadExceptionValidation extends ValidationException {
    public LoadExceptionValidation(String message) {
        super(message);
    }
}
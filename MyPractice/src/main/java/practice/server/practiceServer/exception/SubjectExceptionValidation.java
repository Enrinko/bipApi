package practice.server.practiceServer.exception;

import jakarta.validation.ValidationException;

public class SubjectExceptionValidation extends ValidationException {
    public SubjectExceptionValidation(String message) {
        super(message);
    }
}
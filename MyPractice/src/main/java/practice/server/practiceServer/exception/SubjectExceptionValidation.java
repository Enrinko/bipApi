package practice.server.practiceServer.exception;

import javax.validation.ValidationException;

public class SubjectExceptionValidation extends ValidationException {
    public SubjectExceptionValidation(String message) {
        super(message);
    }
}
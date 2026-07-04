package practice.server.practiceServer.exception;

import jakarta.validation.ValidationException;

public class GroupExceptionValidation extends ValidationException {
    public GroupExceptionValidation(String message) {
        super(message);
    }
}
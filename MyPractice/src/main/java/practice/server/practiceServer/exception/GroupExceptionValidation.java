package practice.server.practiceServer.exception;

import javax.validation.ValidationException;

public class GroupExceptionValidation extends ValidationException {
    public GroupExceptionValidation(String message) {
        super(message);
    }
}
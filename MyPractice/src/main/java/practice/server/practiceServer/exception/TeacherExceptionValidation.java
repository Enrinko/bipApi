package practice.server.practiceServer.exception;

import jakarta.validation.ValidationException;

public class TeacherExceptionValidation extends ValidationException {
    public TeacherExceptionValidation(String message) {
        super(message);
    }
}
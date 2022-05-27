package practice.server.practiceServer.utils;

import practice.server.practiceServer.entity.SubjectEntity;
import practice.server.practiceServer.exception.SubjectExceptionValidation;

import java.util.regex.Pattern;

public class SubjectValidation {
    public static void validation(SubjectEntity entity) {
        if (entity.getSubjectName() == null) {
            throw new SubjectExceptionValidation("Предмет должен быть заполнен");
        }
    }
}

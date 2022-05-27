package practice.server.practiceServer.utils;

import practice.server.practiceServer.entity.TeacherEntity;
import practice.server.practiceServer.exception.TeacherExceptionValidation;

import java.util.regex.Pattern;

public class TeacherValidation {
    public static void validation(TeacherEntity entity) {
        if (entity.getInitials() == null) {
            throw new TeacherExceptionValidation("Учитель должен быть заполнен");
        }
        Pattern pattern = Pattern.compile("[А-Я][а-я]+\\s+[А-Я]\\.[А-Я]\\.");
        if (!pattern.matcher(entity.getInitials()).find()) {
            throw new TeacherExceptionValidation("Учитель должен быть заполнен по шаблону");
        }
     }
}

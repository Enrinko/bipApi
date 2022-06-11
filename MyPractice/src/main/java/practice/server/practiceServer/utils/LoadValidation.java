package practice.server.practiceServer.utils;

import practice.server.practiceServer.entity.LoadEntity;
import practice.server.practiceServer.exception.LoadExceptionValidation;

import java.util.regex.Pattern;

public class LoadValidation {
    public static void validation(LoadEntity entity) {
        if (entity.getTeacherInLoad() == null)
            throw new LoadExceptionValidation("Учитель должен быть заполнен");
        if (entity.getGroupInLoad() == null)
            throw new LoadExceptionValidation("Группа должна быть заполнена");
        if (entity.getSubjectInLoad() == null)
            throw new LoadExceptionValidation("Предмет должен быть заполнен");
        Pattern pattern = Pattern.compile("^[0-9]+$");
        if (entity.getSemesterFirstHours() < 0)
            throw new LoadExceptionValidation("Данные за первый семестр должны быть заполнены");
        if (!pattern.matcher(entity.getSemesterFirstHours().toString()).find())
            throw new LoadExceptionValidation("Данные за первый семестр должны быть цифрами");
        if (entity.getSemesterSecondHours() < 0)
            throw new LoadExceptionValidation("Данные за второй семестр должны быть заполнены");
        if (!pattern.matcher(entity.getSemesterSecondHours().toString()).find())
            throw new LoadExceptionValidation("Данные за второй семестр должны быть цифрами");
        if (entity.getConsultationHours() < 0)
            throw new LoadExceptionValidation("Данные за консультацию должны быть заполнены");
        if (!pattern.matcher(entity.getConsultationHours().toString()).find())
            throw new LoadExceptionValidation("Данные за консультацию должны быть цифрами");
        if (entity.getCourseworkHours() < 0)
            throw new LoadExceptionValidation("Данные за курсовые должны быть заполнены");
        if (!pattern.matcher(entity.getCourseworkHours().toString()).find())
            throw new LoadExceptionValidation("Данные за курсовые должны быть цифрами");
        if (entity.getDiplomaHours() < 0) throw new LoadExceptionValidation("Данные за диплом должны быть заполнены");
        if (!pattern.matcher(entity.getDiplomaHours().toString()).find())
            throw new LoadExceptionValidation("Данные за диплом должны быть цифрами");
        if (entity.getExamHours() < 0) throw new LoadExceptionValidation("Данные за экзамен должны быть заполнены");
        if (!pattern.matcher(entity.getExamHours().toString()).find())
            throw new LoadExceptionValidation("Данные за экзамен должны быть цифрами");
    }
}
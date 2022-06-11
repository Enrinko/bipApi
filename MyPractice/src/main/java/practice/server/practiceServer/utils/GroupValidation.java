package practice.server.practiceServer.utils;

import practice.server.practiceServer.entity.GroupEntity;
import practice.server.practiceServer.exception.GroupExceptionValidation;

import java.util.regex.Pattern;

public class GroupValidation {
    public static void validation(GroupEntity entity) {
        if (entity.getGroupName() == null || entity.getGroupName().isBlank())
            throw new GroupExceptionValidation("Группа должна быть заполнена");
        Pattern pattern = Pattern.compile("[0-9]*[А-Я]+-[0-9]+[А-Я]*");
        if (!pattern.matcher(entity.getGroupName()).find())
            throw new GroupExceptionValidation("Группа должна быть заполнена по образцу");
    }
}
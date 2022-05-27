package practice.server.practiceServer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.server.practiceServer.entity.*;
import practice.server.practiceServer.response.BaseResponse;
import practice.server.practiceServer.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("bipapi/fromexcel")
@AllArgsConstructor
public class LogicController {
    public GroupService group;
    public LoadService load;
    public SubjectService subject;
    public TeacherService teacher;

    @PostMapping("/conversion")
    public ResponseEntity<BaseResponse> convertToDB(@RequestBody List<LoadEntity> sheets) {
        Pattern pattern = Pattern.compile("[0-9]*[А-Я]+-[0-9]+[А-Я]*");
        Pattern patternChars = Pattern.compile("[А-Я]+");
        Matcher matcherMain;
        Matcher matcherChars;
        int size = sheets.size();
        for (int i = 0; i < sheets.size(); i++) {
            LoadEntity sheet = sheets.get(i);
            if (load.getAll().contains(sheet)) {
                sheets.remove(sheet);
                continue;
            }
            GroupEntity groupMaybe = new GroupEntity(sheet.getGroupInLoad().getGroupName());
            if (group.getAll().contains(groupMaybe)) {
                continue;
            }
            SubjectEntity subjectMaybe = new SubjectEntity(sheet.getSubjectInLoad().getSubjectName());
            if (subject.getAll().contains(subjectMaybe)) {
                continue;
            }
            TeacherEntity teacherMaybe = new TeacherEntity(sheet.getTeacherInLoad().getInitials());
            if (teacher.getAll().contains(sheet.getTeacherInLoad())) {
                continue;
            }
            matcherMain = pattern.matcher(sheet.getGroupInLoad().getGroupName());
            matcherChars = patternChars.matcher(sheet.getGroupInLoad().getGroupName());
            if (!matcherMain.find() && matcherChars.find()) {
                groupMaybe.setGroupName(matcherChars.replaceFirst(
                        sheet.getGroupInLoad().getGroupName().substring(
                                matcherChars.start(),
                                matcherChars.end()).concat("-")));
            }
            group.save(groupMaybe);
            teacher.save(teacherMaybe);
            subject.save(subjectMaybe);
        }
        load.saveAll(sheets);
        return ResponseEntity.ok(new BaseResponse(size == sheets.size()? "Из файла были добавлены все записи" : "Количество записей, не добавленных в бд:" + (sheets.size() - size), true));
    }
}

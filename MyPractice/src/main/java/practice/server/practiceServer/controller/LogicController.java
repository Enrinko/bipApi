package practice.server.practiceServer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.server.practiceServer.entity.GroupEntity;
import practice.server.practiceServer.entity.LoadEntity;
import practice.server.practiceServer.entity.SubjectEntity;
import practice.server.practiceServer.entity.TeacherEntity;
import practice.server.practiceServer.response.BaseResponse;
import practice.server.practiceServer.service.GroupService;
import practice.server.practiceServer.service.LoadService;
import practice.server.practiceServer.service.SubjectService;
import practice.server.practiceServer.service.TeacherService;

import java.util.ArrayList;
import java.util.HashSet;
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
        load.truncateMyTable();
        subject.deleteAll();
        group.deleteAll();
        teacher.deleteAll();
        int size = sheets.size();
        iterateHashToList(sheets);
        List<LoadEntity> loads = load.getAll();
        List<LoadEntity> finalLoads = new ArrayList<>();
        for (int i = 0; i < sheets.size(); i++) {
            LoadEntity loades = sheets.get(i);
            loades.setGroupInLoad(group.findByName(loades.getGroupInLoad().getGroupName()));
            loades.setTeacherInLoad(teacher.findByName(loades.getTeacherInLoad().getInitials()));
            List<SubjectEntity> subjectsFuckYourself = subject.findByName(loades.getSubjectInLoad().getSubjectName());
            SubjectEntity subject = new SubjectEntity();
            for (int j = 0; j < subjectsFuckYourself.size(); j++)
                subject = subjectsFuckYourself.get(j).getSubjectName().equals(loades.getSubjectInLoad().getSubjectName()) ? subjectsFuckYourself.get(j) : null;
            loades.setSubjectInLoad(subject);
            if (!loads.contains(loades)) {
                loads.add(loades);
                finalLoads.add(loades);
            }
        }
        load.saveAll(finalLoads);
        return ResponseEntity.ok(new BaseResponse(size == sheets.size() ? "Из файла были добавлены все записи" : "Количество записей, не добавленных в бд:" + (sheets.size() - size), true));
    }

    public void iterateHashToList(List<LoadEntity> loads) {
        Pattern pattern = Pattern.compile("[0-9]*[А-Я]+-[0-9]+[А-Я]*");
        Pattern patternChars = Pattern.compile("[А-Я]+");
        Matcher matcherMain;
        Matcher matcherChars;
        HashSet<GroupEntity> groupsHash = new HashSet<>();
        HashSet<TeacherEntity> teachersHash = new HashSet<>();
        HashSet<SubjectEntity> subjectsHash = new HashSet<>();
        for (int i = 0; i < loads.size(); i++) {
            LoadEntity loadMaybe = loads.get(i);
            GroupEntity groupMaybe = loadMaybe.getGroupInLoad();
            matcherMain = pattern.matcher(groupMaybe.getGroupName());
            matcherChars = patternChars.matcher(groupMaybe.getGroupName());
            if (!matcherMain.find() && matcherChars.find())
                groupMaybe.setGroupName(matcherChars.replaceFirst(loadMaybe.getGroupInLoad().getGroupName().substring(matcherChars.start(), matcherChars.end()).concat("-").trim()).trim());
            groupMaybe.setGroupName(groupMaybe.getGroupName().replace(" ", ""));
            groupsHash.add(groupMaybe);
            TeacherEntity teacherMaybe = loadMaybe.getTeacherInLoad();
            teacherMaybe.setInitials(loadMaybe.getTeacherInLoad().getInitials().trim());
            teachersHash.add(teacherMaybe);
            SubjectEntity subjectMaybe = loadMaybe.getSubjectInLoad();
            subjectMaybe.setSubjectName(subjectMaybe.getSubjectName().trim());
            subjectsHash.add(subjectMaybe);
        }
        List<GroupEntity> groups = new ArrayList<>(groupsHash.stream().toList());
        List<TeacherEntity> teachers = new ArrayList<>(teachersHash.stream().toList());
        List<SubjectEntity> subjects = new ArrayList<>(subjectsHash.stream().toList());
        group.saveAll(groups);
        teacher.saveAll(teachers);
        subject.saveAll(subjects);
    }
}
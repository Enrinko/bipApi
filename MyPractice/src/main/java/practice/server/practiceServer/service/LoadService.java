package practice.server.practiceServer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.server.practiceServer.entity.LoadEntity;
import practice.server.practiceServer.repo.LoadRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class LoadService {
    public LoadRepo repo;

    public void saveAll(List<LoadEntity> groupList) {
        repo.saveAll(groupList);
    }

    public void save(LoadEntity group) {
        repo.save(group);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<LoadEntity> getAll() {
        return repo.findAll();
    }

    public LoadEntity getById(Long id) {
        return repo.findById(id).isPresent() ? repo.findById(id).get() : null;
    }

    public List<LoadEntity> getLoadBy(String groupname, String initials, String subjectname) {
        return repo.findByGroupInLoad_GroupNameOrTeacherInLoad_InitialsOrSubjectInLoad_SubjectName(groupname, initials, subjectname);
    }

    public List<Integer> finalLoadOfTeacher(List<LoadEntity> loads) {
        List<Integer> summs = new ArrayList<>();
        Integer semester1 = 0;
        Integer semester2 = 0;
        Integer exam = 0;
        Integer coursework = 0;
        Integer diploma = 0;
        Integer consultations = 0;
        for (int i = 0; i < loads.size(); i++) {
            semester1 += loads.get(i).getSemesterFirstHours();
            semester2 += loads.get(i).getSemesterSecondHours();
            exam += loads.get(i).getExamHours();
            coursework += loads.get(i).getCourseworkHours();
            diploma += loads.get(i).getDiplomaHours();
            consultations += loads.get(i).getConsultationHours();
        }
        summs.add(semester1);
        summs.add(semester2);
        summs.add(exam);
        summs.add(coursework);
        summs.add(diploma);
        summs.add(consultations);
        return summs;
    }

    @Transactional
    public void truncateMyTable() {
        repo.truncateMyTable();
    }
}
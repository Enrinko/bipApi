package practice.server.practiceServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practice.server.practiceServer.entity.GroupEntity;
import practice.server.practiceServer.entity.SubjectEntity;
import practice.server.practiceServer.entity.TeacherEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadModel {
    private Long id;
    private Integer semesterFirstHours;
    private Integer semesterSecondHours;
    private Integer examHours;
    private Integer courseworkHours;
    private Integer diplomaHours;
    private Integer consultationHours;
    private String teacherInLoad;
    private String subjectInLoad;
    private String groupInLoad;


    public LoadModel(Integer semesterFirstHours, Integer semesterSecondHours, Integer examHours, Integer courseworkHours,
                     Integer diplomaHours, Integer consultationHours, String initials, String subjectName, String groupName) {
        this.semesterFirstHours = semesterFirstHours;
        this.semesterSecondHours = semesterSecondHours;
        this.examHours = examHours;
        this.courseworkHours = courseworkHours;
        this.diplomaHours = diplomaHours;
        this.consultationHours = consultationHours;
        this.teacherInLoad = initials;
        this.subjectInLoad = subjectName;
        this.groupInLoad = groupName;
    }
}

package practice.server.practiceServer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "LoadOfTeachers")
@AllArgsConstructor
@NoArgsConstructor
public class LoadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private Integer semesterFirstHours;
    @NotBlank
    private Integer semesterSecondHours;
    @NotBlank
    private Integer examHours;
    @NotBlank
    private Integer courseworkHours;
    @NotBlank
    private Integer diplomaHours;
    @NotBlank
    private Integer consultationHours;
    @NotNull
    @ManyToOne
    private TeacherEntity teacherInLoad;
    @NotNull
    @ManyToOne
    private SubjectEntity subjectInLoad;
    @NotNull
    @ManyToOne
    private GroupEntity groupInLoad;

    public LoadEntity(Integer semesterFirstHours, Integer semesterSecondHours, Integer examHours, Integer courseworkHours,
                     Integer diplomaHours, Integer consultationHours, TeacherEntity initials, SubjectEntity subjectName, GroupEntity groupName) {
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

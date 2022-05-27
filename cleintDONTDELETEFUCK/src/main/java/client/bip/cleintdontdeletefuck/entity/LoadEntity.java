package client.bip.cleintdontdeletefuck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadEntity {
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

    public LoadEntity(Integer semesterFirstHours, Integer semesterSecondHours, Integer examHours, Integer courseworkHours,
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

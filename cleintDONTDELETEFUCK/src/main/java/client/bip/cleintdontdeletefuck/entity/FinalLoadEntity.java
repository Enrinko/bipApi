package client.bip.cleintdontdeletefuck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalLoadEntity {
    private String teacherInLoad;
    private List<String> subjectInLoad;
    private List<String> groupInLoad;
    private Integer examHours;
    private Integer diplomaHours;
    private Integer consultationHours;
    private Integer courseworkHours;
    private List<Integer> semesterFirstHours;
    private List<Integer> semesterSecondHours;
    private Integer total;

    public FinalLoadEntity(String teacherInLoad, String subject, String group, Integer exam, Integer diploma, Integer consultation,
                           Integer coursework, Integer semesterFirst, Integer semesterSecond) {
        this.teacherInLoad = teacherInLoad;
        this.subjectInLoad.add(subject);
        this.groupInLoad.add(group);
        this.examHours = exam;
        this.diplomaHours = diploma;
        this.consultationHours = consultation;
        this.courseworkHours = coursework;
        this.semesterFirstHours.add(semesterFirst);
        this.semesterSecondHours.add(semesterSecond);
        this.total = exam + consultation + coursework + semesterFirst + semesterSecond + diploma;
    }

    public void addAllWhatINeed(String subject, String group, Integer exam, Integer diploma, Integer consultations,
                           Integer coursework, Integer semesterFirst, Integer semesterSecond) {

        if (this.subjectInLoad.contains(subject)) {
            this.subjectInLoad.remove(subject);
        }
        if (this.groupInLoad.contains(group)) {
            this.groupInLoad.remove(group);
        }
        this.subjectInLoad.add(subject);
        this.groupInLoad.add(group);
        this.examHours += exam;
        this.diplomaHours += diploma;
        this.courseworkHours += consultations;
        this.courseworkHours += coursework;
        this.semesterFirstHours.add(semesterFirst);
        this.semesterSecondHours.add(semesterSecond);
        this.total += exam + diploma + consultations + coursework + semesterFirst + semesterSecond;
    }
    public Integer summOfSemesters (List<Integer> semester) {
        Integer summ = 0;
        for (Integer subject:
             semester) {
            summ += subject;
        }
        return summ;
    }
}

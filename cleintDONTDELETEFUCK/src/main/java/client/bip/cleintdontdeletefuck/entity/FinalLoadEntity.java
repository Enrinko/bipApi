package client.bip.cleintdontdeletefuck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalLoadEntity {
    private String teacherInLoad;
    private String subjectInLoad;
    private String groupInLoad;
    private Integer examHours;
    private Integer diplomaHours;
    private Integer consultationHours;
    private Integer courseworkHours;
    private Integer semesterFirstHours;
    private Integer semesterSecondHours;
    private Integer semesterFinal;
    private Integer total;

}

package client.bip.cleintdontdeletefuck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TeacherEntity {
    private Long id;
    private String initials;

    public TeacherEntity(Long id, String initials) {
        this.id = id;
        this.initials = initials;
    }

    public TeacherEntity(String initials) {
        this.initials = initials;
    }
}

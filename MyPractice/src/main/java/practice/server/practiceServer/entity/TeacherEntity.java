package practice.server.practiceServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "Teachers")
@AllArgsConstructor
@NoArgsConstructor
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String initials;
    @NotNull
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherInLoad")
    private List<LoadEntity> teacherInLoad;

    public TeacherEntity(Long id, String initials) {
        this.id = id;
        this.initials = initials;
    }

    public TeacherEntity(String initials) {
        this.initials = initials;
    }
}
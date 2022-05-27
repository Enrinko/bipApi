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
@Table(name = "Subject")
@AllArgsConstructor
@NoArgsConstructor
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String subjectName;
    @NotNull
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectInLoad")
    private List<LoadEntity> subjectInLoad;


    public SubjectEntity(Long id, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
    }

    public SubjectEntity(String subjectName) {
        this.subjectName = subjectName;
    }
}

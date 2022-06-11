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
@Table(name = "Groups")
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String groupName;
    @NotNull
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupInLoad")
    private List<LoadEntity> groupInLoad;

    public GroupEntity(String groupName) {
        this.groupName = groupName;
    }

    public GroupEntity(Long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }
}
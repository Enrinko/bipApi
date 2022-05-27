package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;
import practice.server.practiceServer.entity.TeacherEntity;

import java.util.List;

@Getter
@Setter
public class TeacherListResponse extends BaseResponse {
    private List<TeacherEntity> data;

    public TeacherListResponse(String message, boolean success, List<TeacherEntity> data) {
        super(message, success);
        this.data = data;
    }
}

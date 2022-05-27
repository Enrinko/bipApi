package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;
import practice.server.practiceServer.entity.TeacherEntity;

import java.util.List;

@Getter
@Setter
public class TeacherResponse extends BaseResponse {
    private TeacherEntity data;

    public TeacherResponse(String message, boolean success, TeacherEntity data) {
        super(message, success);
        this.data = data;
    }
}

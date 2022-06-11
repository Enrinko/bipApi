package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;
import practice.server.practiceServer.entity.SubjectEntity;

@Getter
@Setter
public class SubjectResponse extends BaseResponse {
    private SubjectEntity data;

    public SubjectResponse(String message, boolean success, SubjectEntity data) {
        super(message, success);
        this.data = data;
    }
}
package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;
import practice.server.practiceServer.entity.SubjectEntity;

import java.util.List;

@Getter
@Setter
public class SubjectListResponse extends BaseResponse {
    private List<SubjectEntity> data;

    public SubjectListResponse(String message, boolean success, List<SubjectEntity> data) {
        super(message, success);
        this.data = data;
    }
}

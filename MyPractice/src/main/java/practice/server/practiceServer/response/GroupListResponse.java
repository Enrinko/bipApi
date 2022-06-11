package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;
import practice.server.practiceServer.entity.GroupEntity;

import java.util.List;

@Getter
@Setter
public class GroupListResponse extends BaseResponse {
    private List<GroupEntity> data;

    public GroupListResponse(String message, boolean success, List<GroupEntity> data) {
        super(message, success);
        this.data = data;
    }
}
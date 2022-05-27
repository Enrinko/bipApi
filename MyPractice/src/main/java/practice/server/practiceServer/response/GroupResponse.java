package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;
import practice.server.practiceServer.entity.GroupEntity;

@Getter
@Setter
public class GroupResponse extends BaseResponse {
    private GroupEntity data;

    public GroupResponse(String message, boolean success, GroupEntity data) {
        super(message, success);
        this.data = data;
    }
}

package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;
import practice.server.practiceServer.entity.LoadEntity;
import practice.server.practiceServer.model.LoadModel;

import java.util.List;

@Getter
@Setter
public class LoadListResponse extends BaseResponse {
    private List<LoadModel> data;

    public LoadListResponse(String message, boolean success, List<LoadModel> data) {
        super(message, success);
        this.data = data;
    }
}

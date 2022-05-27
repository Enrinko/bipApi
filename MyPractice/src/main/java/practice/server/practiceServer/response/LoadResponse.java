package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;
import practice.server.practiceServer.entity.LoadEntity;
import practice.server.practiceServer.model.LoadModel;

import java.util.List;

@Getter
@Setter
public class LoadResponse extends BaseResponse {
    private LoadModel data;

    public LoadResponse(String message, boolean success, LoadModel data) {
        super(message, success);
        this.data = data;
    }
}

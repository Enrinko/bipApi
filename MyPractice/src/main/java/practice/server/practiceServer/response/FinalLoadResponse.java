package practice.server.practiceServer.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FinalLoadResponse extends BaseResponse{
    private List<Integer> data;

    public FinalLoadResponse(String message, boolean success, List<Integer> data) {
        super(message, success);
        this.data = data;
    }
}

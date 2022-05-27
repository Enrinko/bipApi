package practice.server.practiceServer.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse {
    protected String message;
    protected boolean success;
}

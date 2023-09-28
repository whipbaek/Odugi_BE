package devcamp.ottogi.userservice.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {
    private Boolean success;
    private int code;
    private String message;
    private T data;
}

package devcamp.ottogi.userservice.response;

import devcamp.ottogi.userservice.domain.SuccessMessages;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class CommonResponse<T> {
    private Boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> CommonResponse<T> successResponse(SuccessMessages msg, T data){
        return CommonResponse.<T>builder()
                .success(true)
                .code(Integer.toString(HttpStatus.OK.value()))
                .message(msg.getMessage())
                .data(data)
                .build();
    }
}

package devcamp.ottogi.userservice.response;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode error;
    public ApiException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }
}

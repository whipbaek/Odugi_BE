package devcamp.ottogi.userservice.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public <T> ResponseEntity<CommonResponse<T>> exceptionHandler(final ApiException e) {
        return ResponseEntity
                .status(e.getError().getStatus())
                .body(CommonResponse.<T>builder()
                        .code(e.getError().getCode())
                        .success(false)
                        .message(e.getMessage())
                        .build());
    }
}

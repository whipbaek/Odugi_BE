package devcamp.ottogi.userservice.exception;

import devcamp.ottogi.userservice.response.CommonResponse;
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
                        .success(false)
                        .message(e.getMessage())
                        .build());
    }
}

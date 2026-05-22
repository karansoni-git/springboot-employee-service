package in.kp.main.advice;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Data
public class ApiErrorResponse {
    private LocalDateTime stamp;
    private HttpStatus status;
    private String error;

    public ApiErrorResponse() {
        this.stamp = LocalDateTime.now();
    }

    public ApiErrorResponse(HttpStatus status, String error) {
        this();
        this.status = status;
        this.error = error;
    }

}

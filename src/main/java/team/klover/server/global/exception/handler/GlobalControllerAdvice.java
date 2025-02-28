package team.klover.server.global.exception.handler;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.exception.KloverException;

@RestControllerAdvice
@Hidden
public class GlobalControllerAdvice {
    @ExceptionHandler(KloverException.class)
    public ApiResponse<?> handleKloverException(KloverException e) {
        return ApiResponse.of(e.getReturnCode());
    }

}

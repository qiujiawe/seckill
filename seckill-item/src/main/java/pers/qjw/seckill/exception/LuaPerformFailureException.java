package pers.qjw.seckill.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class LuaPerformFailureException extends RuntimeException{
    private HttpStatus status;
    private String message;
    public LuaPerformFailureException(String message, HttpStatus code) {
        this.message = message;
        this.status = code;
    }
}

package pers.qjw.seckill.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ConditionNotMetException extends RuntimeException{
    private HttpStatus status;
    private String message;
    public ConditionNotMetException(String message, HttpStatus code) {
        this.message = message;
        this.status = code;
    }
}

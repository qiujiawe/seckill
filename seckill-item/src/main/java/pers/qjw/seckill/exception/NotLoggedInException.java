package pers.qjw.seckill.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotLoggedInException extends RuntimeException {

    private String code;
    private String message;

    public NotLoggedInException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

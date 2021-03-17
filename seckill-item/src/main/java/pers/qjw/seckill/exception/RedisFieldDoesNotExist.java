package pers.qjw.seckill.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RedisFieldDoesNotExist extends RuntimeException{
    private String code;
    private String message;

    public RedisFieldDoesNotExist(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

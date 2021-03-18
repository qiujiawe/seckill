package pers.qjw.seckill.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnableWriteToDatabaseException extends RuntimeException{
    private String code;
    private String message;
    public UnableWriteToDatabaseException(String code,String message){
        this.message = message;
        this.code = code;
    }
}

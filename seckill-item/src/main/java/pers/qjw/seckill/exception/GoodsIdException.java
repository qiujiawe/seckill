package pers.qjw.seckill.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsIdException extends RuntimeException{
    private String code;
    private String message;
    public GoodsIdException(String code,String message){
        this.message = message;
        this.code = code;
    }
}

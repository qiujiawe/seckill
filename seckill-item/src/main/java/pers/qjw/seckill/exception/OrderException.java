package pers.qjw.seckill.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderException extends RuntimeException{
    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public OrderException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}

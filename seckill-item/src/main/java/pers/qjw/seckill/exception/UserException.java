package pers.qjw.seckill.exception;

public class UserException extends RuntimeException {

    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public UserException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}

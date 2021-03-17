package pers.qjw.seckill.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pers.qjw.seckill.domain.ResultBody;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = NotLoggedInException.class)
    public ResultBody NotLoggedInException(NotLoggedInException e) {
        return ResultBody.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = UnableWriteToDatabaseException.class)
    public ResultBody UnableWriteToDatabaseException(UnableWriteToDatabaseException e) {
        logger.error("写入数据库失败！具体位置是:", e);
        return ResultBody.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResultBody HttpRequestMethodNotSupportedException() {
        return ResultBody.error("-1", "资源不支持当前请求方式");
    }

    @ExceptionHandler(value = Exception.class)
    public ResultBody exceptionHandler(Exception e) {
        logger.error("未知异常！原因是:", e);
        return ResultBody.error("500", "服务器出现未知异常");
    }
}

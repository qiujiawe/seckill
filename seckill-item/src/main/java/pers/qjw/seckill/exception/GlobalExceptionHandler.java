package pers.qjw.seckill.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pers.qjw.seckill.domain.ResultVO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = NotLoggedInException.class)
    public ResultVO NotLoggedInException(NotLoggedInException e) {
        return ResultVO.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResultVO HttpRequestMethodNotSupportedException() {
        return ResultVO.error("-1", "资源不支持当前请求方式");
    }

    @ExceptionHandler(value = Exception.class)
    public ResultVO exceptionHandler(Exception e) {
        logger.error("未知异常！原因是:", e);
        return ResultVO.error("500", "服务器出现未知异常");
    }
}

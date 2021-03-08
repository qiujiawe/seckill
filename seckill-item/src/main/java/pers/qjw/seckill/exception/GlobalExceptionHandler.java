package pers.qjw.seckill.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pers.qjw.seckill.domain.ResultBody;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = TokenException.class)
    public ResultBody TokenExceptionHandler(TokenException e) {
        if (Objects.equals(e.getErrorMsg(), "还未登录")) {
            return ResultBody.error(e.getErrorCode(), e.getErrorMsg());
        }
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = UserException.class)
    public ResultBody UserExceptionHandler(UserException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = OrderException.class)
    public ResultBody OrderExceptionHandler(OrderException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = GoodsException.class)
    public ResultBody GoodsExceptionHandler(GoodsException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResultBody HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("请求方式错误", e);
        return ResultBody.error("400", "不能对资源进行此操作(不支持此请求方式)");
    }

    @ExceptionHandler(value = Exception.class)
    public ResultBody exceptionHandler(Exception e) {
        logger.error("未知异常！原因是:", e);
        return ResultBody.error(CommonExceptionEnum.INTERNAL_SERVER_ERROR);
    }
}

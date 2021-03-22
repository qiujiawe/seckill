package pers.qjw.seckill.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler类 异常管理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 日志工具，记录程序抛出的异常
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = LuaPerformFailureException.class)
    public ResponseEntity<String> LuaPerformFailureException(LuaPerformFailureException e) {
        logger.error("lua脚本执行异常！原因是:", e);
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(value = UnableWriteToDatabaseException.class)
    public ResponseEntity<String> UnableWriteToDatabaseException(UnableWriteToDatabaseException e) {
        logger.error("数据无法写入数据库！原因是:", e);
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(value = ConditionNotMetException.class)
    public ResponseEntity<String> ConditionNotMetException(ConditionNotMetException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    /**
     * InputDataErrorException方法用来统一处理程序抛出的InputDataErrorException异常
     * InputDataErrorException表示用户输入的数据有问题
     * @param e 异常对象
     * @return 400 和 异常描述
     */
    @ExceptionHandler(value = ClientDataErrorException.class)
    public ResponseEntity<String> ClientDataErrorException(ClientDataErrorException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    /**
     * NotLoggedInException方法用来统一处理程序抛出的NotLoggedInException异常
     * NotLoggedInException表示用户还没登录就访问需要登录才能访问的资源
     * @param e 异常对象
     * @return 401 和 异常描述
     */
    @ExceptionHandler(value = NotLoggedInException.class)
    public ResponseEntity<String> NotLoggedInException(NotLoggedInException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    /**
     * HttpRequestMethodNotSupportedException方法用来统一处理程序抛出的HttpRequestMethodNotSupportedException异常
     * HttpRequestMethodNotSupportedException表示前端的请求方式错误
     * @return 400 和 异常描述
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> HttpRequestMethodNotSupportedException() {
        return new ResponseEntity<>("资源不支持当前请求方式", HttpStatus.BAD_REQUEST);
    }

    /**
     * exceptionHandler方法用来统一处理程序抛出的未知异常
     * 执行了这个方法说明程序出现了意料之外的异常
     * @return 500 和 异常描述
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e) {
        logger.error("未知异常！原因是:", e);
        return new ResponseEntity<>("服务器出现未知异常", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

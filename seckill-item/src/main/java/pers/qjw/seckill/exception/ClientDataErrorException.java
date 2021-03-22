package pers.qjw.seckill.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ClientDataErrorException extends RuntimeException{
    private HttpStatus status;
    private String message;
    public ClientDataErrorException(String message, HttpStatus code) {
        this.message = message;
        this.status = code;
    }
}

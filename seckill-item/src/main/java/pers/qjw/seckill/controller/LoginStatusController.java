package pers.qjw.seckill.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.domain.User;

import java.util.Objects;

@RestController
@RequestMapping("/api/login-status")
public class LoginStatusController {
    @GetMapping
    public ResponseEntity<Boolean> getLoginStatus(@CurrentUser User user){
        return new ResponseEntity<>(!Objects.isNull(user), HttpStatus.OK);
    }
}

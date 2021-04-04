package pers.qjw.seckill.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.exception.ClientDataErrorException;
import pers.qjw.seckill.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(String phone, String password) {
        boolean flag = userService.checkPhoneAndPassword(phone, password);
        if (flag) {
            // 可以注册
            userService.createUser(phone,password);
            return new ResponseEntity<>("注册成功",HttpStatus.OK);
        } else {
            throw new ClientDataErrorException("该电话号码已注册过账号", HttpStatus.BAD_REQUEST);
        }
    }
}

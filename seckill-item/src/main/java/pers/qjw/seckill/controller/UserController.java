package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.domain.ResultVO;
import pers.qjw.seckill.service.UserService;
import pers.qjw.seckill.util.CheckUtil;

@RestController
@RequestMapping("/api/users")
@Api(tags = "users API")
public class UserController {

    private CheckUtil checkUtil;
    private UserService userService;

    public UserController() {
    }

    @Autowired
    public UserController(CheckUtil checkUtil, UserService userService) {
        this.checkUtil = checkUtil;
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation("创建用户，注册")
    public ResultVO createUser(String phone, String password) {
        // 校验电话号码和密码是否存在 及 长度是否正常
        ResultDTO checkResult = checkUtil.checkPhoneAndPassword(phone, password);
        // 处理结果
        if (!checkResult.isThrough()) {
            // 没有通过校验
            return ResultVO.error(checkResult.getCode(), checkResult.getMessage());
        }
        ResultDTO userServiceCheckResult = userService.checkPhone(phone);
        if (!userServiceCheckResult.isThrough()) {
            // 没有通过校验
            return ResultVO.error(userServiceCheckResult.getCode(),userServiceCheckResult.getMessage());
        }
        userService.createUser(phone, password);
        return ResultVO.success("创建成功");
    }

}

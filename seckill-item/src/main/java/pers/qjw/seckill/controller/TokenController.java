package pers.qjw.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.qjw.seckill.annotations.Authorization;
import pers.qjw.seckill.annotations.CurrentUser;
import pers.qjw.seckill.domain.ResultVO;
import pers.qjw.seckill.domain.User;
import pers.qjw.seckill.domain.ResultDTO;
import pers.qjw.seckill.service.TokenService;
import pers.qjw.seckill.util.CheckUtil;

@RestController
@RequestMapping("/api/tokens")
@Api(tags = "tokens API")
public class TokenController {

    private TokenService tokenService;

    private CheckUtil checkUtil;

    public TokenController() {
    }

    @Autowired
    public TokenController(TokenService tokenService, CheckUtil checkUtil) {
        this.tokenService = tokenService;
        this.checkUtil = checkUtil;
    }

    @PostMapping
    @ApiOperation("创建token,等同于登录功能")
    public ResultVO createToken(User user) {
        // 校验电话号码和密码是否存在 及 长度是否正常
        ResultDTO checkResult = checkUtil.checkPhoneAndPassword(user.getPhone(), user.getPassword());
        // 处理结果
        if (!checkResult.isThrough()) {
            // 没有通过校验
            return ResultVO.error(checkResult.getCode() ,checkResult.getMessage());
        }
        // 校验电话号码和密码是否能创建token
        ResultDTO tokenServiceCheckResult = tokenService.checkPhoneAndPassword(user.getPhone(),user.getPassword());
        // 处理结果
        if (!tokenServiceCheckResult.isThrough()) {
            // 没有通过校验
            return ResultVO.error(tokenServiceCheckResult.getCode(),tokenServiceCheckResult.getMessage());
        }
        // 创建token
        String text = tokenService.createToken(user.getPhone());
        return ResultVO.success(text);
    }

    @DeleteMapping
    @Authorization
    @ApiOperation("删除token，等同于退出登录功能")
    public ResultVO deleteToken(@CurrentUser User user) {
        tokenService.deleteToken(user.getPhone());
        return ResultVO.success("注销成功");
    }

}

package pers.qjw.seckill.util;

import org.springframework.stereotype.Component;
import pers.qjw.seckill.domain.ResultDTO;


import java.util.Objects;

@Component
public class CheckUtil {
    public ResultDTO checkPhoneAndPassword(String phone, String password){
        boolean phoneFlag = Objects.isNull(phone);
        boolean PasswordFlag = Objects.isNull(password);
        if (phoneFlag || PasswordFlag) {
            return ResultDTO.error("手机号码或密码缺失");
        }
        int phoneLength = phone.length();
        int passwordLength = password.length();
        if (phoneLength != 11 || passwordLength < 6 || passwordLength > 18) {
            return ResultDTO.error("手机号码或密码长度违规");
        }
        return ResultDTO.success();
    }
}

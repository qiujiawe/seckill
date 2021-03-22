package pers.qjw.seckill.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CurrentUser注解可以往方法参数中注入当前登录用户的用户信息，具体有:phone,password,userId
 * 如果用户还未登录则会注入null
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}

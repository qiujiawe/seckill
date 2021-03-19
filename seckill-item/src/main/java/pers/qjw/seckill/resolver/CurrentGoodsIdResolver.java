package pers.qjw.seckill.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pers.qjw.seckill.annotations.CurrentGoodsId;
import pers.qjw.seckill.config.Constants;
import pers.qjw.seckill.exception.GoodsIdException;

public class CurrentGoodsIdResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(Integer.class) &&
                methodParameter.hasParameterAnnotation(CurrentGoodsId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        String goodsId = (String) nativeWebRequest.getAttribute(Constants.GOODS_ID, RequestAttributes.SCOPE_SESSION);
        nativeWebRequest.removeAttribute(Constants.GOODS_ID, RequestAttributes.SCOPE_SESSION);
        int id;
        try {
            assert goodsId != null;
            id = Integer.parseInt(goodsId);
        } catch (Exception e) {
            throw new GoodsIdException("-1", "商品编号异常");
        }
        return id;
    }
}

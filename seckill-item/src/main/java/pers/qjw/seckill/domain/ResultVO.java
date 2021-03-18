package pers.qjw.seckill.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVO implements Serializable {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object result;

    public ResultVO() {
    }

    /**
     * 成功
     */
    public static ResultVO success(Object data) {
        ResultVO rb = new ResultVO();
        rb.setCode("200");
        rb.setMessage("请求成功");
        rb.setResult(data);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultVO error(String code, String message) {
        ResultVO rb = new ResultVO();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}

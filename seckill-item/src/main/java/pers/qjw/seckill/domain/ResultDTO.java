package pers.qjw.seckill.domain;

import lombok.Data;

@Data
public class ResultDTO {
    private boolean success;
    private String code;
    private String message;
    private Object data;

    public static ResultDTO error(String message){
        ResultDTO result = new ResultDTO();
        result.setCode("-1");
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    public static ResultDTO success(){
        ResultDTO result = new ResultDTO();
        result.setCode("-1");
        result.setMessage(null);
        result.setSuccess(true);
        return result;
    }

    public static ResultDTO success(Object data){
        ResultDTO result = new ResultDTO();
        result.setCode("-1");
        result.setMessage(null);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

}

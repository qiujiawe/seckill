package pers.qjw.seckill.config;

public class Constant {

    /**
     * request作用域中存放当前用户id字段的key
     */
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";

    /**
     * 客户端cookie中存放token加密字段的key
     */
    public static final String TOKEN = "TOKEN";

    /**
     * redis中存放所有商品信息字段的key
     */
    public static final String HOT_COMMODITY = "HOT_COMMODITY";

    /**
     * redis中存放商品信息字段的key
     */
    public static final String LIST_GOODS = "LIST_GOODS";

    /**
     * redis中存放商品库存信息字段的key的一部分
     */
    public static final String INVENTORY = "INVENTORY_";

    /**
     * redis中存放订单信息字段的key的一部分
     */
    public static final String ORDER_ID = "ORDER_ID_";

    /**
     * redis中存放令牌信息字段的key的一部分
     */
    public static final String USER_ID = "USER_ID_";

    /**
     * 在 tokenController 中表示不是用户
     */
    public static final int NOT_USER = -1;

    /**
     * 表示cookie中token字段的失效时间 单位是秒 (30天)
     */
    public static final int TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 30;

}

package pers.qjw.seckill.config;

/**
 * Constants类 用来存放各种字段的KEY
 */
public class Constants {

    /**
     * CURRENT_USER_PHONE 属性是request作用域中储存用户phone字段的key
     */
    public static final String CURRENT_USER_PHONE = "CURRENT_USER_PHONE";

    /**
     * AUTHORIZATION 属性是前端设置在headers中token字段的key
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * INVENTORY 属性是redis中储存 商品库存 字段的key的一部分(完整key为: INVENTORY + 商品ID)
     */
    public static final String INVENTORY = "INVENTORY_";

    /**
     * ORDER_SET 属性是redis中储存 订单记录 字段的key，这些记录每隔一段时间会被CacheWriteToDatabase类写入mysql并删除
     */
    public static final String ORDER_SET = "ORDER_SET";
}

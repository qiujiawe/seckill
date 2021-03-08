-- 商品表
CREATE TABLE `seckill_goods` (
                                 `id` int NOT NULL,
                                 `name` varchar(255) DEFAULT NULL,
                                 `number` varchar(255) DEFAULT NULL,
                                 `start_time` datetime DEFAULT NULL,
                                 `end_time` datetime DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `price` decimal(10,0) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 订单表
CREATE TABLE `seckill_order` (
                                 `id` int NOT NULL,
                                 `seckill_goods_id` int DEFAULT NULL,
                                 `user_id` int DEFAULT NULL,
                                 `state` tinyint NOT NULL DEFAULT '-1',
                                 `create_time` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 订单表
CREATE TABLE `user` (
                        `id` int NOT NULL,
                        `phone` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


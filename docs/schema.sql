CREATE DATABASE IF NOT EXISTS userdemo DEFAULT CHARSET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE userdemo;

-- ----------------------------
-- 2. 用户表（user）
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表(存储用户基础信息)';

-- ----------------------------
-- 3. 商品表（product）
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表(存储商品基础信息)';

-- ----------------------------
-- 4. 订单表（order）
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_no` varchar(30) NOT NULL COMMENT '订单编号',
  `user_id` int NOT NULL COMMENT '关联的用户ID(外键)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `fk_order_user` (`user_id`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表(存储订单基础信息)';

-- ----------------------------
-- 5. 订单商品中间表（order_product）
-- ----------------------------
DROP TABLE IF EXISTS `order_product`;
CREATE TABLE `order_product` (
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
    -- 复合主键索引的 “最左匹配” 已覆盖order_id的所有查询，重复建索引无意义
  KEY `fk_order_product_product` (`product_id`),
    -- 创建「索引」的简写（等价于 INDEX）
    -- fk_order_product_product：是索引的自定义名称（命名规则：fk_表名_关联表名_字段名，便于识别）
    -- (product_id)：表示索引作用在 order_product 表的 product_id 字段上
  CONSTRAINT `fk_order_product_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`),
  CONSTRAINT `fk_order_product_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单商品中间表(多对多关联)';
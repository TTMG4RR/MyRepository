CREATE DATABASE IF NOT EXISTS userdemo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE userdemo;

-- 删除表的顺序：先删除有外键依赖的表，再删除被依赖的表
DROP TABLE IF EXISTS order_product;  -- 订单商品中间表（多对多关系）
DROP TABLE IF EXISTS `order`;        -- 订单表
DROP TABLE IF EXISTS product;        -- 商品表
DROP TABLE IF EXISTS category;       -- 分类表
DROP TABLE IF EXISTS user;           -- 用户表

CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_phone (phone),
    KEY idx_user_create_time (create_time)
) COMMENT '用户表';

CREATE TABLE category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    sort INT DEFAULT 0,
    parent_id INT DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_category_name (name),
    KEY idx_category_parent (parent_id)
) COMMENT '商品分类表';

CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    category_id INT,
    status TINYINT DEFAULT 1,
    stock INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_product_name (name),
    KEY idx_product_category (category_id),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category (id)
) COMMENT '商品表';

CREATE TABLE `order` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL,
    user_id INT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_order_user (user_id),
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES user (id)
) COMMENT '订单主表（与用户表一对多关系：一个用户可以有多个订单）';

/**
 * 订单商品中间表（实现订单与商品的多对多关系）
 * 关系说明：
 * - 一个订单可以包含多个商品（一个订单ID对应多个商品ID）
 * - 一个商品可以在多个订单中（一个商品ID对应多个订单ID）
 * 
 * 设计要点：
 * 1. 使用联合主键 (order_id, product_id) 确保同一订单不会重复添加同一商品
 * 2. 为 order_id 和 product_id 分别建立索引，优化查询性能
 * 3. 建立外键约束，确保数据完整性
 */
CREATE TABLE order_product (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id, product_id),  -- 联合主键，防止重复
    KEY idx_order_product_order (order_id),      -- 优化"查询订单的商品"查询
    KEY idx_order_product_product (product_id),  -- 优化"查询商品的订单"查询
    CONSTRAINT fk_order_product_order FOREIGN KEY (order_id) REFERENCES `order` (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_product_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
) COMMENT '订单商品中间表（订单与商品多对多关系）';



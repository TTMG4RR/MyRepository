CREATE DATABASE IF NOT EXISTS userdemo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE userdemo;

DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS user;

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
) COMMENT '订单主表';

CREATE TABLE favorite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_product (user_id, product_id),
    KEY idx_favorite_user (user_id),
    KEY idx_favorite_product (product_id),
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_favorite_product FOREIGN KEY (product_id) REFERENCES product (id)
) COMMENT '用户收藏表（多对多）';


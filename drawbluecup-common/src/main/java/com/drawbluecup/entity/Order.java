package com.drawbluecup.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 * 关系说明：
 * - 与 User 表：多对一关系（一个订单属于一个用户，一个用户可以有多个订单）
 * - 与 Product 表：多对多关系（一个订单可以包含多个商品，一个商品可以在多个订单中）
 *   通过中间表 order_product 实现多对多关系
 */
@Data
public class Order {
    //成员变量
    private Integer id;//主键自增
    private String orderNo;//订单编号.业务展示
    private LocalDateTime createTime;

    private Integer userId;//外键,与主表主键关联
    private User user;
    /**
     * 订单关联的商品列表（多对多关系）
     * 通过中间表 order_product 关联，一个订单可以包含多个商品
     * 使用 List 是因为一个订单可以有多个商品（一对多）
     * 注意：这个字段需要通过 JOIN 查询才能填充，单表查询时为空
     */
    private List<Product> products;


    //构造方法
    public Order(Integer id, String orderNo, Integer userId, User user, LocalDateTime createTime) {
        this.id = id;
        this.orderNo = orderNo;
        this.userId = userId;
        this.user = user;
        this.createTime = createTime;
    }
    public Order() {

    }
}



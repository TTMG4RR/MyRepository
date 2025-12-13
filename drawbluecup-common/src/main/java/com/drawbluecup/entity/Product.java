package com.drawbluecup.entity;

import lombok.Data;

import java.util.List;

@Data
public class Product {
    //成员变量
    private Integer id;
    private String name;
    private List<Order> orders ;

    //构造方法
    public Product() {
    }

    public Product(Integer id, String name, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.orders = orders;
    }

}

package com.drawbluecup.entity;

import lombok.Data;

@Data
public class Product {
    //成员变量
    private Integer id;
    private String name;

    //构造方法
    public Product() {
    }

    public Product(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}

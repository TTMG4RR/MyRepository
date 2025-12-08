package com.drawbluecup.entity;

/*
先是配置好数据库,创建表和字段,
然后创建实体类用于封装数据方便以后传输,
mapper和xml一起来组成操控数据的方法,
服务层在方法基础上添加逻辑,
控制层就是接受前端的请求来响应对应的服务层并对返回值做处理
 */

import com.drawbluecup.validation.Phone;

import java.time.LocalDateTime;
import java.util.List;

//实体层封装数据,用于存储,取出及使用
//而Result层用于封装返回数据

public class User {

    private Integer id ;
    private String name;
    @Phone
    private String phone;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // 核心：一个用户有多个订单，用List<Order>存储
    private List<Order> orders;
    // 多对多：收藏的商品列表
    private List<Product> favoriteProducts;

    //构造方法
    public User() {
    }

    public User(Integer id, String name, String phone) {

         this.id = id;
         this.name = name;
         this.phone = phone;
       //待传入数据,时间使用now来填充,不需要传
    }

    public Integer getId() {
         return id;
    }
    public void setId(int id) {
         this.id = id;
    }


    public String getName() {
         return name;
    }
    public void setName(String name) {
         this.name = name;
    }


    public String getPhone() {
         return phone;
    }
    public void setPhone(String phone) {
         this.phone = phone;
    }


    //但是这里createTime 和 updateTime 这两个时间字段必须添加 getter/setter 方法,因为:
    //框架默认从getter和setter读取字段
    public LocalDateTime getCreateTime() {
         return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
         this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
         return updateTime;
    }
    public void setUpdateTime(LocalDateTime updateTime) {
         this.updateTime = updateTime;
    }

    public List<Order> getOrders() {
         return orders;
    }
    public void setOrders(List<Order> orders) {
         this.orders = orders;
    }

    public List<Product> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(List<Product> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +  // 包含创建时间
                ", updateTime=" + updateTime +  // 包含更新时间
                ", orders=" + orders +  // 可选：如果需要显示订单
                '}';
    }

}


/* MyBatis 读取数据库数据时：需要通过 setCreateTime(...) 方法把数据库的 create_time 字段值赋给实体类的 createTime 变量。
MyBatis 向数据库写入数据时：需要通过 getCreateTime() 方法获取实体类的 createTime 值，再拼接成 SQL 语句。
JSON 序列化时（如接口返回 Result 包含 User 对象）：Jackson 框架需要通过 getCreateTime() 方法获取时间值，才能转换成 JSON 中的 createTime 字段。*/
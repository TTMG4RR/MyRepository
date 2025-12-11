package com.drawbluecup.entity;

/*
先是配置好数据库,创建表和字段,
然后创建实体类用于封装数据方便以后传输,
mapper和xml一起来组成操控数据的方法,
服务层在方法基础上添加逻辑,
控制层就是接受前端的请求来响应对应的服务层并对返回值做处理
 */

import com.drawbluecup.validation.Phone;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

//实体层封装数据,用于存储,取出及使用
//而Result层用于封装返回数据
@Data
public class User {

    private Integer id ;
    private String name;
    @Phone
    private String phone;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // 核心：一个用户有多个订单，用List<Order>存储
    private List<Order> orders;

    //构造方法
    public User() {
    }

    public User(Integer id, String name, String phone,String password) {

         this.id = id;
         this.name = name;
         this.phone = phone;
       //待传入数据,时间使用now来填充,不需要传(在mapper.xml)
    }

    //getter和setter由lombok简化

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
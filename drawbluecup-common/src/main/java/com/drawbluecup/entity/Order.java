package com.drawbluecup.entity;

import java.time.LocalDateTime;

public class Order {
    private Integer id;//主键自增
    private String orderNo;//订单编号.业务展示
    private Integer userId;//外键,与主表主键关联

    //关联的用户对象（一对多中“多"的一方持有“一"的引用)
    // 可选：直接关联用户对象（方便通过订单获取用户信息，如订单属于哪个用户）
    private User user;//WIP
    private LocalDateTime createTime;//WIP



    public Order(Integer id, String orderNo, Integer userId, User user, LocalDateTime createTime) {
        this.id = id;
        this.orderNo = orderNo;
        this.userId = userId;
        this.user = user;
        this.createTime = createTime;
    }
    public Order() {

    }



    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


}



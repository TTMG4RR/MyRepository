package com.drawbluecup.service;

import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.User;

import java.util.List;

public interface OrderService {

    List<Order> findOrderAll();             //查询所有订单
    User findUserWithOrders(Integer userId);//查询用户以及它所有的订单
    void addOrder(Order order);             //添加订单
    void deleteOrderAll();                  //删除所有订单

}

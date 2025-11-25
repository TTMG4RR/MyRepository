package com.drawbluecup.mapper;

import com.drawbluecup.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OrderMapper {

    //查
    List<Order> findByUserId(Integer userId);   //查当前用户的所有订单(有外键约束)
    Order findByOrderId(Integer id);                 //根据订单id查询
    Order findByOrderNo(String orderNo);        //根据订单编号查询
    List<Order> findOrderAll();                 //查询所有订单

    //增
    void addOrder(Order order); //添加订单

    //删
    void deleteOrderAll();      //删除所有订单



}

package com.drawbluecup.service;

import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.User;

import java.util.List;

/**
 * 订单服务接口
 * 定义订单相关的业务方法
 */
public interface OrderService {

    List<Order> findOrderAll();             //查询所有订单
    User findUserWithOrders(Integer userId);//查询用户以及它所有的订单
    void addOrder(Order order);             //添加订单
    void deleteOrderAll();                  //删除所有订单

    /**
     * 查询订单及其关联的商品列表（多对多关系）
     * 使用 JOIN 查询，一次性获取订单和商品信息
     * @param orderId 订单ID
     * @return 包含商品列表的订单对象
     */
    Order findOrderWithProducts(Integer orderId);

    /**
     * 为订单添加商品（建立订单与商品的多对多关联）
     * @param orderId 订单ID
     * @param productId 商品ID
     */
    void addProductToOrder(Integer orderId, Integer productId);

    /**
     * 从订单中移除商品（删除订单与商品的关联）
     * @param orderId 订单ID
     * @param productId 商品ID
     */
    void removeProductFromOrder(Integer orderId, Integer productId);
}

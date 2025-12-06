package com.drawbluecup.mapper;

import com.drawbluecup.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单 Mapper 接口
 * 负责订单相关的数据库操作
 */
@Mapper
public interface OrderMapper {

    //查
    List<Order> findByUserId(Integer userId);   //查当前用户的所有订单(有外键约束)
    Order findByOrderId(Integer id);                 //根据订单id查询
    Order findByOrderNo(String orderNo);        //根据订单编号查询
    List<Order> findOrderAll();                 //查询所有订单

    /**
     * 查询订单及其关联的商品列表（多对多关系）
     * 使用 JOIN 查询，一次性获取订单和商品信息
     * @param orderId 订单ID
     * @return 包含商品列表的订单对象
     */
    Order findOrderWithProducts(@Param("orderId") Integer orderId);

    //增
    void addOrder(Order order); //添加订单

    /**
     * 为订单添加商品（向中间表 order_product 插入记录）
     * 实现订单与商品的多对多关联
     * @param orderId 订单ID
     * @param productId 商品ID
     */
    void addProductToOrder(@Param("orderId") Integer orderId, @Param("productId") Integer productId);

    /**
     * 从订单中移除商品（从中间表 order_product 删除记录）
     * @param orderId 订单ID
     * @param productId 商品ID
     */
    void removeProductFromOrder(@Param("orderId") Integer orderId, @Param("productId") Integer productId);

    //删
    void deleteOrderAll();      //删除所有订单
}

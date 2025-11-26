package com.drawbluecup.service.impl;

import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.User;
import com.drawbluecup.exception.BusinessException;
import com.drawbluecup.mapper.OrderMapper;
import com.drawbluecup.mapper.UserMapper;
import com.drawbluecup.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
//注意点1:"implements OrderService"不要漏,是契约/承诺
//注意点2:因为是承诺,所以接口和实现类要一一对应,不要少
public class OrderServiceImpl implements OrderService {
    // 注入 UserMapper（MyBatis 自动生成的代理对象），通过它操作数据库
    // @Autowired 会让 Spring 自动把 UserMapper 的实例赋值给这个变量，不用手动 new
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;


    /*

     * 1.1查询所有订单
     * 业务逻辑：直接查询，无额外校验
     */
    @Override
    public List<Order> findOrderAll() {

        //调用 Mapper 的 findOrderAll() 方法，获取数据库中所有用户
        List<Order> orderList = orderMapper.findOrderAll();
        // 如果查询结果为空，返回空列表（避免 null，方便前端处理）
        return orderList != null ? orderList : List.of();

    }

    /*2.2查询用户以及所对应的订单
     *
     */
    @Override
    public User findUserWithOrders(Integer userId) {
        //要求查询用户以及用户对应的订单
        // 1. 先查用户基本信息（单表查询）
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(404,"用户不存在");
        }

        //2. 再查该用户的所有订单（多表关联的核心：通过用户ID查订单表）
        List<Order> orders = orderMapper.findByUserId(userId);

        // 3. 将订单列表关联到用户对象中（组装数据）,将orders装进user里面一起返回,封装
        user.setOrders(orders);//组装用setOrder,这是预设好的

        // 4. 返回包含订单的用户对象
        return user;

    }


    /*
     * 3.1新增订单    //检查参数,进一步检查参数   // (和下面修改用户对比学习)
     *在执行mapper命令前,先进行逻辑筛选
     * 注意订单号的唯一性
     * 业务逻辑：先检查前端输入参数orderNo非空(否则抛异常),进一步检查orderNo是否存在(原本就不存在新增订单,所以不用查询旧订单,跟不用排除自己和自己重复orderNo),新增订单
     * 校验参数合法性 + 检查订单编号是否已存在（避免重复）
     */
    @Override
    public void addOrder(Order order) {
        //1.检验参数不为空
        if (order == null) {
            throw new BusinessException(400,"新增失败：订单信息不能为空");
        }
        if (order.getOrderNo() == null|| order.getOrderNo().trim().isEmpty()) {
            //字符串类型，需要 trim()
            throw new BusinessException(400,"新增失败：订单编号不能为空");
        }

        if (order.getUserId() == null|| order.getUserId()<=0) {
            //Integer 是数字类型，没有 trim() 方法
            throw new BusinessException(400,"新增失败：id数值并不合理");
        }//这是userId数值不合理的情况

        //2.检验订单号是否重复//假设推断,如果订单号存在,就可以找到相应的订单,就是被占用;如果不存在,就找不到,就是null,就是没有被占用
        Order existOrder = orderMapper.findByOrderNo(order.getOrderNo());
        if (existOrder != null) {
            throw new BusinessException(400,"新增失败：订单编号 " + order.getOrderNo() + " 已被占用");
        }
        //检验外键绑定用户,该用户是否存在
        User existUser = userMapper.findById(order.getUserId());
        if (existUser == null) {
            throw new BusinessException(404,"新增失败：id为 " + order.getUserId() + " 的用户不存在");
        }
        //3.到这里就可以安全执行命令了
        orderMapper.addOrder(order);



    }

    /*
     * 5.2删除所有订单
     * 业务逻辑：删除所有订单
     */
    @Override
    public void deleteOrderAll() {
        orderMapper.deleteOrderAll();
    }
}

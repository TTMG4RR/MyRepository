package com.drawbluecup.service.impl;

import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.Product;
import com.drawbluecup.entity.User;
import com.drawbluecup.exception.BusinessException;
import com.drawbluecup.mapper.OrderMapper;
import com.drawbluecup.mapper.ProductMapper;
import com.drawbluecup.mapper.UserMapper;
import com.drawbluecup.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ProductMapper productMapper;  // 注入商品 Mapper，用于校验商品是否存在


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

    /**
     * 查询订单及其关联的商品列表（多对多关系）
     * 业务逻辑：
     * 1. 校验订单ID是否合法
     * 2. 调用 Mapper 的 JOIN 查询，一次性获取订单和商品信息
     * 3. 如果订单不存在，抛出异常
     * 
     * @param orderId 订单ID
     * @return 包含商品列表的订单对象
     */
    @Override
    public Order findOrderWithProducts(Integer orderId) {
        // 1. 校验订单ID是否合法
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(400, "订单ID不合法");
        }

        // 2. 调用 Mapper 的 JOIN 查询
        // 注意：这里使用 findOrderWithProducts 方法，它会自动通过 resultMap 组装商品列表
        Order order = orderMapper.findOrderWithProducts(orderId);

        // 3. 校验订单是否存在
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        return order;
    }

    /**
     * 为订单添加商品（建立订单与商品的多对多关联）
     * 业务逻辑：
     * 1. 校验订单ID和商品ID是否合法
     * 2. 检查订单是否存在
     * 3. 检查商品是否存在
     * 4. 检查商品是否已经在订单中（避免重复添加）
     * 5. 向中间表 order_product 插入记录
     * 
     * @param orderId 订单ID
     * @param productId 商品ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)  // 开启事务，确保数据一致性
    public void addProductToOrder(Integer orderId, Integer productId) {
        // 1. 校验参数
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(400, "订单ID不合法");
        }
        if (productId == null || productId <= 0) {
            throw new BusinessException(400, "商品ID不合法");
        }

        // 2. 检查订单是否存在
        Order order = orderMapper.findByOrderId(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 3. 检查商品是否存在
        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }

        // 4. 检查商品是否已经在订单中（避免重复添加）
        // 思路：查询订单及其商品，如果商品列表包含该商品，说明已存在
        Order orderWithProducts = orderMapper.findOrderWithProducts(orderId);
        if (orderWithProducts.getProducts() != null) {
            boolean exists = orderWithProducts.getProducts().stream()
                    .anyMatch(p -> p.getId().equals(productId));
            if (exists) {
                throw new BusinessException(400, "商品已存在于订单中");
            }
        }

        // 5. 向中间表插入记录
        orderMapper.addProductToOrder(orderId, productId);
    }

    /**
     * 从订单中移除商品（删除订单与商品的关联）
     * 业务逻辑：
     * 1. 校验订单ID和商品ID是否合法
     * 2. 检查订单是否存在
     * 3. 检查商品是否在订单中
     * 4. 从中间表 order_product 删除记录
     * 
     * @param orderId 订单ID
     * @param productId 商品ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)  // 开启事务
    public void removeProductFromOrder(Integer orderId, Integer productId) {
        // 1. 校验参数
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(400, "订单ID不合法");
        }
        if (productId == null || productId <= 0) {
            throw new BusinessException(400, "商品ID不合法");
        }

        // 2. 检查订单是否存在
        Order order = orderMapper.findByOrderId(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 3. 检查商品是否在订单中
        Order orderWithProducts = orderMapper.findOrderWithProducts(orderId);
        if (orderWithProducts.getProducts() == null || orderWithProducts.getProducts().isEmpty()) {
            throw new BusinessException(400, "订单中没有该商品");
        }

        boolean exists = orderWithProducts.getProducts().stream()
                .anyMatch(p -> p.getId().equals(productId));
        if (!exists) {
            throw new BusinessException(400, "订单中没有该商品");
        }

        // 4. 从中间表删除记录
        orderMapper.removeProductFromOrder(orderId, productId);
    }
}

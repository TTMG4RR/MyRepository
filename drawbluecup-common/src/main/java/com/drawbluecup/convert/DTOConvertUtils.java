package com.drawbluecup.convert;

import com.drawbluecup.dto.order.OrderRespDTOWithUserAndProducts;
import com.drawbluecup.dto.product.ProductRespDTOWithout;
import com.drawbluecup.dto.user.UserRespDTOWithOrderwithproduct;
import com.drawbluecup.dto.user.UserRespDTOWithout;
import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.Product;
import com.drawbluecup.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 手动DTO转换工具类（全项目复用）
 * 核心：空值保护 + 逻辑统一 + 批量转换
 */
public class DTOConvertUtils {

    // ==================== 1. 单个User → UserRespDTOWithout ====================
    public static UserRespDTOWithout convertUserToDTO(User user) {
        // 空值保护：避免空指针
        if (user == null) {
            return null;
        }
        UserRespDTOWithout userDTO = new UserRespDTOWithout();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreateTime(user.getCreateTime());
        userDTO.setUpdateTime(user.getUpdateTime());
        return userDTO;
    }

    // ==================== 2. 单个Product → ProductRespDTOWithout ====================
    public static ProductRespDTOWithout convertProductToDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductRespDTOWithout productDTO = new ProductRespDTOWithout();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        // 如需添加商品其他字段（如价格），只改这里一次即可
        // productDTO.setPrice(product.getPrice());
        return productDTO;
    }

    // ==================== 3. Product列表 → ProductRespDTOWithout列表 ====================
    public static List<ProductRespDTOWithout> convertProductListToDTOList(List<Product> productList) {
        List<ProductRespDTOWithout> productDTOList = new ArrayList<>();
        // 空值保护：避免遍历null列表
        if (productList == null || productList.isEmpty()) {
            return productDTOList;
        }
        for (Product product : productList) {
            productDTOList.add(convertProductToDTO(product));
        }
        return productDTOList;
    }

    // ==================== 4. 单个Order(一对多) → OrderRespDTOWithUserAndProducts（核心） ====================
    public static OrderRespDTOWithUserAndProducts convertOrderToDTO(Order order, User user) {
        if (order == null) {
            return null;
        }
        OrderRespDTOWithUserAndProducts orderDTO = new OrderRespDTOWithUserAndProducts();
        // 1. 订单基础字段
        orderDTO.setId(order.getId());
        orderDTO.setOrderNo(order.getOrderNo());
        orderDTO.setCreateTime(order.getCreateTime());
        // 2. 用户信息（复用用户转换方法）
        UserRespDTOWithout userDTO = convertUserToDTO(user);
        if (userDTO != null) {
            orderDTO.setUserId(userDTO.getId());
            orderDTO.setUser(userDTO);
        }
        // 3. 商品列表（复用商品列表转换方法）
        orderDTO.setProducts(convertProductListToDTOList(order.getProducts()));
        return orderDTO;
    }

    // ==================== 5. Order列表 → OrderRespDTOWithUserAndProducts列表（批量转换） ====================
    public static List<OrderRespDTOWithUserAndProducts> convertOrderListToDTOList(List<Order> orderList,User user) {
        List<OrderRespDTOWithUserAndProducts> orderDTOList = new ArrayList<>();
        if (orderList == null || orderList.isEmpty()) {
            return orderDTOList;
        }
        // 注意：批量转换时，user需要外部查询后传入，这里先占位，接口中手动处理
        for (Order order : orderList) {
            orderDTOList.add(convertOrderToDTO(order, user));
        }
        return orderDTOList;
    }



}


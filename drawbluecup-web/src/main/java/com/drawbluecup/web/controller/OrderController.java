package com.drawbluecup.web.controller;


import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.User;
import com.drawbluecup.result.Result;
import com.drawbluecup.service.OrderService;
import com.drawbluecup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;




import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController  // 标记为 Controller，且所有方法返回 JSON 格式（替代 @Controller + @ResponseBody）
@RequestMapping("/api/order")  // 所有接口的统一前缀

@Tag(name = "订单管理", description = "订单增删改查接口")

public class OrderController {
    // 注入 Service 层对象（通过 Spring 自动赋值，不用手动 new）
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /*
     * 查询所有订单（GET 请求）
     * 请求方式：GET
     * 无参数，返回订单列表
     */
    @GetMapping("/findAll")
    @Operation(summary = "查询所有订单", description = "返回所有订单列表")
    public Result<List<Order>> findOrderAll() {
        return Result.success(200,"查询成功",orderService.findOrderAll());
    }


    /*实现根据用户id查询用户以及所对应订单
     *
     */
    @GetMapping("/findUserWithOrders/{id}")
    public Result<User> findUserWithOrders(
            @Parameter(description = "用户 ID", required = true)
            @PathVariable Integer id){

        return Result.success(200,"查询成功",orderService.findUserWithOrders(id));

    }

    /*
     * 新增订单
     * 前端填写订单信息（订单编号、订单对应用户id），通过这个接口保存到数据库
     */

    @PostMapping("/add")
    public Result<Void> addOrder(@RequestBody Order order)
    {

        orderService.addOrder(order);
        return Result.success(201,"新增该订单成功",null);

    }


    /*
     * 接口5.2：删除所有订单(慎重)
     * 前端想删除所有订单，通过这个接口删除数据库里的记录
     */
    @DeleteMapping("/deleteAll")
    public Result<Void> deleteOrderAll()
    {

        orderService.deleteOrderAll();
        return Result.success(200,"删除所有订单成功",null);

    }

    /**
     * 查询订单及其关联的商品列表（多对多关系）
     * 接口路径：/api/order/{orderId}/products
     * 请求方式：GET
     * 功能：通过订单ID查询订单信息，并返回该订单包含的所有商品列表
     * 
     * @param orderId 订单ID（路径变量）
     * @return 包含商品列表的订单对象
     */
    @GetMapping("/{orderId}/products")
    @Operation(summary = "查询订单及其商品", description = "根据订单ID查询订单信息，并返回该订单包含的所有商品列表")
    public Result<Order> findOrderWithProducts(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Integer orderId) {
        return Result.success(200, "查询成功", orderService.findOrderWithProducts(orderId));
    }

    /**
     * 为订单添加商品（建立订单与商品的多对多关联）
     * 接口路径：/api/order/{orderId}/products/{productId}
     * 请求方式：POST
     * 功能：将指定商品添加到指定订单中
     * 
     * @param orderId 订单ID（路径变量）
     * @param productId 商品ID（路径变量）
     * @return 操作结果
     */
    @PostMapping("/{orderId}/products/{productId}")
    @Operation(summary = "为订单添加商品", description = "将指定商品添加到指定订单中，建立订单与商品的多对多关联")
    public Result<Void> addProductToOrder(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Integer orderId,
            @Parameter(description = "商品ID", required = true)
            @PathVariable Integer productId) {
        orderService.addProductToOrder(orderId, productId);
        return Result.success(200, "商品已添加到订单", null);
    }

    /**
     * 从订单中移除商品（删除订单与商品的关联）
     * 接口路径：/api/order/{orderId}/products/{productId}
     * 请求方式：DELETE
     * 功能：从指定订单中移除指定商品
     * 
     * @param orderId 订单ID（路径变量）
     * @param productId 商品ID（路径变量）
     * @return 操作结果
     */
    @DeleteMapping("/{orderId}/products/{productId}")
    @Operation(summary = "从订单中移除商品", description = "从指定订单中移除指定商品，删除订单与商品的关联")
    public Result<Void> removeProductFromOrder(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Integer orderId,
            @Parameter(description = "商品ID", required = true)
            @PathVariable Integer productId) {
        orderService.removeProductFromOrder(orderId, productId);
        return Result.success(200, "商品已从订单中移除", null);
    }













}

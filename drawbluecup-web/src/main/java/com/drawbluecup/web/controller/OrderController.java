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
        return Result.success("查询成功",orderService.findOrderAll());
    }


    /*实现根据用户id查询用户以及所对应订单
     *
     */
    @GetMapping("/findUserWithOrders/{id}")
    public Result<User> findUserWithOrders(
            @Parameter(description = "用户 ID", required = true)
            @PathVariable Integer id){

        return Result.success("查询成功",orderService.findUserWithOrders(id));

    }

    /*
     * 新增订单
     * 前端填写订单信息（订单编号、订单对应用户id），通过这个接口保存到数据库
     */

    @PostMapping("/add")
    public Result<Void> addOrder(@RequestBody Order order)
    {

        orderService.addOrder(order);
        return Result.success("新增该订单成功",null);

    }


    /*
     * 接口5.2：删除所有订单(慎重)
     * 前端想删除所有订单，通过这个接口删除数据库里的记录
     */
    @DeleteMapping("/deleteAll")
    public Result<Void> deleteOrderAll()
    {

        orderService.deleteOrderAll();
        return Result.success("删除所有订单成功",null);

    }













}

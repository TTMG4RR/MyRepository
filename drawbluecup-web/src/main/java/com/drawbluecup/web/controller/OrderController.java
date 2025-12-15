package com.drawbluecup.web.controller;


import com.drawbluecup.convert.DTOConvertUtils;
import com.drawbluecup.dto.order.OrderAddDTO;
import com.drawbluecup.dto.order.OrderRespDTOWithUserAndProducts;
import com.drawbluecup.dto.product.ProductRespDTOWithout;
import com.drawbluecup.dto.user.UserRespDTOWithOrderwithproduct;
import com.drawbluecup.dto.user.UserRespDTOWithout;
import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.Product;
import com.drawbluecup.entity.User;
import com.drawbluecup.result.Result;
import com.drawbluecup.service.OrderService;
import com.drawbluecup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Operation(summary = "查询所有订单", description = "返回所有订单列表以及所属用户,包含商品,与user一对多,与product多对多")
    public Result<List<OrderRespDTOWithUserAndProducts>> findOrderAll() {
        // 1. 本质转移，获取所有待转对象
        List<Order> orderList = orderService.findOrderAll();

        // 2.设置出参DTO集合(收纳容器的容器),由于收集DTO(结果)
        List<OrderRespDTOWithUserAndProducts> respDTOList = new ArrayList<>();

        //3.将待转对象一个个装进一个个新建容器,再逐个装进容器的容器
        for (Order order : orderList) {
            OrderRespDTOWithUserAndProducts  orderRespDTOWith = new OrderRespDTOWithUserAndProducts();

            orderRespDTOWith.setId(order.getId());
            orderRespDTOWith.setOrderNo(order.getOrderNo());
            orderRespDTOWith.setCreateTime(order.getCreateTime());

        //我们希望在每一个容器里面有原来待转对象没有的元素,所以我们有新增一批/个待转对象,可以直接放入,但是更优雅的做法是DTO(装容器)
            //字段装容器
            User user = orderService.findUserWithOrders(order.getUserId());

            UserRespDTOWithout userRespDTOWithout = new UserRespDTOWithout();

            userRespDTOWithout.setId(user.getId());
            userRespDTOWithout.setName(user.getName());
            userRespDTOWithout.setPhone(user.getPhone());
            userRespDTOWithout.setCreateTime(user.getCreateTime());
            userRespDTOWithout.setUpdateTime(user.getUpdateTime());


            orderRespDTOWith.setUserId(userRespDTOWithout.getId());
            orderRespDTOWith.setUser(userRespDTOWithout);

            // ③ 新增：赋值商品列表（沿用你的for循环格式，无Stream）
            // 先查当前订单关联的商品列表
            List<Product> productList = orderService.findProductsByOrderId(order.getId());
            // 初始化商品DTO列表
            List<ProductRespDTOWithout> productDTOList = new ArrayList<>();
            // 循环处理每个商品，转成DTO
            for (Product product : productList) {
                ProductRespDTOWithout productDTO = new ProductRespDTOWithout();
                productDTO.setId(product.getId());
                productDTO.setName(product.getName());

                productDTOList.add(productDTO);
            }
            // 把商品DTO列表赋值给订单DTO
            orderRespDTOWith.setProducts(productDTOList);

            // 把当前订单DTO加入结果集
            respDTOList.add(orderRespDTOWith);
        }

        return Result.success(200,"查询成功",respDTOList);
        //有时间要封装实体类转DTO
    }


    /*实现根据用户id查询用户以及所对应订单
     *
     */
    @GetMapping("/findUserWithOrders/{id}")
    @Operation(summary = "根据用户id查询用户以及所对应订单", description = "实现根据用户id查询用户以及所对应订单")

    public Result<UserRespDTOWithOrderwithproduct> findUserWithOrders(
            @Parameter(description = "用户 ID", required = true)
            @PathVariable Integer id){

        User userWithOrders = orderService.findUserWithOrders(id);
        UserRespDTOWithOrderwithproduct userRespDTOWithOrderwithproduct = new UserRespDTOWithOrderwithproduct();//总容器
        userRespDTOWithOrderwithproduct.setUserRespDTOWithout(  DTOConvertUtils.convertUserToDTO(userWithOrders));//装user部分

        List<Order> Orders = userWithOrders.getOrders();
        userRespDTOWithOrderwithproduct.setOrderDTOWithUserAndProducts(DTOConvertUtils.convertOrderListToDTOList(Orders,userWithOrders));


        return Result.success(200,"查询成功",userRespDTOWithOrderwithproduct);

    }

    /*
     * 新增订单
     * 前端填写订单信息（订单编号、订单对应用户id），通过这个接口保存到数据库
     */

    @PostMapping("/add")
    @Operation(summary = "新增订单", description = "不需要传输自增id")
    public Result<Void> addOrder(@RequestBody OrderAddDTO addDTO)
    {
        Order order = new Order();
        order.setOrderNo(addDTO.getOrderNo());
        order.setUserId(addDTO.getUserId());

        orderService.addOrder(order);

        return Result.success(201,"新增该订单成功",null);

    }


    /*
     * 接口5.2：删除所有订单(慎重)
     * 前端想删除所有订单，通过这个接口删除数据库里的记录
     */
    @DeleteMapping("/deleteAll")
    @Operation(summary = "删除所有订单(慎重)")
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
    @Operation(summary = "根据订单id查询订单", description = "根据订单ID查询订单信息，并返回该订单包含的所有商品列表和所属用户")
    public Result<OrderRespDTOWithUserAndProducts> findOrderWithProductsDTO(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Integer orderId) {

        //只处理单个订单，不用循环遍历所有订单，和查询所有核心逻辑高度复用，只是少了一层外层循环

        // 1. 查询原始订单（包含商品、用户关联信息）
        Order order = orderService.findOrderWithProducts(orderId);

        // 2. 初始化订单DTO，赋值基础信息
        OrderRespDTOWithUserAndProducts orderDTO = new OrderRespDTOWithUserAndProducts();
        orderDTO.setId(order.getId());
        orderDTO.setOrderNo(order.getOrderNo());
        orderDTO.setCreateTime(order.getCreateTime());

        // 3. 赋值所属用户（转成UserRespDTOWithout，过滤冗余字段）
        User user = userService.findById(order.getUserId());
        UserRespDTOWithout userDTO = new UserRespDTOWithout();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreateTime(user.getCreateTime());
        userDTO.setUpdateTime(user.getUpdateTime());
        orderDTO.setUser(userDTO);
        orderDTO.setUserId(user.getId());

        // 4. 赋值商品列表（转成ProductRespDTOWithout，过滤orders字段）
        List<Product> productList = order.getProducts();
        List<ProductRespDTOWithout> productDTOList = new ArrayList<>();
        for (Product product : productList) {
            ProductRespDTOWithout productDTO = new ProductRespDTOWithout();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTOList.add(productDTO);
        }
        orderDTO.setProducts(productDTOList);

        // 5. 返回DTO（无冗余字段）
        return Result.success(200, "查询成功", orderDTO);
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

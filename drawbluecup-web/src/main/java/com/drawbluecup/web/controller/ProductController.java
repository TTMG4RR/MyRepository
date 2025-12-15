package com.drawbluecup.web.controller;

import com.drawbluecup.convert.DTOConvertUtils;
import com.drawbluecup.dto.order.OrderRespDTOWithUserAndProducts;
import com.drawbluecup.dto.product.ProductAddDTO;
import com.drawbluecup.dto.product.ProductRespDTOWithOrders;
import com.drawbluecup.dto.product.ProductRespDTOWithout;
import com.drawbluecup.dto.product.ProductUpdateDTO;
import com.drawbluecup.dto.user.UserRespDTOWithout;
import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.Product;
import com.drawbluecup.entity.User;
import com.drawbluecup.result.Result;
import com.drawbluecup.service.ProductService;
import com.drawbluecup.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/*
 * 用户管理接口层（Controller）
 * 作用：接收前端 HTTP 请求，调用 Service 处理，返回结果
 */
@RestController  // 标记为 Controller，且所有方法返回 JSON 格式（替代 @Controller + @ResponseBody）
                // 用 @RestController 写接口、返回 Java 对象，Spring 会**自动调用 Jackson** 帮你转成 JSON。
@RequestMapping("/api/product")  // 所有接口的统一前缀
//http://localhost:8080

@Tag(name = "商品Product管理", description = "商品增删改查接口")

//Swagger3.x 注解：生成 API 文档时，@Tag是接口分组名，@Operation是单个接口的描述

public class ProductController {


    // 注入 Service 层对象（通过 Spring 自动赋值，不用手动 new）
    @Autowired
    private ProductService productService;//依赖的是接口（UserService）而不是实现类
    @Autowired
    private UserService userService;


    /*
     * 1.1查询所有商品（GET 请求）
     * 接口路径：/api/product/findAll
     * 请求方式：GET
     * 无参数，返回商品列表
     */
    @GetMapping("/findAll")
    @Operation(summary = "查询所有商品")

    public Result<PageInfo<Product>> findAll(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize){//实体转DTO

        // 1. 调用Service获取所有Product实体类并分页
        PageInfo<Product> productPageInfo = productService.findAll(pageNum, pageSize);

        // 3. 返回分页结果
        return Result.success(200,"查询成功" , productPageInfo);
    }

    /*
     * 1.2根据id查询商品
     * 接口路径：/api/product/findById
     * 请求方式：GET
     * 参数id，返回商品列表
     */
    @GetMapping("/findById/{id}")
    @Operation(summary = "根据id查询商品", description = "根据商品ID查询商品信息")

    public Result<ProductRespDTOWithout> findById(@PathVariable Integer id){

        // 1. 调用Service获取实体类（还是原来的逻辑，Service返回Product）
        Product product = productService.findById(id);

        // 2. 实体类转DTO（只赋值前端需要的id和name）
        ProductRespDTOWithout respDTO = new ProductRespDTOWithout();
        respDTO.setId(product.getId());
        respDTO.setName(product.getName());

        // 3. 返回DTO给前端（前端只看到id+name，看不到其他字段）
        return Result.success(200,"查询成功",respDTO);
    }

    /*
     * 1.3根据name查询商品
     * 接口路径：/api/product/findByName
     * 请求方式：GET
     * 参数name，返回商品列表
     */
    @GetMapping("/findByName/{name}")
    @Operation(summary = "根据name查询商品", description = "根据商品name查询商品信息")
    public Result<ProductRespDTOWithout> findByName(@PathVariable String name){

        // 1. 调用Service获取实体类（还是原来的逻辑，Service返回Product）
        Product product = productService.findByName(name);

        // 2. 实体类转DTO（只赋值前端需要的id和name）
        ProductRespDTOWithout respDTO = new ProductRespDTOWithout();
        respDTO.setId(product.getId());
        respDTO.setName(product.getName());

        // 3. 返回DTO给前端（前端只看到id+name，看不到其他字段）
        return Result.success(200,"查询成功",respDTO);

    }

    /*
     * 2.1删除所有商品
     * 接口路径：/api/product/deleteAll
     * 请求方式：Delete
     * 无参数，无返回
     */
    @DeleteMapping("/deleteAll")
    @Operation(summary = "删除所有商品（慎重）")
    public Result<Void> deleteAll(){
        productService.deleteAll();//不能写在下面,因为不返回值
        return Result.success(200,"删除所有商品成功",null);
    }

    /*
     * 2.2删除商品
     * 接口路径：/api/product/deleteById
     * 请求方式：Delete
     * 参数id，无返回
     */

    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "基于id删除商品")
    public Result<Void> deleteById(@PathVariable Integer id){
        productService.deleteById(id);
        return Result.success(200,"删除成功",null);
    }



    /*
     * 2.1添加商品
     * 接口路径：/api/product/add
     * 请求方式：Post
     * 参数对象(不用包含id,自增)，无返回
     */
    @PostMapping("/add")
    @Operation(summary = "新增商品", description = "不需要传输自增id")
    public Result<Void> add(@RequestBody ProductAddDTO addDTO){ // 接收DTO，不再接收Product

        // 关键：DTO转实体类（只赋值name字段）
        Product product = new Product();
        product.setName(addDTO.getName());// 手动赋值（你的场景字段少，不用BeanUtils）

        //给服务层实体类
        productService.add(product);
        return Result.success(201,"添加成功",null);
    }



    /*
     * 3.1修改商品
     * 接口路径：/api/product/update
     * 请求方式：Put
     * 参数对象(其中包含id和其他字段,id用来定位,其他是修改)，无返回
     */
    @PutMapping("/update")
    @Operation(summary = "基于id查询修改单个商品")//因为要用到id查询,增加DTO不适用了(本来也不应该混用😒)

    public Result<Void> update(@RequestBody ProductUpdateDTO updateDTO){

        //将前端DTO转换为实体类
        Product product = new Product();
        product.setId(updateDTO.getId());//一一赋值
        product.setName(updateDTO.getName());

        productService.update(product);
        return Result.success(200,"修改成功",null);
    }


    @GetMapping("/{productId}/orders")
    @Operation(summary = "查询商品及其关联的订单", description = "根据商品ID查询商品信息，并返回包含该商品的所有订单列表")
    public Result<ProductRespDTOWithOrders> findProductWithOrders(@PathVariable Integer productId) {

        ProductRespDTOWithOrders productRespDTOWithOrders = new ProductRespDTOWithOrders();//总容器

        Product productWithOrders =  productService.findProductWithOrders(productId);
        //单独处理订单加用户
        List<Order> Orders = productWithOrders.getOrders();//材料
        List<OrderRespDTOWithUserAndProducts> OrderList = new ArrayList<>();//容器


        for(Order order : Orders) {
            User user = userService.findById(order.getUserId());

            OrderList.add(DTOConvertUtils.convertOrderToDTO(order,user));//只传实体类
        }


        productRespDTOWithOrders.setOrders(OrderList);
        productRespDTOWithOrders.setId(productId);
        productRespDTOWithOrders.setName(productWithOrders.getName());




        return Result.success(200, "查询成功", productRespDTOWithOrders);

    }


}

//若有抛出异常,会被自动异常处理器接受
/*注解
    @RestController 标记为 Controller       (替代 @Controller)
                    且所有方法返回 JSON 格式   (替换@ResponseBody)
    @RequestMapping ("...")  // 所有接口的统一前缀
    @RequestBody    与@ResponseBody相反,是将前端的json格式转换成相应的对象
    @PathVariable   把路径的{参数}拉下来
    @Autowired      直接创建对象并赋值不用手动new

 */

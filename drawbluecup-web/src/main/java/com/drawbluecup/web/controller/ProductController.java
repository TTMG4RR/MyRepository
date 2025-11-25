package com.drawbluecup.web.controller;

import com.drawbluecup.entity.Product;
import com.drawbluecup.result.Result;
import com.drawbluecup.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
 * 用户管理接口层（Controller）
 * 作用：接收前端 HTTP 请求，调用 Service 处理，返回结果
 */
@RestController  // 标记为 Controller，且所有方法返回 JSON 格式（替代 @Controller + @ResponseBody）
@RequestMapping("/api/product")  // 所有接口的统一前缀
//http://localhost:8080

public class ProductController {


    // 注入 Service 层对象（通过 Spring 自动赋值，不用手动 new）
    @Autowired
    private ProductService productService;//依赖的是接口（UserService）而不是实现类


    /*
     * 1.1查询所有商品（GET 请求）
     * 接口路径：/api/product/findAll
     * 请求方式：GET
     * 无参数，返回商品列表
     */
    @GetMapping("/findAll")
    public Result<List<Product>> findAll(){
    return Result.success(200003,"查询成功" , productService.findAll());
    }

    /*
     * 1.2根据id查询商品
     * 接口路径：/api/product/findById
     * 请求方式：GET
     * 参数id，返回商品列表
     */
    @GetMapping("/findById/{id}")
    public Result<Product> findById(@PathVariable Integer id){
        return Result.success(200003,"查询成功" , productService.findById(id));
    }

    /*
     * 1.3根据name查询商品
     * 接口路径：/api/product/findByName
     * 请求方式：GET
     * 参数name，返回商品列表
     */
    @GetMapping("/findByName/{name}")
    public Result<Product> findByName(@PathVariable String name){
        return Result.success(200003,"查询成功" , productService.findByName(name));
    }

    /*
     * 2.1删除所有商品
     * 接口路径：/api/product/deleteAll
     * 请求方式：Delete
     * 无参数，无返回
     */
    @DeleteMapping("/deleteAll")
    public Result<Void> deleteAll(){
        productService.deleteAll();//不能写在下面,因为不返回值
        return Result.success(200003,"删除所有商品成功",null);
    }

    /*
     * 2.2删除商品
     * 接口路径：/api/product/deleteById
     * 请求方式：Delete
     * 参数id，无返回
     */
    @DeleteMapping("/deleteById/{id}")
    public Result<Void> deleteById(@PathVariable Integer id){
        productService.deleteById(id);
        return Result.success(200003,"删除成功",null);
    }

    /*
     * 2.1添加商品
     * 接口路径：/api/product/add
     * 请求方式：Post
     * 参数对象(不用包含id,自增)，无返回
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Product product){
        productService.add(product);
        return Result.success(200003,"添加成功",null);
    }

    /*
     * 3.1修改商品
     * 接口路径：/api/product/update
     * 请求方式：Put
     * 参数对象(其中包含id和其他字段,id用来定位,其他是修改)，无返回
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Product product){
        productService.update(product);
        return Result.success(200003,"修改成功",null);
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

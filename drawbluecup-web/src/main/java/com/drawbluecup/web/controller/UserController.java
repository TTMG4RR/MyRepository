package com.drawbluecup.web.controller;



import com.drawbluecup.entity.User;
import com.drawbluecup.result.Result;
import com.drawbluecup.service.UserService;
import com.drawbluecup.validation.Phone;

import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

//控制层,用来接收前端发来的URL路径请求以及内含的数据,做出相应措施
//服务层已经处理好了逻辑--->不合理的将会抛出异常附带异常信息,合理将执行方法
//现在需要处理好服务,在执行方法过程中,合理就返回值(用Result包装)


/*
 * 用户管理接口层（Controller）
 * 作用：接收前端 HTTP 请求，调用 Service 处理，返回结果
 */
@RestController  // 标记为 Controller，且所有方法返回 JSON 格式（替代 @Controller + @ResponseBody）
@RequestMapping("/api/user")  // 所有接口的统一前缀（比如查询所有用户的接口路径是 /api/user/findAll）
@Validated
public class UserController {

    // 注入 Service 层对象（通过 Spring 自动赋值，不用手动 new）
    @Autowired
    private UserService userService;//依赖的是接口（UserService）而不是实现类

    /*
     * 查询所有用户
     * 接口路径：/api/user/findAll
     * 请求方式：GET
     * 无参数，返回用户列表
     */

    @GetMapping("/findAll")  // 接收 GET 请求，子路径是 /findAll
    public Result<List<User>> findUserAll() {
        // 调用 Service 的 findAll() 方法，获取结果并返回给前端
        return Result.success(200,"查询成功",userService.findAll());
        //return userService.findAll();

    }




    /*
     * 2.0实现“根据ID查询单个用户”功能
     * 业务逻辑：先校验ID是否合法，再查数据库，最后校验结果是否存在
     */

    //示例:http://localhost:8080/api/user/findById/6
    @GetMapping("/findById/{id}")
    public Result<User> findById(@PathVariable Integer id){

            return Result.success(200,"查询成功",userService.findById(id));
            //return "查询成功"+userService.findById(id);

    }


    /*
     * 接口2.1实现“根据phone查询单个用户”功能
     * 业务逻辑：先校验phone是否合法，再查数据库，最后校验结果是否存在
     */
    //示例:http://localhost:8080/api/user/findByPhone/{phone}
    @GetMapping("/findByPhone/{phone}")
    public Result<User> findByPhone(@PathVariable @Phone String phone){
            return Result.success(200,"查询成功",userService.findByPhone(phone));

    }



    //示例:http://localhost:8080/api/user/query？name=w
    //http://localhost:8080/api/user/query?name=w&phone=5
    //多个 Query 参数之间用 & 分隔，后端会同时接收 name 和 phone 参数，
    //然后在 SQL 中同时拼接这两个条件（姓名含 “w” 且手机号含 “5” 的用户）。
    /*接口2.3条件查询用户
     *
     */
    @GetMapping("/query")
    public Result<PageInfo<User>> queryUserByCondition(
            @RequestParam(required = false) String name,   // required=false：参数可选
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) LocalDateTime createTime,
            @RequestParam(required = false) LocalDateTime updateTime,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize
    )
    {
        return Result.success(200,"用户条件查询成功", userService.queryUserByCondition(name, phone, createTime ,updateTime ,pageNum, pageSize));
    }
//success 是静态方法，不需要创建类的实例就能调用





    /*
     * 接口3.0：新增用户
     * 前端填写用户信息（姓名、手机号），通过这个接口保存到数据库
     */
    // @PostMapping("/add")：表示这个方法处理“POST类型”的请求，子路径是/add
    // POST类型请求通常用来“新增数据”，会往数据库里加新记录

    //示例:http://localhost:8080/api/user/add
    @PostMapping("/add")

    // @RequestBody User user：把前端传来的JSON格式数据，自动转成User对象//接受前端
    // （比如前端传{"name":"张三","phone":"123"}，这里就会得到一个name=张三、phone=123的User对象）


    public Result<Void> addUser (@Valid @RequestBody User user)
    {
            userService.addUser(user);
            return Result.success(201,"新增该用户成功",null);

    }






    /*
     * 接口4：修改用户
     * 前端修改已有的用户信息，通过这个接口更新到数据库
     */
    // PUT类型请求通常用来“修改数据”，会更新数据库里的已有记录

    //示例:http://localhost:8080/api/user/update
    @PutMapping("/update")
    //@RequestBody User user：接收前端传来的JsoN，转成User对象
    public Result<Void> updateUser(@Valid @RequestBody User user)
    {

            userService.updateUser(user);
            return Result.success(200,"修改该用户成功",null);

    }




    /*
     * 接口5：删除某个用户
     * 前端想删除某个用户，通过这个接口删除数据库里的记录
     */
    // @DeleteMapping("/delete/{id}")：处理“DELETE类型”的请求，子路径里的{id}是要删除的用户ID
    // DELETE类型请求通常用来“删除数据”，会从数据库里删掉记录
    //示例:http://localhost:8080/api/user/delete/{id}
    @DeleteMapping("/delete/{id}")
    //通过路径来输入/接收
    //{id} 是路径变量，对应前端传递的用户ID（比如请求 /delete/1 表示删除ID=1的用户）
    public Result<Void> deleteUser(@PathVariable Integer id)
    {
            userService.deleteUser(id);
            return Result.success(200,"删除该用户成功",null);

    }



    /*
     * 接口5.1：删除所有用户但不包括订单(慎重)
     * 前端想删除所有用户，通过这个接口删除数据库里的记录
     */
    @DeleteMapping("/deleteUserAll")
    public Result<Void> deleteUserAll()
    {

            userService.deleteUserAll();
            return Result.success(200,"删除所有用户成功",null);

    }










}

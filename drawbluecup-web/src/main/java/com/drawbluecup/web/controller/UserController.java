package com.drawbluecup.web.controller;

import com.drawbluecup.JWT.JwtUtils;
import com.drawbluecup.dto.user.LoginAndToken.UserLoginDTO;
import com.drawbluecup.dto.user.LoginAndToken.UserLoginRespDTO;
import com.drawbluecup.dto.user.LoginAndToken.UserRefreshTokenDTO;
import com.drawbluecup.dto.user.LoginAndToken.UserRefreshTokenRespDTO;
import com.drawbluecup.dto.user.UserAddDTO;
import com.drawbluecup.dto.user.UserRespDTOWithout;
import com.drawbluecup.dto.user.UserUpdateDTO;
import com.drawbluecup.entity.User;
import com.drawbluecup.result.Result;
import com.drawbluecup.service.UserService;
import com.drawbluecup.validation.Phone;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@Tag(name = "用户管理", description = "用户增删改查接口")

public class UserController {

    // 注入 Service 层对象（通过 Spring 自动赋值，不用手动 new）
    @Autowired
    private UserService userService;//依赖的是接口（UserService）而不是实现类

    /**
     * 查询所有用户
     * 接口路径：/api/user/findAll
     * 请求方式：GET
     * 无参数，返回用户列表
     */

    @GetMapping("/findAll")// 接收 GET 请求，子路径是 /findAll
    @Operation(summary = "查询所有用户", description = "查询所有用户以及和用户相关的数据")

    public Result<PageInfo<User>> findUserAll(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        // 1. 调用Service获取所有User实体类并分页
        PageInfo<User> userPageInfo = userService.findAll(pageNum, pageSize);

        return Result.success(200,"查询成功",userPageInfo);


    }

//================================================================================================



    /**
     * 2.0实现“根据ID查询单个用户”功能
     * 业务逻辑：先校验ID是否合法，再查数据库，最后校验结果是否存在
     */

    // ========== 优化：查询接口增加权限校验（只能查自己） ==========
    @GetMapping("/findById/{id}")
    @Operation(summary = "根据ID查询单个用户信息")
    public Result<UserRespDTOWithout> findById(@PathVariable Integer id, HttpServletRequest request) {
        // 新增：从request获取登录用户ID，校验权限

        Integer loginUserId = (Integer) request.getAttribute("loginUserId");
        //request.getAttribute("loginUserId") 的返回值
        // 是 Object类型（Java 里的 “万能类型”，可以存任意数据，比如整数、字符串、对象）。
        //存进去的是 Integer 类型的用户 ID
        //明确告诉程序 “这个 Object 其实是 Integer”—— 这就是强制类型转换，用 (Integer) 表示

        //校验权限
        if (loginUserId != null && !loginUserId.equals(id)) {//token放行的id要匹配到数据库对应的id
            return Result.error(403, "无权查询他人信息");
        }

        //封装成DTO并返回
        UserRespDTOWithout respDTOWithout = new UserRespDTOWithout();
        User user = userService.findById(id);

        respDTOWithout.setId(user.getId());
        respDTOWithout.setName(user.getName());
        respDTOWithout.setPhone(user.getPhone());
        respDTOWithout.setCreateTime(user.getCreateTime());
        respDTOWithout.setUpdateTime(user.getUpdateTime());

        return Result.success(200, "查询成功", respDTOWithout);
    }



    // ========== 新增：登录接口（返回双Token） ==========
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "登录成功返回Access Token和Refresh Token")
    public Result<UserLoginRespDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        // 1. 调用Service"验证"登录
        User user = userService.login(loginDTO.getUserId(), loginDTO.getPassword());

        // 2. 组装用户信息，生成双Token
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getId());
        userInfo.put("phone", user.getPhone());

        String accessToken = JwtUtils.generateToken(userInfo, JwtUtils.ACCESS_TOKEN_EXPIRE);
        String refreshToken = JwtUtils.generateToken(userInfo, JwtUtils.REFRESH_TOKEN_EXPIRE);

        // 3. 封装返回结果
        UserLoginRespDTO respDTO = new UserLoginRespDTO();
        respDTO.setAccessToken(accessToken);
        respDTO.setRefreshToken(refreshToken);
        respDTO.setUserName(user.getName());

        return Result.success(200, "登录成功", respDTO);
    }



    // ========== 新增：刷新Token接口 ==========
    @PostMapping("/refreshToken")
    @Operation(summary = "刷新Access Token", description = "用Refresh Token获取新的Access Token")
    public Result<UserRefreshTokenRespDTO> refreshToken(@Valid @RequestBody UserRefreshTokenDTO refreshTokenDTO) {
        try {
            // 1. 解析Refresh Token
            Map<String, Object> userInfo = JwtUtils.parseToken(refreshTokenDTO.getRefreshToken());
            // 2. 生成新的Access Token
            String newAccessToken = JwtUtils.generateToken(userInfo, JwtUtils.ACCESS_TOKEN_EXPIRE);

            // 3. 封装返回
            UserRefreshTokenRespDTO respDTO = new UserRefreshTokenRespDTO();
            respDTO.setNewAccessToken(newAccessToken);
            return Result.success(200, "Access Token刷新成功", respDTO);
        }
        catch (RuntimeException e) {
            return Result.error(401, e.getMessage() + "，请重新登录");
        }
    }

//Refresh Token 里包含用户信息的核心原因是：它和 Access Token 是用同一个用户信息、同一个生成方法创建的，
// 只是过期时间不同。这样设计的目的是让刷新 Token 的逻辑更简单、无状态，前端无需重复传用户信息

//================================================================================================








    /**
     * 接口2.1实现“根据phone查询单个用户信息”功能
     * 业务逻辑：先校验phone是否合法，再查数据库，最后校验结果是否存在
     */
    //示例:http://localhost:8080/api/user/findByPhone/{phone}
    @GetMapping("/findByPhone/{phone}")
    @Operation(summary = "根据phone查询单个用户信息")
    public Result<UserRespDTOWithout> findByPhone(@PathVariable @Phone String phone){

        UserRespDTOWithout respDTOWithout = new UserRespDTOWithout();
        User user = userService.findByPhone(phone);

        respDTOWithout.setId(user.getId());
        respDTOWithout.setName(user.getName());
        respDTOWithout.setPhone(user.getPhone());
        respDTOWithout.setCreateTime(user.getCreateTime());
        respDTOWithout.setUpdateTime(user.getUpdateTime());

            return Result.success(200,"查询成功",respDTOWithout);

    }



    //示例:http://localhost:8080/api/user/query？name=w
    //http://localhost:8080/api/user/query?name=w&phone=5
    //多个 Query 参数之间用 & 分隔，后端会同时接收 name 和 phone 参数，
    //然后在 SQL 中同时拼接这两个条件（姓名含 “w” 且手机号含 “5” 的用户）。
    /*接口2.3条件查询用户
     *
     */
    @GetMapping("/query")
    @Operation(summary = "条件模糊分页查询",description = "可以多个模糊条件组合查询，拥有分页功能")
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
    @Operation(summary = "新增用户")
    // @RequestBody User user：把前端传来的JSON格式数据，自动转成User对象//接受前端

    public Result<Void> addUser (@Valid @RequestBody UserAddDTO addDTO)
    {
        User user = new User();
        user.setPhone(addDTO.getPhone());
        user.setName(addDTO.getName());
        user.setPassword(addDTO.getPassword());

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
    @Operation(summary = "基于id查询修改单个用户")
    //@RequestBody User user：接收前端传来的JsoN，转成User对象
    public Result<Void> updateUser(@Valid @RequestBody UserUpdateDTO updateDTO)
    {
        User user = new User();
        user.setPhone(updateDTO.getPhone());
        user.setName(updateDTO.getName());
        user.setId(updateDTO.getId());
        user.setPassword(updateDTO.getPassword());

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
    @Operation(summary = "基于id删除单个用户")
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
    @Operation(summary = "删除所有用户（慎重）")
    public Result<Void> deleteUserAll()
    {

            userService.deleteUserAll();
            return Result.success(200,"删除所有用户成功",null);

    }






    }











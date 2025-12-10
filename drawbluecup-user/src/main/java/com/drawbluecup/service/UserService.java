package com.drawbluecup.service;


import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    // 对应 Mapper 的核心方法，加上业务相关的定义

    List<User> findAll(); // 查全部用户
    void addUser(User user); // 新增（以后可加“手机号重复校验”逻辑）

    void deleteUser(Integer id); // 删除
    void deleteUserAll(); // 删除所有用户

    void updateUser(User user); // 修改（以后可加“记录存在校验”逻辑）

    User findById(Integer id); // 单条查询通过id
    User findByPhone(String phone);// 单条查询phone


    /*
     * 按条件查询用户并分页
     * @param name 姓名（可选）
     * @param phone 手机号（可选）
     * @param pageNum 页码（默认第1页）(可选,只不过就算不选也会自动赋默认值)
     * @param pageSize 每页条数（默认10条）(可选,只不过就算不选也会自动赋默认值)
     * @return 分页结果（包含用户列表、总条数、总页数等）
     */
    PageInfo<User> queryUserByCondition(String name, String phone, LocalDateTime createTime, LocalDateTime updateTime, Integer pageNum, Integer pageSize);

    User login(String phone, String password);//登录验证方法


}

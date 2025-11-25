package com.drawbluecup.service.impl;


import com.drawbluecup.entity.Order;
import com.drawbluecup.entity.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.drawbluecup.mapper.UserMapper;
import com.drawbluecup.service.UserService;

import com.drawbluecup.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/*
 * UserService 接口的实现类，负责具体的业务逻辑处理
 * 服务层,持久层已经通过xml来构建方法,
 * 现在需要处理逻辑,在合适的时候执行方法,不合适的时候抛出异常附带信息
 *
 *
 *
 * 标注 @Service 让 Spring 容器管理这个类，后续可以被 Controller 注入使用
 */
@Service


/*
 *心得:关于增删查改
 * ***0.查询是基础(访问数据),后面的新增,修改,删除(操作数据)都要先访问数据
 *
 * 1.对于有传参的方法,可以直接先检查参数是否合法---一般是检查非NULL和非---检查非空性数据" "(包含需要传参的查询方法)
 * 1.1对于查询,如果查不到,还要抛出异常,后面的增删改就直接调用查就好了
 * 2.(修)根据id(具有自增性,不需要传)查询用户(为后面进一步检查)
 * 3.(修)进一步检查唯一性数据(先排除自己和自己重复,然后按唯一性数据,寻找是否还有其他使用该数据的人)
 * 3.5(增))进一步检查唯一性数据(不用排除自己和自己重复(因为没有),然后按唯一性数据,寻找是否还有其他使用该数据的人)
 * 7.其中查询用户直接复用：findById 方法被 updateUser、deleteUser 复用，减少了重复代码。
 */


//目录
/*
* public  List<User> findAll()
* public User findById(Integer id)
* public User findByPhone(String phone)
* public void addUser(User user)
* public void updateUser(User user)
* public void deleteUser(Integer id)
*
 */





public class UserServiceImpl implements UserService {

    // 注入 UserMapper（MyBatis 自动生成的代理对象），通过它操作数据库
    // @Autowired 会让 Spring 自动把 UserMapper 的实例赋值给这个变量，不用手动 new
    @Autowired
    private UserMapper userMapper;



    /*

     * 1查询所有用户
     * 业务逻辑：直接查询，无额外校验
     */
    @Override//要与接口一一对应
public  List<User> findAll() {
    //调用 Mapper 的 findAll() 方法，获取数据库中所有用户
    List<User> userList = userMapper.findAll();
    // 如果查询结果为空，返回空列表（避免 null，方便前端处理）
    return userList != null ? userList : List.of();
}



/*
 * 2根据 ID 查询单个用户
 * 业务逻辑：查询前先检查 ID 非空(否则抛异常)，查询后判断记录是否存在
 */

    @Override
    public User findById(Integer id) {
        // 1. 校验参数：ID 不能为 null 或小于 1（非法 ID 直接抛异常）
        if (id == null || id <= 0) {
            throw new BusinessException("查询失败：用户 ID 必须是正整数");
        }


        // 2. 调用 Mapper 查询
        User user = userMapper.findById(id);


        // 3. 校验结果：如果查不到记录，抛异常提示
        if (user == null) {
            throw new BusinessException("查询失败：ID 为 " + id + " 的用户不存在");
        }

        return user;
    }


    /*
     *2.1根据phone查询单个用户
     * 业务逻辑：前端输入phone非空,不合法抛异常,查询,查询后判断记录是否存在,合法返回值,不合法返回异常
     *
     */
    @Override
    public User findByPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new BusinessException("查询失败：电话phone必须填");
        }


        User user = userMapper.findByPhone(phone);


        if (user == null) {
            throw new BusinessException("查询失败：phone 为 " + phone + " 的用户不存在");
        }

        return user;
    }










    /*
     * 3.0新增用户    //检查参数,进一步检查参数   // (和下面修改用户对比学习)
     *
     * 注意手机号的唯一性
     * 业务逻辑：先检查前端输入参数name非空phone非空(否则抛异常),进一步检查手机号是否存在(原本就不存在新增用户,所以不用查询旧用户,跟不用排除自己和自己重复phone),新增用户
     * 校验参数合法性 + 检查手机号是否已存在（避免重复）
     */
    @Override
    public void addUser(User user) {
        // 1. 校验参数：user (整体)不能为 null，name 和 phone 不能为空
        if (user == null) {
            throw new BusinessException("新增失败：用户信息不能为空");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new BusinessException("新增失败：用户名不能为空");
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new BusinessException("新增失败：手机号不能为空");
        }

        // 2. 检查手机号是否已存在//假设推断,如果手机号存在,就可以找到相应的用户,就是被占用;如果不存在,就找不到,就是null,就是还没有占用
        User existingUser = userMapper.findByPhone(user.getPhone());
        if (existingUser != null) {//如果不为NULL,说明通过手机号查到了用户,即代表该手机号已被占用
            throw new BusinessException("新增失败：手机号 " + user.getPhone() + " 已被占用");
        }

        // 3. 执行新增（调用 Mapper 的 add 方法）
        userMapper.add(user);

    }




    //4.修改用户 //检查参数,找到用户,进一步检查参数,修改用户(具体如下)
    //先检查数据参数name非空,phone非空(否则抛异常),再根据id找到用户是否存在,用于后面排除自己的phone(否则抛异常),再检查phone非重复,再修改用户
    @Override
    public void updateUser(User user) {
        // 1. 校验参数：user 和 id 不能为 null，name/phone 不能为空
        if (user == null || user.getId() == null) {
            throw new BusinessException("修改失败：用户 ID 不能为空");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new BusinessException("修改失败：用户名不能为空");
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new BusinessException("修改失败：手机号不能为空");
        }

        // 2. 检查要修改的用户是否存在（调用上面的 findById 方法，复用校验逻辑）
        User oldUser = findById(user.getId()); // 如果不存在，会直接抛异常

        // 3. 检查新手机号是否被其他用户占用（如果手机号没变，不用检查）
        //如何查重?根据手机号看看能不能查到其他用户,如果能,就是重复,当然先排除查到之前的自己,所以先给这个方法加个if
        if (!Objects.equals(oldUser.getPhone(), user.getPhone())) {
            User existingUser = userMapper.findByPhone(user.getPhone());
            if (existingUser != null) {
                throw new BusinessException("修改失败：手机号 " + user.getPhone() + " 已被占用");
            }
        }

        // 4. 执行修改（调用 Mapper 的 updateAll 方法）
        userMapper.update(user);
    }



    /*
     * 5.0删除用户
     * 业务逻辑：找到用户,删除用户
     */
    @Override
    public void deleteUser(Integer id) {
        // 1. 检查用户是否存在（直接调用 findById，不存在会抛异常）

            findById(id);//本类的方法，上面已经判断逻辑了
            // 2. 执行删除（调用 Mapper 的 deleteAll 方法）
            userMapper.delete(id);


    }

    /*
     * 5.1删除所有user用户
     * 业务逻辑：删除所有用户
     */
    @Override
    public void deleteUserAll() {
        userMapper.deleteUserAll();
    }



    /*
     * 6.条件查询
     *   业务逻辑:传入参数,分为两类参数
     * 一是条件参数,用来进行条件模糊查询的(通过mapper层来实现)现在是服务层,处理逻辑,(xml已经处理过)
     * 二是分页参数,用来设置分页的形式(通过静态方法来实现),现在需要检查参数,就算不合理也要合理,给默认值
     */

    @Override
    public PageInfo<User> queryUserByCondition(String name, String phone, LocalDateTime createTime, LocalDateTime updateTime, Integer pageNum, Integer pageSize) {
        // 处理分页参数默认值
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        // 启动分页
        PageHelper.startPage(pageNum, pageSize);
        // 执行条件查询（XML 和 Mapper 无需修改）
        List<User> userList = userMapper.queryUserByCondition(name, phone, createTime, updateTime);
        // 封装分页结果
        return new PageInfo<>(userList);
    }








}




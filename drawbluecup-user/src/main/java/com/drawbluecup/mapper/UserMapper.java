package com.drawbluecup.mapper;

//Mapper接口,用来列出方法蓝图,由xml来用Sql语句写具体方法

// 导入User实体类（因为方法返回值是List<User>）

import com.drawbluecup.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


// 导入List集合（因为返回值是列表）
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

//Spring找接口注解:
@Mapper

public interface UserMapper  {
    //注意传参类型和返回值类型

    //----------------------------------整体数据-----------------------------------//
    // 查询方法，返回User列表
    List<User> findAll();//只有“查”才需要返回值，因为就是要显示数据

    //下面不用返回值，增删改在表中操作
    //添加所有字段数据,多个数据封装成user传递
    void add(User user);//直接在下一行增加单行数据

    //因为删除操作只需要 id 一个参数，不需要传递整个 User 对象
    void delete(Integer id);//通过id查询(但不显示)并删除单行数据
    void deleteUserAll();//删除user表所有😈

    void update(User user);//通过id查询(但不显示)并更新单行数据

    //------------------------------------条件查询-------------------------------------------//

    /**
     * 按条件查询用户列表（支持模糊查询）
     * @param name 姓名（模糊匹配，如“张”会匹配“张三”“张四”）
     * @param phone 手机号（模糊匹配，如“138”会匹配“13800138000”“13812345678”）
     * @return 符合条件的用户列表
     */

    List<User> queryUserByCondition(
            @Param("name") String name,   // @Param注解：明确SQL中参数的名称，避免MyBatis参数绑定错误
            @Param("phone") String phone,
            @Param("createTime") LocalDateTime createTime,// 告诉 MyBatis：这个参数在 SQL 中叫 createTime
            @Param("updateTime") LocalDateTime updateTime// 告诉 MyBatis：这个参数在 SQL 中叫 updateTime

//少了逗号的话，编译器会把 createTime 和 updateTime 当成一个参数处理，导致 “实际参数数量与方法定义不符”（原本定义 4 个参数，编译器会误认为是 3 个）。
    );


    //-------------------------------------局部数据-----------------------------------//

    User findById(Integer id);//
    User findByPhone(String phone);//

}

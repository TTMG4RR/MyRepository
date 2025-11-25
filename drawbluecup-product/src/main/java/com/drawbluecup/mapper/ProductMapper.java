package com.drawbluecup.mapper;

import com.drawbluecup.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    //查询所有商品信息
    List<Product> findAll();
    //根据id查询商品
    Product findById(@Param("id")Integer id);
    //根据name查询商品(支持模糊)
    Product findByName(@Param("name")String name);

    //删除商品(根据id)
    void deleteById(@Param("id")Integer id);
    //删除所有商品
    void deleteAll();


    //新增商品
    void add(Product product);

    //更新商品
    void update(Product product);

}

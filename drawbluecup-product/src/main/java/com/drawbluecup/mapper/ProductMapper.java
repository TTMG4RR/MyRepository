package com.drawbluecup.mapper;

import com.drawbluecup.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    /**查询所有商品信息
     *
     * @return
     */
    List<Product> findAll();

    /**根据id查询商品
     *
     * @param id
     * @return
     */

    Product findById(@Param("id")Integer id);

    /**根据name查询商品(支持模糊)
     *
     * @param name
     * @return
     */
    Product findByName(@Param("name")String name);

//<======================================================================================>

    /**删除商品(根据id)
     *
     * @param id
     */
    void deleteById(@Param("id")Integer id);


    /**删除所有商品
     *
     */
    void deleteAll();

//<======================================================================================>


    /**新增商品
     *
     * @param product
     */
    void add(Product product);

//<======================================================================================>


    /**
     * 更新商品
     * @param product
     */
    void update(Product product);


//<======================================================================================>

    /**
     * 查询商品及其关联的订单列表（多对多反向查询）
     * @param productId 商品ID
     * @return 包含订单列表的商品对象
     */
    Product findProductWithOrders(@Param("productId") Integer productId);

}

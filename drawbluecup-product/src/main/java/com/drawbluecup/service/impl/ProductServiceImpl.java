package com.drawbluecup.service.impl;

import com.drawbluecup.entity.Product;
import com.drawbluecup.exception.BusinessException;
import com.drawbluecup.mapper.ProductMapper;
import com.drawbluecup.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//服务层实现类
//服务层实现类来填充服务层接口的意义(帮助mapper处理业务逻辑)，然后控制层来调用服务层接口
//不合理?直接抛异常🤪
@Service
public class ProductServiceImpl implements ProductService {

    // 注入 ProductMapper（MyBatis 自动生成的代理对象），通过它操作数据库
    // @Autowired 会让 Spring 自动把 UserMapper 的实例赋值给这个变量，不用手动 new
    @Autowired
    private ProductMapper productMapper;


    //-----------------------------------------------------------------------//
    //查询

    //查询所有商品信息
    @Override
    public List<Product> findAll(){
        //没有检验,如果没有查询到返回空列表
        return productMapper.findAll();

    }

    //根据id查询商品
    @Override
    public Product findById(Integer id){
        //检验id合法性:ID 不能为 null 或小于 1（非法 ID 直接抛异常）
        if (id == null || id <= 0) {
            throw new BusinessException(400103,"输入id不可为空或小于0");
        }
        return productMapper.findById(id);

    }

    //根据name查询商品
    @Override
    public Product findByName(String name){
        //检验name合法性:name 不能为 null （非法  直接抛异常）
        if (name == null ) {
            throw new BusinessException(400103,"输入name不可为空");
        }
        return productMapper.findByName(name);

    }



    //以下增删改都是操作数据库,不应该返回数据(返回值为void)



    //删除
    //删除商品根据id
    @Override
    public void deleteById(Integer id){
        //检验id合法性:ID 不能为 null 或小于 1（非法 ID 直接抛异常）
        if (id == null || id <= 0) {
            throw new BusinessException(400103,"输入id不可为空或小于0");
        }
        productMapper.deleteById(id);
    }

    //删除所有商品
    @Override
    public void deleteAll(){

        productMapper.deleteAll();
    }




    //新增
    //新增商品
    @Override
    public void add(Product product){
        /*检验:
            1参数是否合理?整体为null?局部为null?id小于0?
            2新增对象里面字段是否可以重复?(商品名不可重复)
                先把新增对象的商品名用来搜索(复用findByName)
                    若为null,说明没有找到,说明没有重复
                    若不为null,抛异常提醒,"已存在该商品"

         */

        //1.
        if (product == null) {
            throw new BusinessException(400103,"新增商品不可为空");
        }
        if (product.getName() == null) {
            throw new BusinessException(400103,"新增商品的商品名不可为空");
        }



        //2.
        if( productMapper.findByName(product.getName()) != null ){
            throw new BusinessException(400303,"新增商品的商品名已存在");
        }

        productMapper.add(product);
    }


    //更新
    //更新商品(基于id查询来更新,并不能修改id)
    @Override
    public void update(Product product){


        // 1. 检验ID的合法性
        if (product.getId() == null || product.getId() <= 0) {
            throw new BusinessException(400103,"输入id不可为空或小于0");
        }

        // 2. 先查询数据库，获取当前要更新的商品信息
        Product existingProduct = productMapper.findById(product.getId());//这里不会抛异常,因为调用的时mapper层的方法
        if (existingProduct == null) {
            throw new BusinessException(400403,"要更新的商品不存在");
        }

        // 3. 检查商品名称是否有变化。如果名称没变，就不需要进行重复校验!
        if (!existingProduct.getName().equals(product.getName())) {
            // 4. 名称变了，才需要检查新名称是否已被其他商品使用
            Product productWithSameName = productMapper.findByName(product.getName());
            if (productWithSameName != null) {
                // 找到了一个使用新名称的商品，说明名称重复了
                throw new BusinessException(400303,"修改商品的商品名已存在");
            }
        }

        // 5. 所有校验通过，执行更新操作
        productMapper.update(product);

        /*

        //检验id合理?
        if (product.getId() == null || product.getId() <= 0) {
            throw new BusinessException("输入id不可为空或小于0");
        }
        //用来修改的数据是否和之前表(除了自己)的有重复?
        if( productMapper.findByName(product.getName()) != null &&
               ! productMapper.findByName(product.getName()) .equals( productMapper.findById(product.getId())) ){
            throw new BusinessException("修改商品的商品已存在");
        }
        productMapper.update(product);



         */
    }


}

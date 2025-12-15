package com.drawbluecup.service;

import com.drawbluecup.entity.Product;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {

    PageInfo<Product> findAll(Integer pageNum, Integer pageSize);
    Product findById(Integer id);
    Product findByName(String name);
    void deleteById(Integer id);
    void deleteAll();
    void add(Product product);
    void update(Product product);
    public Product findProductWithOrders(Integer productId);




}

package com.drawbluecup.service;

import com.drawbluecup.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(Integer id);
    Product findByName(String name);
    void deleteById(Integer id);
    void deleteAll();
    void add(Product product);
    void update(Product product);




}

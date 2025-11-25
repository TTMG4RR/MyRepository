package com.drawbluecup.entity;

import java.time.LocalDateTime;

/**
 * 用户收藏商品的多对多关系实体
 */
public class Favorite {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private LocalDateTime createTime;
    /**
     * 关联的商品详情，便于通过 JOIN 一次性取回
     */
    private Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}


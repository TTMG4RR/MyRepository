package com.drawbluecup.service.impl;

import com.drawbluecup.entity.Favorite;
import com.drawbluecup.entity.Product;
import com.drawbluecup.entity.User;
import com.drawbluecup.exception.BusinessException;
import com.drawbluecup.mapper.FavoriteMapper;
import com.drawbluecup.mapper.ProductMapper;
import com.drawbluecup.mapper.UserMapper;
import com.drawbluecup.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Integer userId, Integer productId) {
        validateUser(userId);
        validateProduct(productId);
        Favorite existing = favoriteMapper.findOne(userId, productId);
        if (existing != null) {
            throw new BusinessException(400501, "该商品已被收藏");
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favoriteMapper.insert(favorite);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Integer userId, Integer productId) {
        validateUser(userId);
        validateProduct(productId);
        int rows = favoriteMapper.delete(userId, productId);
        if (rows == 0) {
            throw new BusinessException(400404, "收藏记录不存在或已删除");
        }
    }

    @Override
    public List<Favorite> listByUser(Integer userId) {
        validateUser(userId);
        return favoriteMapper.findByUserId(userId);
    }

    private User validateUser(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(400101, "用户 ID 不合法");
        }
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(400404, "用户不存在");
        }
        return user;
    }

    private Product validateProduct(Integer productId) {
        if (productId == null || productId <= 0) {
            throw new BusinessException(400102, "商品 ID 不合法");
        }
        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new BusinessException(400404, "商品不存在");
        }
        return product;
    }
}



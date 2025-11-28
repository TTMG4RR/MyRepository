package com.drawbluecup.service;

import com.drawbluecup.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    void addFavorite(Integer userId, Integer productId);

    void removeFavorite(Integer userId, Integer productId);

    List<Favorite> listByUser(Integer userId);
}



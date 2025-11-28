package com.drawbluecup.mapper;

import com.drawbluecup.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    List<Favorite> findByUserId(@Param("userId") Integer userId);

    Favorite findOne(@Param("userId") Integer userId, @Param("productId") Integer productId);

    void insert(Favorite favorite);

    int delete(@Param("userId") Integer userId, @Param("productId") Integer productId);
}



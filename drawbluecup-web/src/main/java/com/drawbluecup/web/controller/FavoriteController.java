package com.drawbluecup.web.controller;

import com.drawbluecup.entity.Favorite;
import com.drawbluecup.result.Result;
import com.drawbluecup.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
@Validated
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public Result<Void> addFavorite(@Valid @RequestBody FavoriteRequest request) {
        favoriteService.addFavorite(request.getUserId(), request.getProductId());
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> removeFavorite(@Valid @RequestBody FavoriteRequest request) {
        favoriteService.removeFavorite(request.getUserId(), request.getProductId());
        return Result.success();
    }

    @GetMapping("/user/{userId}")
    public Result<List<Favorite>> listUserFavorites(@PathVariable Integer userId) {
        return Result.success(favoriteService.listByUser(userId));
    }

    public static class FavoriteRequest {
        @NotNull(message = "用户ID不能为空")
        private Integer userId;
        @NotNull(message = "商品ID不能为空")
        private Integer productId;

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
    }
}



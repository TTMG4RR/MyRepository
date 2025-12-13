package com.drawbluecup.dto.user.LoginAndToken;

import lombok.Data;

    /**
     * 登录入参DTO
     */
    @Data
    public class UserLoginDTO {
        private Integer userId ;
        private String password;
    }


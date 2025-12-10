package com.drawbluecup.dto.user.LoginAndToken;

import lombok.Data;

/**
 * 刷新Token入参DTO
 */
@Data
public class UserRefreshTokenDTO {
    private String refreshToken;
}
package com.drawbluecup.dto.user.LoginAndToken;

import lombok.Data;

/**
 * 刷新Token出参DTO
 */
@Data
public class UserRefreshTokenRespDTO {
    private String newAccessToken;

}
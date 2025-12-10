package com.drawbluecup.dto.user.LoginAndToken;

import lombok.Data;

/**
 * 登录出参DTO（返回双Token）
 */
@Data
public class UserLoginRespDTO {
    private String accessToken;
    private String refreshToken;
    private String userName;
}

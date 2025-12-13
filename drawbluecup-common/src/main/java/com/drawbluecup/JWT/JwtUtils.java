package com.drawbluecup.JWT;


import com.drawbluecup.exception.BusinessException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey; // 临时兼容（JDK 17仍保留javax.crypto）
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类：新增双Token过期时间常量
 */
public class JwtUtils {
    // 密钥（生产环境建议用32位随机字符串，且保密）
    private static final String SECRET = "12345678901234567890123456789012";

    // ========== 新增：双Token过期时间 ==========
    public static final long ACCESS_TOKEN_EXPIRE = 2 * 60 * 60 * 1000; // Access Token：2小时
    public static final long REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000; // Refresh Token：7天


    //==============================方法==================================//

    /**
     * 生成加密密钥
     */
    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * 生成Token（通用方法，支持Access/Refresh Token）
     * @param userInfo 存入Token的用户信息（如userId、phone）
     * @param expireTime 过期时间（毫秒）
     * @return JWT Token字符串
     */
    public static String generateToken(Map<String, Object> userInfo, long expireTime) {
        return Jwts.builder()
                .setClaims(userInfo) // 存入用户信息（Payload）
                .setExpiration(new Date(System.currentTimeMillis() + expireTime)) // 过期时间
                .signWith(getKey()) // 签名（防篡改）
                .compact();
    }

    /**
     * 解析Token
     * @param token 前端传入的Token字符串
     * @return Token中的用户信息（如userId）
     * @throws RuntimeException Token无效/过期时抛出异常
     */
    public static Map<String, Object> parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey()) // 用相同密钥验证
                    .build()
                    .parseClaimsJws(token) // 解析Token
                    .getBody(); // 获取Payload中的用户信息
        } catch (Exception e) {
            throw new BusinessException("Token无效或已过期：" + e.getMessage());//自定义异常BusinessException不要写错了
        }
    }
}
package com.drawbluecup.JWT;

import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;//修正拦截器的包导入（把javax换成jakarta）
import java.util.Map;

/**
 * Token拦截器：仅校验Access Token（Refresh Token接口不拦截）
 */
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 排除登录接口、刷新Token接口（无需校验Access Token）
        String requestUrl = request.getRequestURI();
        if (requestUrl.contains("/login") || requestUrl.contains("/refreshToken")) {
            return true; // 放行
        }

        // 2. 从请求头获取Access Token（约定前端字段名：Access-Token）
        String accessToken = request.getHeader("Access-Token");
        if (accessToken == null || accessToken.isEmpty()) {
            // 手动返回错误响应
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write("{\"code\":401,\"message\":\"Access Token为空，请先登录\"}");
            return false; // 拒绝放行
        }

        // 3. 排除静态资源（仅拦截Controller接口）
        if (!(handler instanceof org.springframework.web.method.HandlerMethod)) {
            return true;
        }

        // 4. 解析Access Token（验证有效性），并提取用户信息
        Map<String, Object> userInfo = JwtUtils.parseToken(accessToken);
        Integer userId = (Integer) userInfo.get("userId");
        // 将用户ID存入request，供后续接口使用！！
        request.setAttribute("loginUserId", userId);

        return true; // 校验通过，放行
    }
}
package com.drawbluecup.dto.user;

//入参 DTO:前端传，只含需要的字段

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
     * 新增用户的入参DTO：只包含前端需要传递的字段
     */
    @Data // 替代手动写getter/setter
    @Schema(name = "UserAddDTO", description = "新增用户入参") // Swagger注解：文档里说明这个DTO的作用

    public class UserAddDTO {
        @Schema(description = "用户名称", required = true) // 文档里标注字段说明

        private String name; // 用户名

        @Schema(description = "用户电话",required = true)

        private String phone;

        @Schema(description = "用户密码",required = true)

        private String password;
}

package com.drawbluecup.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Schema(name = "UserRespDTOWithout", description = "用户信息出参(仅用户)")// Swagger注解：文档里说明这个DTO的作用

public class UserRespDTOWithout {
    @Schema(description = "用户主键id")
    private Integer id;
    @Schema(description = "用户名称")
    private String name;
    @Schema(description = "用户电话")
    private String phone;
    @Schema(description = "用户创建时间")
    private LocalDateTime createTime;
    @Schema(description = "用户更新时间")
    private LocalDateTime updateTime;

}
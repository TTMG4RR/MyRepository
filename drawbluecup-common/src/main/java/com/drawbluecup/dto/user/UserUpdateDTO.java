package com.drawbluecup.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "UserUpdateDTO",description = "更新用户入参")
public class UserUpdateDTO {

    @Schema(description = "用户主键id", required = true)
    private Integer id;

    @Schema(description = "用户名称", required = true) // 文档里标注字段说明

    private String name; // 用户名

    @Schema(description = "用户电话", required = true)

    private String phone;



}
package com.drawbluecup.dto.user;


import com.drawbluecup.validation.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "UserUpdateDTO",description = "更新用户入参")
public class UserUpdateDTO {

    @Schema(description = "用户主键id(必传,用来定位)", required = true)
    private Integer id;

    @Schema(description = "用户名称（不传则保留原值)", required = true) // 文档里标注字段说明

    private String name; // 用户名

    @Schema(description = "用户电话(不传则保留原值)", required = true)
    @Phone
    private String phone;

    @Schema(description = "用户密码(不传则保留原值)", required = true)

    private String password;

}
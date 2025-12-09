package com.drawbluecup.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品出参DTO：只包含前端需要展示的字段（比如id+name）
 */
@Data
@Schema(name = "ProductRespDTO", description = "商品信息出参")// Swagger注解：文档里说明这个DTO的作用

public class UserRespDTO {

//WIP
}
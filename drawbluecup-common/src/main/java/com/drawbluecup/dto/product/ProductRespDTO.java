package com.drawbluecup.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品出参DTO：只包含前端需要展示的字段（比如id+name）
 */
@Data
@Schema(name = "ProductRespDTO", description = "商品信息出参")// Swagger注解：文档里说明这个DTO的作用

public class ProductRespDTO {

    @Schema(description = "商品ID")// 文档里标注字段说明
    private Integer id; // 前端需要看的id

    @Schema(description = "商品名称")
    private String name; // 前端需要看的name

    // 没有其他字段（比如如果Product有price，不想暴露就不加）
}
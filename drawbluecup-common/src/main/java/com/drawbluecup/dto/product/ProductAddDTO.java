package com.drawbluecup.dto.product;

//入参 DTO:前端新增商品时传，只含需要的字段

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
     * 新增商品的入参DTO：只包含前端需要传递的字段（你的业务里只有name）
     */
    @Data // 替代手动写getter/setter（和你实体类的lombok用法一致）
    @Schema(name = "ProductAddDTO", description = "新增商品入参") // Swagger注解：文档里说明这个DTO的作用

    public class ProductAddDTO {
        @Schema(description = "商品名称", required = true, example = "苹果") // 文档里标注字段说明

        private String name; // 只有前端需要传的name字段，无id（id是自增，前端不用传）
}

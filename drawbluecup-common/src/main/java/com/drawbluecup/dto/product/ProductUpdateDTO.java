package com.drawbluecup.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新增商品的入参DT0：只包含前端需要传递的字段（你的业务里只有name）
 */
@Data
@Schema(name = "ProductUpdateDTO",description = "更新商品入参")
public class ProductUpdateDTO {
    @Schema(description = "用于定位的id")
    private Integer id;

    @Schema(description = "商品名称")
    private String name;

}

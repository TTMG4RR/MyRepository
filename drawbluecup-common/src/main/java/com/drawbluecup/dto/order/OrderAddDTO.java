package com.drawbluecup.dto.order;

//入参 DTO:前端新增订单时传，只含需要的字段

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
     * 新增订单的入参DTO：只包含前端需要传递的字段
     */
    @Data // 替代手动写getter/setter
    @Schema(name = "OrderAddDTO", description = "新增订单入参") // Swagger注解：文档里说明这个DTO的作用

    public class OrderAddDTO {
        @Schema(description = "订单编号", required = true) // 文档里标注字段说明

        private String orderNo; // 订单编号（前端生成/传递，唯一标识订单）

        @Schema(description = "所属用户的id", required = true) // 文档里标注字段说明

        private Integer userId;// 下单用户ID（关联用户表主键）

    //DTO ，实体类的字段顺序、SQL 语句的字段顺序，三者之间互不影响，核心依赖 “字段名匹配”，和顺序无关。
}

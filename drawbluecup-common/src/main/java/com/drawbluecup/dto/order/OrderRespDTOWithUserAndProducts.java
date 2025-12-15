package com.drawbluecup.dto.order;


import com.drawbluecup.dto.product.ProductRespDTOWithout;
import com.drawbluecup.dto.user.UserRespDTOWithout;
import com.drawbluecup.entity.Product;
import com.drawbluecup.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Schema(name = "OrderRespDTOWithUserAndProducts", description = "订单信息出参(一对多）")// Swagger注解：文档里说明这个DTO的作用

public class OrderRespDTOWithUserAndProducts {

    @Schema(description = "订单主键id")
    private Integer id;
    @Schema(description = "订单编号")
    private String orderNo;
    @Schema(description = "订单创建时间")
    private LocalDateTime createTime;

    @Schema(description = "所属用户id")
    private Integer userId;
    @Schema(description = "所属用户")
    private UserRespDTOWithout user;

    @Schema(description = "包含商品")
    private List<ProductRespDTOWithout> products;


}
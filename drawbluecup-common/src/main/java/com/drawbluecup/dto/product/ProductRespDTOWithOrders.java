package com.drawbluecup.dto.product;

import com.drawbluecup.convert.DTOConvertUtils;
import com.drawbluecup.dto.order.OrderRespDTOWithUserAndProducts;
import com.drawbluecup.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
@Data
public class ProductRespDTOWithOrders {

    @Schema(description = "商品主键ID")// 文档里标注字段说明
    private Integer id; // 前端需要看的id

    @Schema(description = "商品名称")
    private String name; // 前端需要看的name

    @Schema(description = "拥有订单")
    private List<OrderRespDTOWithUserAndProducts> Orders ; // 前端需要看的name
}

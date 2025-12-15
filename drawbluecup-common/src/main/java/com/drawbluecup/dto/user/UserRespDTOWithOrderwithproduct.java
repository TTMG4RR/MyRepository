package com.drawbluecup.dto.user;

import com.drawbluecup.dto.order.OrderRespDTOWithUserAndProducts;
import lombok.Data;

import java.util.List;

@Data
public class UserRespDTOWithOrderwithproduct {

    private UserRespDTOWithout userRespDTOWithout;
    private List<OrderRespDTOWithUserAndProducts> orderDTOWithUserAndProducts;

}

package com.AShop.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartOrderDto {
    private Long cartItemId;

    private List<CartOrderDto> cartOrderDtoList;
    //장바구니에서 여러 상품을 주문하므로 자기 자신을 List로 가지고 있도록 함
}

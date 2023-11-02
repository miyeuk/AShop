package com.AShop.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;

    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;


    //Query로 결과 조회시 MainItemDto 객체로 바로 받아옴
    //컴 파일 하여 QmainItemDto
    @QueryProjection
    public MainItemDto(Long id, String itemNm , String itemDetail , String imgUrl, Integer price){
        this.id = id;
        this.itemNm=itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}

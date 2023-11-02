package com.AShop.dto;


import com.AShop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    private static ModelMapper modelMapper =new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg,ItemImgDto.class);
        //itemImg 엔티티 객체를 파라미터로 받아서 item 객체의 자료형과 맴버 변수의 이름이 같을 때 ItemImgDto로 값을 복사해서 반환.
        //static 메소드를 선언해 ItemImgDto 객체를 생성하지 않아도 호출 할 수 있도록 할 예정.
    }
}
